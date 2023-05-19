package mongodataRepositories;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.dao.DataAccessException;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceUserRepository;
import model.Role;
import model.User;
import mongodataRepositories.items.Collections;
import mongodataRepositories.items.RoleItems;
import mongodataRepositories.items.ClientItems;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;

public class UserRepository implements InterfaceUserRepository {
    private MongoDatabase mongoDatabase;

    public UserRepository(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public int createUser() throws CoreException {
        try {
            int clientId = getNextId();
            int roleId = getRoleIdByName(Role.SignedUser);

            var collection = mongoDatabase.getCollection(Collections.client.get());

            Document document = new Document();
            document.put(ClientItems.client_id.get(), clientId);
            document.put(ClientItems.role_id.get(), roleId);
            document.put(ClientItems.name.get(), "");

            collection.insertOne(document);

            return clientId;
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public User getUser(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.client.get());
            Bson filter = eq(ClientItems.client_id.get(), userID);
            Document doc = collection.find(filter).first();

            String name = (String) doc.get(ClientItems.name.get());
            Role role = getRoleById((int) doc.get(ClientItems.role_id.get()));

            User user = new User(name, role, userID);

            return user;

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterUser(User user) throws CoreException {
        try {
            int roleId = getRoleIdByName(user.getRole());

            var collection = mongoDatabase.getCollection(Collections.client.get());
            Bson filter = eq(ClientItems.client_id.get(), user.getID());
            Bson update = combine(
                set(ClientItems.name.get(), user.getName()),
                set(ClientItems.role_id.get(), roleId)
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
    public void removeUser(int userID) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.client.get());
            Bson filter = eq(ClientItems.client_id.get(), userID);
            DeleteResult result = collection.deleteMany(filter);

            if (result.getDeletedCount() < 1) {
                throw new DataLayerException();
            }

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public User[] getUsersByRole(Role userRole) throws CoreException {
        try {
            int roleId = getRoleIdByName(userRole);

            var collection = mongoDatabase.getCollection(Collections.client.get());
            Bson filter = eq(ClientItems.role_id.get(), roleId);

            var docs = collection.find(filter);
            var users = new ArrayList<User>(){};

            for (Document doc : docs) {
                int userID = (int) doc.get(ClientItems.client_id.get());
                String name = (String) doc.get(ClientItems.name.get());
                Role role = getRoleById((int) doc.get(ClientItems.role_id.get()));

                User user = new User(name, role, userID);

                users.add(user);
            }

            return users.toArray(new User[]{});

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private int getNextId() throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.client.get());
    
            Document maxClient = collection.find()
                    .sort(new Document(ClientItems.client_id.get(), -1))
                    .limit(1)
                    .first();
    
            if (maxClient == null) {
                return 1;
            }
    
            return (int) maxClient.get(ClientItems.client_id.get()) + 1;
    
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private int getRoleIdByName(Role role) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.role.get());
            Document doc = collection.find(new Document(RoleItems.name.get(), role.getTitle())).first();
    
            return (int) doc.get(RoleItems.role_id.get());

        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private Role getRoleById(int id) throws CoreException {
        try {
            var collection = mongoDatabase.getCollection(Collections.role.get());
            Bson filter = eq(RoleItems.role_id.get(), id);
            Document doc = collection.find(filter).first();

            String name = (String) doc.get(RoleItems.name.get());
            Role role = getRoleByName(name);

            return role;
        } catch (Exception e) {
            throw new DataLayerException(e);
        }
    }

    private Role getRoleByName(String name) throws CoreException {
        Role[] roles = Role.values();

        for (Role role : roles) {
            if (name.equals(role.getTitle())) {
                return role;
            }
        }

        throw new DataLayerException();
    }
    
}
