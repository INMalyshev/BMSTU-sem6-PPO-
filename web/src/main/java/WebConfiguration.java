import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import interfaces.repositories.*;
import interfaces.services.*;
import pgdataMappers.*;
import services.*;

import org.postgresql.Driver;

@Configuration
@ComponentScan("org.malyshevin.web")
public class WebConfiguration {
    private static final String configPath = "config.properties";
    
    @Bean
    Properties properties() {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(new File(configPath)));
        } catch (IOException e) {
            logger().log(Level.SEVERE, "cannot read .properties",  e);
            System.exit(1);
        }

        return props;
    }

    @Bean
    Logger logger() {

        try {
            Properties props = properties();

            String logPath = props.getProperty("LOG_PATH");

            Logger logger = Logger.getLogger(WebConfiguration.class.getName());

            FileHandler fh = new FileHandler(logPath, true);
            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            logger.log(Level.FINEST, "Logger created");

            return logger;
        } catch (IOException e) {
            System.exit(1);
        }

        return null;

    }

    // DataSource

    @Bean
    DataSource dataSource() {

        String url = "";
        String usr = "";
        String psw = "";

        Properties props = properties();

        url = props.getProperty("PG_DB_URL");
        usr = props.getProperty("PG_DB_USERNAME");
        psw = props.getProperty("PG_DB_PASSWORD");

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl(url);
        dataSource.setUsername(usr);
        dataSource.setPassword(psw);

        logger().log(Level.FINEST, "DataSource created");

        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            logger().log(Level.SEVERE, "connection error",  e);
            System.exit(1);
        }

        return dataSource;
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase("business");
    }

    // Mapper

    @Bean
    IntMapper intMapper() {
        return new IntMapper();
    } 

    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    TrainingPlanMapper trainingPlanMapper() {
        return new TrainingPlanMapper();
    }

    @Bean
    ApproachPlanMapper approachPlanMapper () {
        return new ApproachPlanMapper();
    }

    @Bean
    TrainingMapper trainingMapper() {
        return new TrainingMapper();
    }

    @Bean
    ApproachMapper approachMapper () {
        return new ApproachMapper();
    }

    @Bean
    RequestMapper requestMapper() {
        return new RequestMapper();
    }

    // Repository

    @Bean
    NamedParameterJdbcTemplate myJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    InterfaceExerciseTypeRepository exerciseTypeRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.ExerciseTypeRepository();
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.ExerciseTypeRepository();
        }

        return null;
    }

    @Bean
    InterfaceUserRepository userRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.UserRepository(myJdbcTemplate(), userMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.UserRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceTrainingPlanRepository trainingPlanRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.TrainingPlanRepository(myJdbcTemplate(), trainingPlanMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.TrainingPlanRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceApproachPlanRepository approachPlanRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.ApproachPlanRepository(myJdbcTemplate(), approachPlanMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.ApproachPlanRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceTrainingRepository trainingRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.TrainingRepository(myJdbcTemplate(), trainingMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.TrainingRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceApproachRepository approachRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.ApproachRepository(myJdbcTemplate(), approachMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.ApproachRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceRequestRepository requestRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.RequestRepository(myJdbcTemplate(), requestMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.RequestRepository(mongoDatabase());
        }

        return null;
    }

    // service

    @Bean
    InterfaceExerciseTypeService exerciseTypeService() {
        return new ExerciseTypeService(exerciseTypeRepository(), logger());
    }

    @Bean
    InterfaceUserService userService() {
        return new UserService(userRepository(), logger());
    }

    @Bean
    InterfaceTrainingPlanService trainingPlanService() {
        return new TrainingPlanService(trainingPlanRepository(), approachPlanRepository(), userService(), logger());
    }

    @Bean
    InterfaceTrainingService trainingService() {
        return new TrainingService(trainingRepository(), approachRepository(), trainingPlanService(), userService(), logger());
    }

    @Bean
    InterfaceRequestService requestService() {
        return new RequestService(requestRepository(), userService(), logger());
    }

}

