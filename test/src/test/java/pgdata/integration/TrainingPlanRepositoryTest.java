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

import model.TrainingPlan;
import pgdataMappers.IntMapper;
import pgdataMappers.TrainingPlanMapper;
import pgdataRepositories.TrainingPlanRepository;

public class TrainingPlanRepositoryTest {
    
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int    DEFAULT_INT     = -999;
    private static final String DEFAULT_STRING  = "default";

    @Test
    public void createTrainingPlan_checkCountIncrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingPlanMapper trainingPlanMapper = ctx.getBean(TrainingPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingPlanRepository trainingPlanRepository = new TrainingPlanRepository(jdbcTemplate, trainingPlanMapper, intMapper);
     
        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT + 1;
        int userId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM training_plan");
        if (rs.next()) { expectedCount = rs.getInt("count") + 1; }

        rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }
        int trainingPlanId = trainingPlanRepository.createTrainingPlan(userId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM training_plan");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getTrainingPlanByID_checkCreatorUserIdIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingPlanMapper trainingPlanMapper = ctx.getBean(TrainingPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingPlanRepository trainingPlanRepository = new TrainingPlanRepository(jdbcTemplate, trainingPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanByID(trainingPlanId);

        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(userId, trainingPlan.getCreatorUserID());
    }

    @Test
    public void alterTrainingPlan_checkCreatorUserIdChenge_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingPlanMapper trainingPlanMapper = ctx.getBean(TrainingPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingPlanRepository trainingPlanRepository = new TrainingPlanRepository(jdbcTemplate, trainingPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int otherUserId = DEFAULT_INT;
        int alteredUserId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { otherUserId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        TrainingPlan alteredTrainingPlan = new TrainingPlan(trainingPlanId, otherUserId);
        trainingPlanRepository.alterTrainingPlan(alteredTrainingPlan);

        rs = statement.executeQuery("SELECT creator_user_id FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        if (rs.next()) { alteredUserId = rs.getInt("creator_user_id"); }

        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + otherUserId);
        statement.close();
        connection.close();

        assertEquals(otherUserId, alteredUserId);
    }

    @Test
    public void removeTrainingPlan_checkAmountDecrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingPlanMapper trainingPlanMapper = ctx.getBean(TrainingPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingPlanRepository trainingPlanRepository = new TrainingPlanRepository(jdbcTemplate, trainingPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM training_plan");
        if (rs.next()) { expectedCount = rs.getInt("count") - 1; }

        trainingPlanRepository.removeTrainingPlan(trainingPlanId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM training_plan");
        if (rs.next()) { count = rs.getInt("count"); }
        
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getTrainingPlanByUserID_checkResultSizeAndCreatorUser_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        TrainingPlanMapper trainingPlanMapper = ctx.getBean(TrainingPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        TrainingPlanRepository trainingPlanRepository = new TrainingPlanRepository(jdbcTemplate, trainingPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int otherTrainingPlanId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { otherTrainingPlanId = rs.getInt("training_plan_id"); }

        TrainingPlan[] trainingPlans = trainingPlanRepository.getTrainingPlanByUserID(userId);

        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + otherTrainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        for (TrainingPlan tp : trainingPlans) {
            assertEquals(userId, tp.getCreatorUserID());
        }

        assertEquals(2, trainingPlans.length);
    }

}
