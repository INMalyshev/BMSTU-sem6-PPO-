package mongodata.integration;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mongodb.client.MongoDatabase;

import interfaces.repositories.InterfaceRequestRepository;
import model.Request;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.RequestItems;

import java.util.Date;

public class RequestRepositoryTest {
    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(MongodataIntegrationTestConfiguration.class);
 
    @Test
    public void createRequestTest() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);

        var collection = mongoDatabase.getCollection(Collections.request.get());
        long cntRowsBefore = collection.countDocuments();

        requestRepository.createRequest();

        long cntRowsAfter = collection.countDocuments();

        cleanAllTables(mongoDatabase);
        org.junit.Assert.assertEquals(cntRowsBefore + 1, cntRowsAfter);
    }

    @Test
    public void getRequestTest() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);

        var collection = mongoDatabase.getCollection(Collections.request.get());

        Date date = new Date();

        Document doc = new Document();
        doc.put(RequestItems.request_id.get(), 1);
        doc.put(RequestItems.client_from_id.get(), -999);
        doc.put(RequestItems.client_to_id.get(), -999);
        doc.put(RequestItems.message.get(), "");
        doc.put(RequestItems.satisfied.get(), false);
        doc.put(RequestItems.time_changed.get(), date);

        collection.insertOne(doc);

        Request request = requestRepository.getRequest(1);

        cleanAllTables(mongoDatabase);

        org.junit.Assert.assertEquals(date, request.getChanged());
    }

    @Test
    public void getRequestTrainerId() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);

        var collection = mongoDatabase.getCollection(Collections.request.get());

        Date date = new Date();

        Document doc = new Document();
        doc.put(RequestItems.request_id.get(), 1);
        doc.put(RequestItems.client_from_id.get(), -999);
        doc.put(RequestItems.client_to_id.get(), 1);
        doc.put(RequestItems.message.get(), "");
        doc.put(RequestItems.satisfied.get(), false);
        doc.put(RequestItems.time_changed.get(), date);

        collection.insertOne(doc);

        Request[] requests = requestRepository.getRequestsByTrainerId(1);

        cleanAllTables(mongoDatabase);

        org.junit.Assert.assertEquals(1, requests.length);
    }

    @Test
    public void getRequestBySignedUserId() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);

        var collection = mongoDatabase.getCollection(Collections.request.get());

        Date date = new Date();

        Document doc = new Document();
        doc.put(RequestItems.request_id.get(), 1);
        doc.put(RequestItems.client_from_id.get(), 1);
        doc.put(RequestItems.client_to_id.get(), -999);
        doc.put(RequestItems.message.get(), "");
        doc.put(RequestItems.satisfied.get(), false);
        doc.put(RequestItems.time_changed.get(), date);

        collection.insertOne(doc);

        Request[] requests = requestRepository.getRequestsBySignedUserId(1);

        cleanAllTables(mongoDatabase);

        org.junit.Assert.assertEquals(1, requests.length);
    }

    @Test
    public void removeRequestTest() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);

        var collection = mongoDatabase.getCollection(Collections.request.get());

        Date date = new Date();

        Document doc = new Document();
        doc.put(RequestItems.request_id.get(), 1);
        doc.put(RequestItems.client_from_id.get(), -999);
        doc.put(RequestItems.client_to_id.get(), -999);
        doc.put(RequestItems.message.get(), "");
        doc.put(RequestItems.satisfied.get(), false);
        doc.put(RequestItems.time_changed.get(), date);

        collection.insertOne(doc);

        long cntRowsBefore = collection.countDocuments();
        requestRepository.removeRequest(1);
        long cntRowsAfter = collection.countDocuments();

        cleanAllTables(mongoDatabase);

        org.junit.Assert.assertEquals(cntRowsBefore, cntRowsAfter + 1);
    }

    private void cleanAllTables(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection(Collections.request.get());
        collection.deleteMany(new Document());
    }
}
