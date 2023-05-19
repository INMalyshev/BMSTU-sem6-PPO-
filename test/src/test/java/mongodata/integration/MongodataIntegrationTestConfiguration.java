package mongodata.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import interfaces.repositories.InterfaceApproachPlanRepository;
import interfaces.repositories.InterfaceRequestRepository;
import interfaces.repositories.InterfaceUserRepository;
import mongodataRepositories.ApproachPlanRepository;
import mongodataRepositories.RequestRepository;
import mongodataRepositories.UserRepository;


@Configuration
@EnableMongoRepositories(basePackages = "com.example.repository")
public class MongodataIntegrationTestConfiguration {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase("business_mongo");
    }

    @Bean
    InterfaceUserRepository userRepository() {
        return new UserRepository(mongoDatabase());
    }

    @Bean
    InterfaceRequestRepository requestRepository() {
        return new RequestRepository(mongoDatabase());
    }

    @Bean
    InterfaceApproachPlanRepository approachPlanRepository() {
        return new ApproachPlanRepository(mongoDatabase());
    }
}
