package pgdataRepositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceRequestRepository;
import model.Request;
import pgdataMappers.IntMapper;
import pgdataMappers.RequestMapper;

public class RequestRepository implements InterfaceRequestRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RequestMapper requestMapper;
    private final IntMapper intMapper;

    public RequestRepository(NamedParameterJdbcTemplate jdbcTemplate, RequestMapper requestMapper,
            IntMapper intMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.requestMapper = requestMapper;
        this.intMapper = intMapper;
    }

    @Override
    public int createRequest() throws CoreException {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_from_id", null);
        params.addValue("client_to_id", null);
        params.addValue("message", null);
        params.addValue("satisfied", false);
        params.addValue("time_changed", new Date());

        try {
            Optional<Integer> requestId =  jdbcTemplate.query(SQL_CREATE_REQUEST, params, intMapper).stream().findFirst();

            if (requestId.isPresent()) {
                return requestId.get();
            }
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public Request getRequest(int requestId) throws CoreException {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("request_id", requestId);

        try {
            Optional<Request> request =  jdbcTemplate.query(SQL_GET_REQUEST_BY_ID, params, requestMapper).stream().findFirst();

            if (request.isPresent()) {
                return request.get();
            }
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();

    }

    @Override
    public Request[] getRequestsByTrainerId(int trainerId) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_to_id", trainerId);

        try {
            return jdbcTemplate.query(SQL_GET_REQUESTS_BY_TRAINER_ID, params, requestMapper).toArray(new Request[]{});
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Request[] getRequestsBySignedUserId(int userId) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("client_from_id", userId);

        try {
            return jdbcTemplate.query(SQL_GET_REQUESTS_BY_SIGNED_USER_ID, params, requestMapper).toArray(new Request[]{});
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterRequest(Request request) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("request_id", request.getID());
        params.addValue("client_from_id", request.getUserFromId());
        params.addValue("client_to_id", request.getUserToId());
        params.addValue("message", request.getMessage());
        params.addValue("satisfied", request.isSatisfied());
        params.addValue("time_changed", new Date());

        try {
            jdbcTemplate.update(SQL_ALTER_REQUEST, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeRequest(int requestId) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("request_id", requestId);

        try {
            jdbcTemplate.update(SQL_REMOVE_REQUEST, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    private static final String SQL_CREATE_REQUEST = """
        INSERT INTO request (client_from_id, client_to_id, message, satisfied, time_changed) 
        VALUES (:client_from_id, :client_to_id, :message, :satisfied, :time_changed) 
        RETURNING request_id
    """;

    private static final String SQL_GET_REQUEST_BY_ID = """
        SELECT request_id, client_from_id, client_to_id, message, satisfied, time_changed
        FROM request
        WHERE request_id = :request_id
    """;

    private static final String SQL_GET_REQUESTS_BY_TRAINER_ID = """
        SELECT request_id, client_from_id, client_to_id, message, satisfied, time_changed
        FROM request
        WHERE client_to_id = :client_to_id
            AND satisfied = false
    """;    
    
    private static final String SQL_GET_REQUESTS_BY_SIGNED_USER_ID = """
        SELECT request_id, client_from_id, client_to_id, message, satisfied, time_changed
        FROM request
        WHERE client_from_id = :client_from_id
            AND satisfied = false
    """;

    private static final String SQL_ALTER_REQUEST = """
        UPDATE request
        SET client_from_id = :client_from_id, client_to_id = :client_to_id, message = :message, satisfied = :satisfied, time_changed = :time_changed
        WHERE request_id = :request_id
    """;

    private static final String SQL_REMOVE_REQUEST = """
        DELETE FROM request
        WHERE request_id = :request_id
    """;
    
}
