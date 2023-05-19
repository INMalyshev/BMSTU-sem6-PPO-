package mongodataRepositories;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceApproachRepository;
import model.Approach;
import model.ExerciseType;
import mongodataRepositories.items.ApproachItems;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.ExcersizeTypeItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;

public class ApproachRepository implements InterfaceApproachRepository {

    private MongoDatabase mongoDatabase;

    public ApproachRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createApproach(int trainingID) throws CoreException {
        try {
            int approachID = getNextId();

            var collection = mongoDatabase.getCollection(Collections.approach.get());

            Document document = new Document();
            document.put(ApproachItems.approach_id.get(), approachID);
            document.put(ApproachItems.training_id.get(), trainingID);
            document.put(ApproachItems.amount.get(), 0);
            document.put(ApproachItems.expected_amount.get(), -999);
            document.put(ApproachItems.excersize_type_id.get(), getExerciseTypeIdByName(ExerciseType.Squat));
            document.put(ApproachItems.completed.get(), false);

            collection.insertOne(document);

            return approachID;
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Approach getApproachByID(int approachID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach.get());
            Bson filter = eq(ApproachItems.approach_id.get(), approachID);
            Document doc = collection.find(filter).first();

            ExerciseType type = getExerciseTypeById((int) doc.get(ApproachItems.excersize_type_id.get()));
            int amount = (int) doc.get(ApproachItems.amount.get());
            int expectedAmount = (int) doc.get(ApproachItems.expected_amount.get());
            int trainingID = (int) doc.get(ApproachItems.training_id.get());

            return new Approach(approachID, trainingID, amount, expectedAmount, type);
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Approach[] getApproachByTrainingID(int trainingID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach.get());
            Bson filter = eq(ApproachItems.training_id.get(), trainingID);
            var docs = collection.find(filter);
            var aprs = new ArrayList<Approach>();

            for (var doc : docs) {
                ExerciseType type = getExerciseTypeById((int) doc.get(ApproachItems.excersize_type_id.get()));
                int amount = (int) doc.get(ApproachItems.amount.get());
                int expectedAmount = (int) doc.get(ApproachItems.expected_amount.get());
                int approachID = (int) doc.get(ApproachItems.approach_id.get());

                var apr = new Approach(approachID, trainingID, amount, expectedAmount, type);

                aprs.add(apr);
            }

            return aprs.toArray(new Approach[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterApproach(Approach approach) throws CoreException {
        try {
            int typeId = getExerciseTypeIdByName(approach.getType());

            var collection = mongoDatabase.getCollection(Collections.approach.get());
            Bson filter = eq(ApproachItems.approach_id.get(), approach.getID());
            Bson update = combine(
                set(ApproachItems.expected_amount.get(), approach.getExpectedAmount()),
                set(ApproachItems.amount.get(), approach.getAmount()),
                set(ApproachItems.completed.get(), approach.isCompleted()),
                set(ApproachItems.excersize_type_id.get(), typeId),
                set(ApproachItems.training_id.get(), approach.GetTrainingID())
            );

            UpdateResult updateResult = collection.updateOne(filter, update);

            if (updateResult.getModifiedCount() <= 0) {
                throw new DataLayerException();
            }
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeApproach(int approachID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach.get());
            Bson filter = eq(ApproachItems.approach_id.get(), approachID);
            
            DeleteResult result = collection.deleteMany(filter);

            if (result.getDeletedCount() < 1) {
                throw new DataLayerException();
            }
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private int getNextId() throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach.get());
    
            Document maxClient = collection.find()
                    .sort(new Document(ApproachItems.approach_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxClient == null) {
                return 1;
            }
    
            return (int) maxClient.get(ApproachItems.approach_id.get()) + 1;
    
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private ExerciseType getExerciseTypeById(int id) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.excersize_type.get());
            Bson filter = eq(ExcersizeTypeItems.excersize_type_id.get(), id);
            Document doc = collection.find(filter).first();

            String name = (String) doc.get(ExcersizeTypeItems.name.get());

            return getExerciseTypeByName(name);

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }
    
    private ExerciseType getExerciseTypeByName(String name) throws CoreException {
        ExerciseType[] types = ExerciseType.values();

        for (ExerciseType type : types) {
            if (name.equals(type.getTitle())) {
                return type;
            }
        }

        throw new DataLayerException();
    }

    private int getExerciseTypeIdByName(ExerciseType type) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.excersize_type.get());
            Document doc = collection.find(new Document(ExcersizeTypeItems.name.get(), type.getTitle())).first();
    
            return (int) doc.get(ExcersizeTypeItems.excersize_type_id.get());

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }
    
}
