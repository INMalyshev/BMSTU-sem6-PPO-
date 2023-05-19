package mongodataRepositories;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceApproachPlanRepository;
import model.ApproachPlan;
import model.ExerciseType;
import mongodataRepositories.items.ApproachPlanItems;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.ExcersizeTypeItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;

public class ApproachPlanRepository implements InterfaceApproachPlanRepository {
    private MongoDatabase mongoDatabase;

    public ApproachPlanRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createApproachPlan(int trainingPlanID) throws CoreException {
        try {
            int approachPlanID = getNextId();
            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());

            Document document = new Document();
            document.put(ApproachPlanItems.approach_plan_id.get(), approachPlanID);
            document.put(ApproachPlanItems.expected_amount.get(), -999);
            document.put(ApproachPlanItems.training_plan_id.get(), trainingPlanID);
            document.put(ApproachPlanItems.excersize_type_id.get(), 1);

            collection.insertOne(document);

            return approachPlanID;
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public ApproachPlan getApproachPlanByID(int approachPlanID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());
            Bson filter = eq(ApproachPlanItems.approach_plan_id.get(), approachPlanID);
            Document doc = collection.find(filter).first();

            ExerciseType type = getExerciseTypeById((int) doc.get(ApproachPlanItems.excersize_type_id.get()));
            int trainingPlanId = (int) doc.get(ApproachPlanItems.training_plan_id.get());
            int amount = (int) doc.get(ApproachPlanItems.expected_amount.get());

            return new ApproachPlan(trainingPlanId, type, amount, approachPlanID);

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public ApproachPlan[] getApproachPlanByTrainingPlanID(int trainingPlanID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());
            Bson filter = eq(ApproachPlanItems.training_plan_id.get(), trainingPlanID);
            
            var docs = collection.find(filter);
            ArrayList<ApproachPlan> plans = new ArrayList<ApproachPlan>(){};

            for (Document doc : docs) {
                ExerciseType type = getExerciseTypeById((int) doc.get(ApproachPlanItems.excersize_type_id.get()));
                int approachPlanID = (int) doc.get(ApproachPlanItems.approach_plan_id.get());
                int amount = (int) doc.get(ApproachPlanItems.expected_amount.get());

                ApproachPlan ap = new ApproachPlan(trainingPlanID, type, amount, approachPlanID);

                plans.add(ap);
            }

            return plans.toArray(new ApproachPlan[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterApproachPlan(ApproachPlan approachPlan) throws CoreException {
        try {
            int typeId = getExerciseTypeIdByName(approachPlan.getType());

            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());
            Bson filter = eq(ApproachPlanItems.approach_plan_id.get(), approachPlan.getID());
            Bson update = combine(
                set(ApproachPlanItems.expected_amount.get(), approachPlan.getExpectedAmount()),
                set(ApproachPlanItems.excersize_type_id.get(), typeId),
                set(ApproachPlanItems.training_plan_id.get(), approachPlan.getTrainingPlanID())
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
    public void removeApproachPlan(int approachPlanID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());
            Bson filter = eq(ApproachPlanItems.approach_plan_id.get(), approachPlanID);
            
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
            var collection = mongoDatabase.getCollection(Collections.approach_plan.get());
    
            Document maxClient = collection.find()
                    .sort(new Document(ApproachPlanItems.approach_plan_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxClient == null) {
                return 1;
            }
    
            return (int) maxClient.get(ApproachPlanItems.approach_plan_id.get()) + 1;
    
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
