package mongodataRepositories;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceTrainingPlanRepository;
import model.TrainingPlan;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.TrainingPlanItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;

public class TrainingPlanRepository implements InterfaceTrainingPlanRepository {
    private MongoDatabase mongoDatabase;

    public TrainingPlanRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createTrainingPlan(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());

            int trainingPlanID = getNextId();

            Document doc = new Document();
            doc.put(TrainingPlanItems.training_plan_id.get(), trainingPlanID);
            doc.put(TrainingPlanItems.creator_user_id.get(), userID);

            collection.insertOne(doc);

            return trainingPlanID;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public TrainingPlan getTrainingPlanByID(int trainingPlanID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());

            Bson filter = eq(TrainingPlanItems.training_plan_id.get(), trainingPlanID);
            Document doc = collection.find(filter).first();

            int creatorUserId = (int) doc.get(TrainingPlanItems.creator_user_id.get());

            TrainingPlan tp = new TrainingPlan(trainingPlanID, creatorUserId);

            return tp;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public TrainingPlan[] getTrainingPlanByUserID(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());

            Bson filter = eq(TrainingPlanItems.creator_user_id.get(), userID);
            var docs = collection.find(filter);
            var plans = new ArrayList<TrainingPlan>(){};

            for (Document doc : docs) {
                int trainingPlanId = (int) doc.get(TrainingPlanItems.training_plan_id.get());

                TrainingPlan tp = new TrainingPlan(trainingPlanId, userID);

                plans.add(tp);
            }

            return plans.toArray(new TrainingPlan[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterTrainingPlan(TrainingPlan trainingPlan) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());

            Bson filter = eq(TrainingPlanItems.training_plan_id.get(), trainingPlan.getID());

            Bson update = combine(
                set(TrainingPlanItems.creator_user_id.get(), trainingPlan.getCreatorUserID())
            );

            UpdateResult result = collection.updateOne(filter, update);

            if (result.getModifiedCount() < 1) {
                throw new DataLayerException();
            }

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeTrainingPlan(int trainingPlanID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());

            Bson filter = eq(TrainingPlanItems.training_plan_id.get(), trainingPlanID);

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
            var collection = mongoDatabase.getCollection(Collections.training_plan.get());
    
            Document maxDoc = collection.find()
                    .sort(new Document(TrainingPlanItems.training_plan_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxDoc == null) {
                return 1;
            }
    
            return (int) maxDoc.get(TrainingPlanItems.training_plan_id.get()) + 1;
    
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }
    
}
