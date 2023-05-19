package mongodata.integration;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mongodb.client.MongoDatabase;

import interfaces.repositories.InterfaceUserRepository;
import model.Role;
import model.User;
import mongodataRepositories.items.ClientItems;
import mongodataRepositories.items.Collections;

import static com.mongodb.client.model.Filters.*;

public class UserRepositoryTest {
    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(MongodataIntegrationTestConfiguration.class);

    @Test
    public void createUserTest() throws Exception {
        MongoDatabase mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);

        var collection = mongoDatabase.getCollection(Collections.client.get());
        long cntRowsBefore = collection.countDocuments();

        userRepository.createUser();

        long cntRowsAfter = collection.countDocuments();

        cleanAllUserTables(mongoDatabase);
        org.junit.Assert.assertEquals(cntRowsBefore + 1, cntRowsAfter);
    }

    @Test
    public void alterUserTest() throws Exception {
        var mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);

        var collection = mongoDatabase.getCollection(Collections.client.get());
        Document doc = new Document();
        doc.put(ClientItems.client_id.get(), 1);
        doc.put(ClientItems.name.get(), "name");
        doc.put(ClientItems.role_id.get(), 1);
        collection.insertOne(doc);

        User user = new User("name1", Role.SignedUser, 1);
        userRepository.alterUser(user);

        Bson filter = eq(ClientItems.client_id.get(), 1);
        Document result = collection.find(filter).first();

        cleanAllUserTables(mongoDatabase);

        org.junit.Assert.assertNotEquals(result, null);
        org.junit.Assert.assertEquals("name1", (String) result.get(ClientItems.name.get()));
        org.junit.Assert.assertEquals(2, (int) result.get(ClientItems.role_id.get()));
    }

    @Test
    public void getUserTest() throws Exception {
        var mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);

        var collection = mongoDatabase.getCollection(Collections.client.get());
        Document doc = new Document();
        doc.put(ClientItems.client_id.get(), 1);
        doc.put(ClientItems.name.get(), "name");
        doc.put(ClientItems.role_id.get(), 1);
        collection.insertOne(doc);

        User user = userRepository.getUser(1);

        cleanAllUserTables(mongoDatabase);

        org.junit.Assert.assertEquals("name", user.getName());
        org.junit.Assert.assertEquals(1, user.getID());
        org.junit.Assert.assertEquals(Role.Trainer, user.getRole());
    }

    @Test
    public void getUserByRoleTest() throws Exception {
        var mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);

        var collection = mongoDatabase.getCollection(Collections.client.get());

        Document doc1 = new Document();
        doc1.put(ClientItems.client_id.get(), 1);
        doc1.put(ClientItems.name.get(), "name1");
        doc1.put(ClientItems.role_id.get(), 1);

        Document doc2 = new Document();
        doc2.put(ClientItems.client_id.get(), 2);
        doc2.put(ClientItems.name.get(), "name2");
        doc2.put(ClientItems.role_id.get(), 1);

        collection.insertOne(doc1);
        collection.insertOne(doc2);

        User[] users = userRepository.getUsersByRole(Role.Trainer);

        cleanAllUserTables(mongoDatabase);

        org.junit.Assert.assertEquals(2, users.length);
    }

    @Test
    public void deleteUserTest() throws Exception {
        var mongoDatabase = ctx.getBean(MongoDatabase.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);

        var collection = mongoDatabase.getCollection(Collections.client.get());
        Document doc = new Document();
        doc.put(ClientItems.client_id.get(), 1);
        doc.put(ClientItems.name.get(), "name");
        doc.put(ClientItems.role_id.get(), 1);
        collection.insertOne(doc);

        long cntRowsBefore = collection.countDocuments();
        userRepository.removeUser(1);
        long cntRowsAfter = collection.countDocuments();

        cleanAllUserTables(mongoDatabase);

        org.junit.Assert.assertEquals(cntRowsBefore, cntRowsAfter + 1);
    }

    private void cleanAllUserTables(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection(Collections.client.get());
        collection.deleteMany(new Document());
    }
}
