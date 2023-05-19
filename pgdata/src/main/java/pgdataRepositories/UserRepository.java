package pgdataRepositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceUserRepository;
import pgdataMappers.IntMapper;
import pgdataMappers.UserMapper;
import model.User;
import model.Role;

public class UserRepository implements InterfaceUserRepository {

    private static final String DEFAULT_STRING = "default";

    private static final String SQL_CREATE_USER = """
        INSERT INTO client (name, role_id) 
        VALUES (:name, :role_id) 
        RETURNING client_id
    """;

    private static final String SQL_GET_USER_BY_ID = """
        SELECT 
            client.name as client_name,
            role.name as role_name,
            client_id
        FROM 
            client
            JOIN role USING(role_id)
        WHERE 
            client_id = :client_id
    """;

    private static final String SQL_ALTER_USER = """
        UPDATE 
            client 
        SET 
            name = :name, 
            role_id = :role_id 
        WHERE 
            client_id = :client_id
    """;

    private static final String SQL_REMOVE_USER_BY_ID = """
        DELETE FROM 
            client 
        WHERE 
            client_id = :client_id
    """;

    private static final String SQL_GET_ROLE_ID_BY_NAME = """
        SELECT 
            role_id 
        FROM 
            role 
        WHERE 
            name = :name
    """;

    private static final String SQL_GET_USERS_BY_ROLE_NAME = """
        SELECT 
            client.name as client_name,
            role.name as role_name,
            client_id
        FROM 
            client
            JOIN role USING(role_id)
        WHERE 
            role.name = :roleName
        ORDER BY
            client_id ASC
    """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final IntMapper intMapper;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserMapper userMapper, IntMapper intMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
        this.intMapper = intMapper;

    }

    private int getRoleIdByName(Role role) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", role.getTitle());

        try {
            Optional<Integer> userId =  jdbcTemplate.query(SQL_GET_ROLE_ID_BY_NAME, params, intMapper).stream().findFirst();

            if (userId.isPresent()) {
                return userId.get();
            }
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public int createUser() throws CoreException {

        int role_id = this.getRoleIdByName(Role.SignedUser);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", DEFAULT_STRING);
        params.addValue("role_id", role_id);


        try {
            Optional<Integer> userId =  jdbcTemplate.query(SQL_CREATE_USER, params, intMapper).stream().findFirst();

            if (userId.isPresent()) {
                return userId.get();
            }
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public User getUser(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_id", userID);

        try {
            Optional<User> user =  jdbcTemplate.query(SQL_GET_USER_BY_ID, params, userMapper).stream().findFirst();

            if (user.isPresent()) {
                return user.get();
            }
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public void alterUser(User user) throws CoreException {

        int role_id = this.getRoleIdByName(user.getRole());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_id", user.getID());
        params.addValue("name", user.getName());
        params.addValue("role_id", role_id);

        try {
            jdbcTemplate.update(SQL_ALTER_USER, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeUser(int userID) throws CoreException {
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_id", userID);

        try {
            jdbcTemplate.update(SQL_REMOVE_USER_BY_ID, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public User[] getUsersByRole(Role userRole) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleName", userRole.getTitle());

        try {
            User[] users = jdbcTemplate.query(SQL_GET_USERS_BY_ROLE_NAME, params, userMapper).toArray(new User[]{});

            return users;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }
    
}
