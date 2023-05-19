package pgdata.integration;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import model.Training;
import pgdataMappers.IntMapper;
import pgdataMappers.TrainingMapper;
import pgdataRepositories.TrainingRepository;

public class TrainingRepositoryTest {
    
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int    DEFAULT_INT     = -999;
    private static final String DEFAULT_STRING  = "default";

    @Test
    public void createTraining_checkCountIncrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);
     
        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT + 1;
        int userId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM training");
        if (rs.next()) { expectedCount = rs.getInt("count") + 1; }

        rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }
        int trainingId = trainingRepository.createTraining(userId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM training");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getTrainingByID_checkCreatorUserIdIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        Training training = trainingRepository.getTrainingByID(trainingId);

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(userId, training.GetHolderUserID());
    }

    @Test
    public void alterTraining_checkCreatorUserIdChenge_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int otherUserId = DEFAULT_INT;
        int alteredUserId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { otherUserId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        Training alteredTraining = new Training(trainingId, otherUserId);
        trainingRepository.alterTraining(alteredTraining);

        rs = statement.executeQuery("SELECT holder_user_id FROM training WHERE training_id = " + trainingId);
        if (rs.next()) { alteredUserId = rs.getInt("holder_user_id"); }

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + otherUserId);
        statement.close();
        connection.close();

        assertEquals(otherUserId, alteredUserId);
    }

    @Test
    public void removeTraining_checkAmountDecrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM training");
        if (rs.next()) { expectedCount = rs.getInt("count") - 1; }

        trainingRepository.removeTraining(trainingId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM training");
        if (rs.next()) { count = rs.getInt("count"); }
        
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getTrainingByUserID_checkResultSizeAndCreatorUser_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int otherTrainingId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { otherTrainingId = rs.getInt("training_id"); }

        Training[] trainings = trainingRepository.getTrainingsByUserID(userId);

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + otherTrainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        for (Training tp : trainings) {
            assertEquals(userId, tp.GetHolderUserID());
        }

        assertEquals(2, trainings.length);
    }

    @Test
    public void getDoneTrainingByUserID_checkResultSizeAndCreatorUser_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int otherTrainingId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", true) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", true) RETURNING training_id");
        if (rs.next()) { otherTrainingId = rs.getInt("training_id"); }

        Training[] trainings = trainingRepository.getDoneTrainingsByUserID(userId);

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + otherTrainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        for (Training tp : trainings) {
            assertEquals(userId, tp.GetHolderUserID());
        }

        assertEquals(2, trainings.length);
    }

    @Test
    public void getPlannedTrainingByUserID_checkResultSizeAndCreatorUser_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingMapper trainingMapper = ctx.getBean(TrainingMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingRepository trainingRepository = new TrainingRepository(jdbcTemplate, trainingMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int otherTrainingId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { otherTrainingId = rs.getInt("training_id"); }

        Training[] trainings = trainingRepository.getPlannedTrainingsByUserID(userId);

        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + otherTrainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        for (Training tp : trainings) {
            assertEquals(userId, tp.GetHolderUserID());
        }

        assertEquals(2, trainings.length);
    }
}
