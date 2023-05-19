import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
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

import consoleAction.common.AlterUserAction;
import consoleAction.common.ExitAction;
import consoleAction.common.LogOutAction;
import consoleAction.common.ShowUserInfoAction;
import consoleAction.signed.CreateNewTrainingAction;
import consoleAction.signed.CreateRequestAction;
import consoleAction.signed.DeleteTrainingByIdAction;
import consoleAction.signed.GetAllUserTrainingsAction;
import consoleAction.signed.GetDoneUserTrainingsAction;
import consoleAction.signed.GetPlannedUserTrainingsAction;
import consoleAction.signed.GetTrainingByIdAction;
import consoleAction.signed.PerformePlannedAction;
import consoleAction.signed.RemoveRequestAction;
import consoleAction.signed.ShowWaitingRequestsAction;
import consoleAction.trainer.CreateTrainingFromTrainingPlanAction;
import consoleAction.trainer.CreateTrainingPlanAction;
import consoleAction.trainer.DeleteTrainingPlanAction;
import consoleAction.trainer.SatisfyRequestAction;
import consoleAction.trainer.ShowSingleTrainingPlanAction;
import consoleAction.trainer.ShowTrainerRequestsAction;
import consoleAction.trainer.ShowTrainingPlansAction;
import consoleAction.usigned.CreateUserAction;
import consoleAction.usigned.EnterUserIdAction;
import consoleMenu.MenuOption;
import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import interfaces.repositories.*;
import interfaces.services.*;
import pgdataMappers.*;
import services.*;

import org.postgresql.Driver;

@Configuration
@ComponentScan("org.malyshevin")
public class ConsoleConfiguration {
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

            String logPath = props.getProperty("CONSOLE_LOG_PATH");

