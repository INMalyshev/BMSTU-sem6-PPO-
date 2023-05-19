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

import model.Approach;
import model.ExerciseType;
import pgdataMappers.ApproachMapper;
import pgdataMappers.IntMapper;
import pgdataRepositories.ApproachRepository;

public class ApproachRepositoryTest {

    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int    DEFAULT_INT     = -999;
    private static final String DEFAULT_STRING  = "default";

    @Test
    public void createApproach_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachMapper approachMapper = ctx.getBean(ApproachMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachRepository approachRepository = new ApproachRepository(jdbcTemplate, approachMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;

        int countStart = DEFAULT_INT;
        int countEnd = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach");
        if (rs.next()) { countStart = rs.getInt("count"); }

        int approachId = approachRepository.createApproach(trainingId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach");
        if (rs.next()) { countEnd = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + approachId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(countStart + 1, countEnd);
    }

    @Test
    public void getApproachByID_checkTrainingId_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachMapper approachMapper = ctx.getBean(ApproachMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachRepository approachRepository = new ApproachRepository(jdbcTemplate, approachMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int approachId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO approach (amount, expected_amount, training_id, excersize_type_id, completed) VALUES (0, 0, " + trainingId + ", 1, false) RETURNING approach_id");
        if (rs.next()) { approachId = rs.getInt("approach_id"); }

        Approach approach = approachRepository.getApproachByID(approachId);

        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + approachId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(trainingId, approach.GetTrainingID());
    }

    @Test
    public void getApproachByTrainingID_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachMapper approachMapper = ctx.getBean(ApproachMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachRepository approachRepository = new ApproachRepository(jdbcTemplate, approachMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int approachId = DEFAULT_INT;
        int otherApproachId = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO approach (amount, expected_amount, training_id, excersize_type_id, completed) VALUES (0, 0, " + trainingId + ", 1, false) RETURNING approach_id");
        if (rs.next()) { approachId = rs.getInt("approach_id"); }

        rs = statement.executeQuery("INSERT INTO approach (amount, expected_amount, training_id, excersize_type_id, completed) VALUES (0, 0, " + trainingId + ", 1, false) RETURNING approach_id");
        if (rs.next()) { otherApproachId = rs.getInt("approach_id"); }

        Approach[] approachs = approachRepository.getApproachByTrainingID(trainingId);

        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + otherApproachId);
        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + approachId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(2, approachs.length);
    }

    @Test
    public void alterApproach_checkTrainingId_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachMapper approachMapper = ctx.getBean(ApproachMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachRepository approachRepository = new ApproachRepository(jdbcTemplate, approachMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int approachId = DEFAULT_INT;

        int newExpectedAmount = DEFAULT_INT;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO approach (amount, expected_amount, training_id, excersize_type_id, completed) VALUES (0, 0, " + trainingId + ", 1, false) RETURNING approach_id");
        if (rs.next()) { approachId = rs.getInt("approach_id"); }

        Approach approach = new Approach(approachId, trainingId, 123, newExpectedAmount, ExerciseType.PullUp);
        approachRepository.alterApproach(approach);

        rs = statement.executeQuery("SELECT expected_amount FROM approach WHERE approach_id = " + approachId);
        if (rs.next()) { newExpectedAmount = rs.getInt("expected_amount"); }

        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + approachId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(approach.getExpectedAmount(), newExpectedAmount);
    }

    @Test
    public void removeApproach_countCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);
        ApproachMapper approachMapper = ctx.getBean(ApproachMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ApproachRepository approachRepository = new ApproachRepository(jdbcTemplate, approachMapper, intMapper);

        int userId = DEFAULT_INT;
        int trainingId = DEFAULT_INT;
        int approachId = DEFAULT_INT;

        int countStart = DEFAULT_INT;
        int countEnd = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        rs = statement.executeQuery("INSERT INTO training (holder_user_id, completed) VALUES (" + userId + ", false) RETURNING training_id");
        if (rs.next()) { trainingId = rs.getInt("training_id"); }

        rs = statement.executeQuery("INSERT INTO approach (amount, expected_amount, training_id, excersize_type_id, completed) VALUES (0, 0, " + trainingId + ", 1, false) RETURNING approach_id");
        if (rs.next()) { approachId = rs.getInt("approach_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach");
        if (rs.next()) { countStart = rs.getInt("count"); }

        approachRepository.removeApproach(approachId);

        rs = statement.executeQuery("SELECT COUNT(*) FROM approach");
        if (rs.next()) { countEnd = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM approach WHERE approach_id = " + approachId);
        statement.executeUpdate("DELETE FROM training WHERE training_id = " + trainingId);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(countStart - 1, countEnd);
    }
    
}
