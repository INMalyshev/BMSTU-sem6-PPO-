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

import model.ApproachPlan;
import model.ExerciseType;
import pgdataMappers.ApproachPlanMapper;
import pgdataMappers.IntMapper;
import pgdataRepositories.ApproachPlanRepository;

public class ApproachPlanRepositoryTest {
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int    DEFAULT_INT     = -999;
    private static final String DEFAULT_STRING  = "default";

    @Test
    public void createApproachPlan_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachPlanMapper approachPlanMapper = ctx.getBean(ApproachPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachPlanRepository approachPlanRepository = new ApproachPlanRepository(jdbcTemplate, approachPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;

        int countStart = DEFAULT_INT;
        int countEnd = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach_plan");
        if (rs.next()) { countStart = rs.getInt("count"); }

        int approachPlanId = approachPlanRepository.createApproachPlan(trainingPlanId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach_plan");
        if (rs.next()) { countEnd = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(countStart + 1, countEnd);
    }

    @Test
    public void getApproachPlanByID_checkTrainingId_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachPlanMapper approachPlanMapper = ctx.getBean(ApproachPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachPlanRepository approachPlanRepository = new ApproachPlanRepository(jdbcTemplate, approachPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int approachPlanId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id) VALUES (0, " + trainingPlanId + ", 1) RETURNING approach_plan_id");
        if (rs.next()) { approachPlanId = rs.getInt("approach_plan_id"); }

        ApproachPlan approachPlan = approachPlanRepository.getApproachPlanByID(approachPlanId);

        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(trainingPlanId, approachPlan.getTrainingPlanID());
    }

    @Test
    public void getApproachPlanByTrainingPlanID_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachPlanMapper approachPlanMapper = ctx.getBean(ApproachPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachPlanRepository approachPlanRepository = new ApproachPlanRepository(jdbcTemplate, approachPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int approachPlanId = DEFAULT_INT;
        int otherApproachPlanId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id) VALUES (0, " + trainingPlanId + ", 1) RETURNING approach_plan_id");
        if (rs.next()) { approachPlanId = rs.getInt("approach_plan_id"); }

        rs = statement.executeQuery("INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id) VALUES (0, " + trainingPlanId + ", 1) RETURNING approach_plan_id");
        if (rs.next()) { otherApproachPlanId = rs.getInt("approach_plan_id"); }

        ApproachPlan[] approachPlans = approachPlanRepository.getApproachPlanByTrainingPlanID(trainingPlanId);

        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + otherApproachPlanId);
        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(2, approachPlans.length);
    }

    @Test
    public void alterApproachPlan_checkTrainingId_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachPlanMapper approachPlanMapper = ctx.getBean(ApproachPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachPlanRepository approachPlanRepository = new ApproachPlanRepository(jdbcTemplate, approachPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int approachPlanId = DEFAULT_INT;

        int newExpectedAmount = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id) VALUES (0, " + trainingPlanId + ", 1) RETURNING approach_plan_id");
        if (rs.next()) { approachPlanId = rs.getInt("approach_plan_id"); }

        ApproachPlan approachPlan = new ApproachPlan(trainingPlanId, ExerciseType.PullUp, 123, approachPlanId);
        approachPlanRepository.alterApproachPlan(approachPlan);

        rs = statement.executeQuery("SELECT expected_amount FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        if (rs.next()) { newExpectedAmount = rs.getInt("expected_amount"); }

        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(approachPlan.getExpectedAmount(), newExpectedAmount);
    }

    @Test
    public void removeApproachPlan_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachPlanMapper approachPlanMapper = ctx.getBean(ApproachPlanMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachPlanRepository approachPlanRepository = new ApproachPlanRepository(jdbcTemplate, approachPlanMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingPlanId = DEFAULT_INT;
        int approachPlanId = DEFAULT_INT;

        int countStart = DEFAULT_INT;
        int countEnd = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training_plan (creator_user_id) VALUES (" + userId + ") RETURNING training_plan_id");
        if (rs.next()) { trainingPlanId = rs.getInt("training_plan_id"); }

        rs = statement.executeQuery("INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id) VALUES (0, " + trainingPlanId + ", 1) RETURNING approach_plan_id");
        if (rs.next()) { approachPlanId = rs.getInt("approach_plan_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach_plan");
        if (rs.next()) { countStart = rs.getInt("count"); }

        approachPlanRepository.removeApproachPlan(approachPlanId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach_plan");
        if (rs.next()) { countEnd = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM approach_plan WHERE approach_plan_id = " + approachPlanId);
        statement.executeUpdate("DELETE FROM training_plan WHERE training_plan_id = " + trainingPlanId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(countStart - 1, countEnd);
    }
    
}
