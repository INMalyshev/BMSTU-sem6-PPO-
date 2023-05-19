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

import model.Role;
import model.User;

import pgdataMappers.IntMapper;
import pgdataMappers.UserMapper;

import pgdataRepositories.UserRepository;

public class UserRepositoryTest {

    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(PgdataIntegrationTestConfiguration.class);

    private static final int    DEFAULT_INT     = -999;
    private static final String DEFAULT_STRING  = "default";

    @Test
    public void createUser_checkUserCountIncrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        UserRepository userRepository = new UserRepository(jdbcTemplate, userMapper, intMapper);

        int count = DEFAULT_INT;
        int expectedCount = DEFAULT_INT + 1;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM client");
        if (rs.next()) { expectedCount = rs.getInt("count") + 1; }

        int result = userRepository.createUser();

        rs = statement.executeQuery("SELECT COUNT(*) FROM client");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.executeUpdate("DELETE FROM client WHERE client_id = " + result);
        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getUserByID_checkNamesAndRolesIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        UserRepository userRepository = new UserRepository(jdbcTemplate, userMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { id = rs.getInt("client_id"); }

        User user = userRepository.getUser(id);

        statement.executeUpdate("DELETE FROM client WHERE client_id = " + id);
        statement.close();
        connection.close();

        assertEquals(DEFAULT_STRING, user.getName());
        assertEquals(Role.Trainer, user.getRole());
    }

    @Test
    public void alterUser_checkNamesAndRolesIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        UserRepository userRepository = new UserRepository(jdbcTemplate, userMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { id = rs.getInt("client_id"); }

        User updatedUser = new User("Ilya", Role.SignedUser, id);
        userRepository.alterUser(updatedUser);

        String name = null;
        String roleName = null;
        rs = statement.executeQuery("select client.name as client_name, role.name as role_name from client join role using(role_id) where client_id = " + id);
        if (rs.next()) {
            name = rs.getString("client_name");
            roleName = rs.getString("role_name");
        }

        statement.executeUpdate("DELETE FROM client WHERE client_id = " + id);
        statement.close();
        connection.close();

        assertEquals(updatedUser.getName(), name);
        assertEquals(updatedUser.getRole().getTitle(), roleName);
    }

    @Test
    public void removeUser_checkUserCountDecrement_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        UserRepository userRepository = new UserRepository(jdbcTemplate, userMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { id = rs.getInt("client_id"); }

        int expectedCount = DEFAULT_INT;
        rs = statement.executeQuery("SELECT COUNT(*) FROM client");
        if (rs.next()) { expectedCount = rs.getInt("count") - 1; }

        userRepository.removeUser(id);

        int count = DEFAULT_INT;
        rs = statement.executeQuery("SELECT COUNT(*) FROM client");
        if (rs.next()) { count = rs.getInt("count"); }

        statement.close();
        connection.close();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getSignedUsers_checkRolesIdentity_Test() throws Exception {
        DataSource dataSource = ctx.getBean(DataSource.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        IntMapper intMapper = ctx.getBean(IntMapper.class);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        UserRepository userRepository = new UserRepository(jdbcTemplate, userMapper, intMapper);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        int id = DEFAULT_INT;
        ResultSet rs = statement.executeQuery("INSERT INTO client (name, role_id) VALUES ('" + DEFAULT_STRING + "', 1) RETURNING client_id");
        if (rs.next()) { id = rs.getInt("client_id"); }

        User[] users = userRepository.getUsersByRole(Role.Trainer);

        statement.executeUpdate("DELETE FROM client WHERE client_id = " + id);
        statement.close();
        connection.close();

        assertTrue(users.length > 0);
        for (User user : users) {
            assertEquals(Role.Trainer, user.getRole());
        }
    }

}