            Logger logger = Logger.getLogger(ConsoleConfiguration.class.getName());

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
    NamedParameterJdbcTemplate jdbcTemplate() {
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
            return new pgdataRepositories.UserRepository(jdbcTemplate(), userMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.UserRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceTrainingPlanRepository trainingPlanRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.TrainingPlanRepository(jdbcTemplate(), trainingPlanMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.TrainingPlanRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceApproachPlanRepository approachPlanRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.ApproachPlanRepository(jdbcTemplate(), approachPlanMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.ApproachPlanRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceTrainingRepository trainingRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.TrainingRepository(jdbcTemplate(), trainingMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.TrainingRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceApproachRepository approachRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.ApproachRepository(jdbcTemplate(), approachMapper(), intMapper());
        }

        if (properties().get("DB_USE").equals("mongo")) {
            return new mongodataRepositories.ApproachRepository(mongoDatabase());
        }

        return null;
    }

    @Bean
    InterfaceRequestRepository requestRepository() {
        if (properties().get("DB_USE").equals("pg")) {
            return new pgdataRepositories.RequestRepository(jdbcTemplate(), requestMapper(), intMapper());
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

    // options

    @Bean
    Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    RuntimeProfile profile() {
        return new RuntimeProfile();
    }

    @Bean
    MenuOption exitOption() {
        InterfaceAction action = new ExitAction();

        return new MenuOption(action, "Выйти");
    }

    @Bean
    MenuOption enterIdOption() {
        InterfaceAction action = new EnterUserIdAction(scanner(), profile(), userService());

        return new MenuOption(action, "Ввести id");
    }

    @Bean
    MenuOption showUserInfoOption() {
        InterfaceAction action = new ShowUserInfoAction(profile(), userService());

        return new MenuOption(action, "Просмотр профиля");
    }

    @Bean
    MenuOption logOutOption() {
        InterfaceAction action = new LogOutAction(profile());

        return new MenuOption(action, "Выйти из профиля");
    }

    @Bean
    MenuOption alterUserOption() {
        InterfaceAction action = new AlterUserAction(scanner(), profile(), userService());

        return new MenuOption(action, "Изменить профиль");
    }

    @Bean
    MenuOption createUserOption() {
        InterfaceAction action = new CreateUserAction(userService(), profile());

        return new MenuOption(action, "Создать нового пользователя");
    }

    @Bean
    MenuOption createTrainingPlanOption() {
        InterfaceAction action = new CreateTrainingPlanAction(profile(), scanner(), trainingPlanService());

        return new MenuOption(action, "Создать план тренировки");
    }

    @Bean
    MenuOption showTrainingPlansOption() {
        InterfaceAction action = new ShowTrainingPlansAction(profile(), trainingPlanService());

        return new MenuOption(action, "Показать мои планы тренировок");
    }

    @Bean
    MenuOption showSingleTrainingOption() {
        InterfaceAction action = new ShowSingleTrainingPlanAction(profile(), scanner(), trainingPlanService());

        return new MenuOption(action, "Показать информацию о конкретной тренировке");
    }

    @Bean
    MenuOption deleteTrainingPlanOption() {
        InterfaceAction action = new DeleteTrainingPlanAction(profile(), scanner(), trainingPlanService());

        return new MenuOption(action, "Удалить план тренировки");
    }

    @Bean
    MenuOption createTrainingFromTrainingPlanOption() {
        InterfaceAction action = new CreateTrainingFromTrainingPlanAction(profile(), scanner(), trainingService());

        return new MenuOption(action, "Назначить тренировку");
    }

    @Bean
    MenuOption getTrainingByIdOption() {
        InterfaceAction action = new GetTrainingByIdAction(profile(), scanner(), trainingService());

        return new MenuOption(action, "Просмотреть конкретную тренировку");
    }

    @Bean
    MenuOption getAllUserTrainingsOption() {
        InterfaceAction action = new GetAllUserTrainingsAction(profile(), trainingService());

        return new MenuOption(action, "Просмотреть все тренировки пользователя");
    }

    @Bean
    MenuOption getDoneUserTrainingsOption() {
        InterfaceAction action = new GetDoneUserTrainingsAction(profile(), trainingService());

        return new MenuOption(action, "Просмотреть выполненные тренировки пользователя");
    }

    @Bean
    MenuOption getPlannedUserTrainingsOption() {
        InterfaceAction action = new GetPlannedUserTrainingsAction(profile(), trainingService());

        return new MenuOption(action, "Просмотреть запланированные тренировки пользователя");
    }

    @Bean
    MenuOption deleteTrainingByIdOption() {
        InterfaceAction action = new DeleteTrainingByIdAction(profile(), scanner(), trainingService());

        return new MenuOption(action, "Удалить тренировку");
    }

    @Bean
    MenuOption createNewTrainingOption() {
        InterfaceAction action = new CreateNewTrainingAction(profile(), scanner(), trainingService());

        return new MenuOption(action, "Создать новую тренировку");
    }

    @Bean
    MenuOption performePlannedOption() {
        InterfaceAction action = new PerformePlannedAction(profile(), scanner(), trainingService());

        return new MenuOption(action, "Выполнить существующую тренировку");
    }

    @Bean
    MenuOption showTrainerRequestsOption() {
        InterfaceAction action = new ShowTrainerRequestsAction(profile(), requestService());

        return new MenuOption(action, "Список запросов на получение тренировки");
    }

    @Bean
    MenuOption satisfyRequestOption() {
        InterfaceAction action = new SatisfyRequestAction(profile(), scanner(), trainingService(), trainingPlanService(), userService(), requestService());

        return new MenuOption(action, "Удовлитворить пользовательский запрос");
    }

    @Bean
    MenuOption createRequestOption() {
        InterfaceAction action = new CreateRequestAction(profile(), scanner(), userService(), requestService());

        return new MenuOption(action, "Запросить тренировку");
    }

    @Bean
    MenuOption showWaitingRequestsOption() {
        InterfaceAction action = new ShowWaitingRequestsAction(profile(), requestService());

        return new MenuOption(action, "Список мои запросов");
    }

    @Bean
    MenuOption removeRequestOption() {
        InterfaceAction action = new RemoveRequestAction(profile(), scanner(), requestService());

        return new MenuOption(action, "Удалить запрос");
    }
}
