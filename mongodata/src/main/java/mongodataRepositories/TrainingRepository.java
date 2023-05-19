package mongodataRepositories;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceTrainingRepository;
import model.Training;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.TrainingItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;

public class TrainingRepository implements InterfaceTrainingRepository {
    private MongoDatabase mongoDatabase;

    public TrainingRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createTraining(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());

            int trainingId = getNextId();

            Document doc = new Document();

            doc.put(TrainingItems.training_id.get(), trainingId);
            doc.put(TrainingItems.completed.get(), false);
            doc.put(TrainingItems.holder_user_id.get(), userID);
            collection.insertOne(doc);

            return trainingId;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training getTrainingByID(int trainingID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());

            Bson filter = eq(TrainingItems.training_id.get(), trainingID);

            Document doc = collection.find(filter).first();

            int holderUserId = (int) doc.get(TrainingItems.holder_user_id.get());
            Boolean completed = (Boolean) doc.get(TrainingItems.completed.get());

            Training training = new Training(trainingID, holderUserId);
            training.SetCompleted(completed);

            return training;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training[] getTrainingsByUserID(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());
            Bson filter = eq(TrainingItems.holder_user_id.get(), userID);

            var docs = collection.find(filter);
            var trainings = new ArrayList<Training>(){};

            for (Document doc : docs) {
                int id = (int) doc.get(TrainingItems.training_id.get());
                Boolean completed = (Boolean) doc.get(TrainingItems.completed.get());

                Training training = new Training(id, userID);
                training.SetCompleted(completed);

                trainings.add(training);
            }

            return trainings.toArray(new Training[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training[] getDoneTrainingsByUserID(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());
            Bson filter = and(
                eq(TrainingItems.holder_user_id.get(), userID),
                eq(TrainingItems.completed.get(), true)
            );

            var docs = collection.find(filter);
            var trainings = new ArrayList<Training>(){};

            for (Document doc : docs) {
                int id = (int) doc.get(TrainingItems.training_id.get());
                Boolean completed = (Boolean) doc.get(TrainingItems.completed.get());

                Training training = new Training(id, userID);
                training.SetCompleted(completed);

                trainings.add(training);
            }

            return trainings.toArray(new Training[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training[] getPlannedTrainingsByUserID(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());
            Bson filter = and(
                eq(TrainingItems.holder_user_id.get(), userID),
                eq(TrainingItems.completed.get(), false)
            );

            var docs = collection.find(filter);
            var trainings = new ArrayList<Training>(){};

            for (Document doc : docs) {
                int id = (int) doc.get(TrainingItems.training_id.get());
                Boolean completed = (Boolean) doc.get(TrainingItems.completed.get());

                Training training = new Training(id, userID);
                training.SetCompleted(completed);

                trainings.add(training);
            }

            return trainings.toArray(new Training[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterTraining(Training training) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());

            Bson filter = eq(TrainingItems.training_id.get(), training.getID());

            Bson update = combine(
                set(TrainingItems.completed.get(), training.GetCompleted())
            );

            collection.updateOne(filter, update);

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeTraining(int trainingID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training.get());

            Bson filter = eq(TrainingItems.training_id.get(), trainingID);

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
            var collection = mongoDatabase.getCollection(Collections.training.get());
    
            Document maxDoc = collection.find()
                    .sort(new Document(TrainingItems.training_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxDoc == null) {
                return 1;
            }
    
            return (int) maxDoc.get(TrainingItems.training_id.get()) + 1;
    
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }
    
}
