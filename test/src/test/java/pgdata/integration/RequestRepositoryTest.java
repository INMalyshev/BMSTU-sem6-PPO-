package pgdata.integration;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import model.Request;
import pgdataMappers.IntMapper;
import pgdataMappers.RequestMapper;
import pgdataRepositories.RequestRepository;

public class RequestRepositoryTest {
    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int DEFAULT_INT = -999;
    private static final String DEFAULT_DATE = "2023.05.11";

    @Test
    public void createRequest_checkUserCountIncrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository userRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT + 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM request");
        if (rs.next()) { expectedCount = rs.getInt("count") + 1; }

        int result = userRepository.createRequest();

        rs = statement.executeQuery("SELECT COUNT(*) FROM request");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + result);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getRequestrByID_checkSatisfiedIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository requestRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO request (satisfied, time_changed) VALUES (true, '" + DEFAULT_DATE + "') RETURNING request_id");
        if (rs.next()) { id = rs.getInt("request_id"); }

        Request request = requestRepository.getRequest(id);

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + id);
        statement.close();
        connection.close();

        assertEquals(true, request.isSatisfied());
    }

    @Test
    public void getRequestsByTrainerId_AmountCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository requestRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int userId = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        int id = DEFAULT_INT;
        rs = statement.executeQuery("INSERT INTO request (satisfied, time_changed, client_to_id) VALUES (false, '" + DEFAULT_DATE + "', " + userId + ") RETURNING request_id");
        if (rs.next()) { id = rs.getInt("request_id"); }

        Request[] requests = requestRepository.getRequestsByTrainerId(userId);

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + id);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(1, requests.length);
    }

    @Test
    public void getRequestsBySignedUserId_AmountCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository requestRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int userId = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('', 2) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        int id = DEFAULT_INT;
        rs = statement.executeQuery("INSERT INTO request (satisfied, time_changed, client_from_id) VALUES (false, '" + DEFAULT_DATE + "', " + userId + ") RETURNING request_id");
        if (rs.next()) { id = rs.getInt("request_id"); }

        Request[] requests = requestRepository.getRequestsBySignedUserId(userId);

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + id);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(1, requests.length);
    }

    @Test
    public void alterRequestsById_changeSatisfiedCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository requestRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int userId = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('', 1) RETURNING client_id");
        if (rs.next()) { userId = rs.getInt("client_id"); }

        int id = DEFAULT_INT;
        rs = statement.executeQuery("INSERT INTO request (satisfied, time_changed, client_to_id) VALUES (false, '" + DEFAULT_DATE + "', " + userId + ") RETURNING request_id");
        if (rs.next()) { id = rs.getInt("request_id"); }

        Request request = requestRepository.getRequest(id);
        request.setSatisfied(true);

        requestRepository.alterRequest(request);

        Request result = requestRepository.getRequest(id);

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + id);
        statement.executeUpdate("DELETE FROM client WHERE client_id = " + userId);
        statement.close();
        connection.close();

        assertEquals(true, result.isSatisfied());
    }

    @Test
    public void removeRequestsById_changeSatisfiedCheck_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        RequestMapper requestMapper = ctx.getBean(RequestMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        RequestRepository requestRepository = new RequestRepository(jdbcTemplate, requestMapper, intMapper);

        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT - 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO request (satisfied, time_changed) VALUES (false, '" + DEFAULT_DATE + "') RETURNING request_id");
        if (rs.next()) { id = rs.getInt("request_id"); }

        rs = statement.executeQuery("SELECT COUNT(*) FROM request");
        if (rs.next()) { expectedCount = rs.getInt("count") - 1; }

        requestRepository.removeRequest(id);

        rs = statement.executeQuery("SELECT COUNT(*) FROM request");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM request WHERE request_id = " + id);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

}
