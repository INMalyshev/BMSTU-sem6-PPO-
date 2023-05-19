package mongodataRepositories;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceRequestRepository;
import model.Request;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.RequestItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class RequestRepository implements InterfaceRequestRepository {
    private MongoDatabase mongoDatabase;

    public RequestRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createRequest() throws CoreException {
        try {
            int requestId = getNextId();
            
            var collection = mongoDatabase.getCollection(Collections.request.get());

            Document document = new Document();
            document.put(RequestItems.request_id.get(), requestId);
            document.put(RequestItems.client_from_id.get(), -999);
            document.put(RequestItems.client_to_id.get(), -999);
            document.put(RequestItems.message.get(), "");
            document.put(RequestItems.satisfied.get(), false);
            document.put(RequestItems.time_changed.get(), new Date());

            collection.insertOne(document);

            return requestId;
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Request getRequest(int requestId) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.request.get());
            Bson filter = eq(RequestItems.request_id.get(), requestId);
            Document doc = collection.find(filter).first();

            int userFromId = (int) doc.get(RequestItems.client_from_id.get());
            int userToId = (int) doc.get(RequestItems.client_to_id.get());
            String message = (String) doc.get(RequestItems.message.get());
            Boolean satisfied = (Boolean) doc.get(RequestItems.satisfied.get());
            Date changed = (Date) doc.get(RequestItems.time_changed.get());

            Request request = new Request(requestId, userFromId, userToId, message, satisfied, changed);

            return request;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Request[] getRequestsByTrainerId(int trainerId) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.request.get());
            Bson filter = and(
                eq(RequestItems.client_to_id.get(), trainerId),
                eq(RequestItems.satisfied.get(), false)
            );

            var docs = collection.find(filter);
            var requests = new ArrayList<Request>(){};

            for (Document doc : docs) {
                int requestId = (int) doc.get(RequestItems.request_id.get());
                int userFromId = (int) doc.get(RequestItems.client_from_id.get());
                String message = (String) doc.get(RequestItems.message.get());
                Boolean satisfied = (Boolean) doc.get(RequestItems.satisfied.get());
                Date changed = (Date) doc.get(RequestItems.time_changed.get());

                Request request = new Request(requestId, userFromId, trainerId, message, satisfied, changed);

                requests.add(request);
            }

            return requests.toArray(new Request[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Request[] getRequestsBySignedUserId(int userId) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.request.get());
            Bson filter = and(
                eq(RequestItems.client_from_id.get(), userId),
                eq(RequestItems.satisfied.get(), false)
            );

            var docs = collection.find(filter);
            var requests = new ArrayList<Request>(){};

            for (Document doc : docs) {
                int requestId = (int) doc.get(RequestItems.request_id.get());
                int userToId = (int) doc.get(RequestItems.client_to_id.get());
                String message = (String) doc.get(RequestItems.message.get());
                Boolean satisfied = (Boolean) doc.get(RequestItems.satisfied.get());
                Date changed = (Date) doc.get(RequestItems.time_changed.get());

                Request request = new Request(requestId, userId, userToId, message, satisfied, changed);

                requests.add(request);
            }

            return requests.toArray(new Request[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterRequest(Request request) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.request.get());
            Bson filter = eq(RequestItems.request_id.get(), request.getID());
            Bson update = combine(
                set(RequestItems.client_from_id.get(), request.getUserFromId()),
                set(RequestItems.client_to_id.get(), request.getUserToId()),
                set(RequestItems.message.get(), request.getMessage()),
                set(RequestItems.satisfied.get(), request.isSatisfied()),
                set(RequestItems.time_changed.get(), new Date())
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
    public void removeRequest(int requestId) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.request.get());
            Bson filter = eq(RequestItems.request_id.get(), requestId);

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
            var collection = mongoDatabase.getCollection(Collections.request.get());
    
            Document maxDoc = collection.find()
                    .sort(new Document(RequestItems.request_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxDoc == null) {
                return 1;
            }
    
            return (int) maxDoc.get(RequestItems.request_id.get()) + 1;
    
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }
    
}
