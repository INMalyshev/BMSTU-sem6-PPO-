package pgdataRepositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceApproachRepository;
import model.Approach;
import model.ExerciseType;
import pgdataMappers.ApproachMapper;
import pgdataMappers.IntMapper;

public class ApproachRepository implements InterfaceApproachRepository {

    private static final int DEFAULT_INT = -999;
    private static final boolean DEFAULT_BOOLEAN = false;

    private static final String SQL_CREATE_APPROACH = """
            INSERT INTO approach (training_id, amount, expected_amount, excersize_type_id, completed)
            VALUES (:training_id, :amount, :expected_amount, :excersize_type_id, :completed)
            RETURNING approach_id
            """;

    private static final String SQL_GET_APPROACH_BY_ID = """
            SELECT 
                approach_id, 
                training_id, 
                amount, 
                expected_amount, 
                name as excersize_type, 
                completed
            FROM
                approach
                JOIN excersize_type USING(excersize_type_id)
            WHERE
                approach_id = :approach_id
            """;

    private static final String SQL_GET_APPROACH_BY_TRAINING_ID = """
            SELECT 
                approach_id, 
                training_id, 
                amount, 
                expected_amount, 
                name as excersize_type, 
                completed
            FROM
                approach
                JOIN excersize_type USING(excersize_type_id)
            WHERE
                training_id = :training_id
            """;

    private static final String SQL_ALTER_APPROACH = """
            UPDATE
                approach
            SET
                training_id = :training_id, 
                amount = :amount, 
                expected_amount = :expected_amount, 
                excersize_type_id = :excersize_type_id, 
                completed = :completed
            WHERE
                approach_id = :approach_id
            """;

    private static final String SQL_REMOVE_APPROACH = """
            DELETE FROM approach
            WHERE approach_id = :approach_id
            """;

    private static final String SQL_GET_EXCERSIZE_TYPE_ID_BY_NAME = """
        SELECT
            excersize_type_id
        FROM
            excersize_type
        WHERE
            name = :name
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ApproachMapper approachMapper;
    private final IntMapper intMapper;

    public ApproachRepository(NamedParameterJdbcTemplate jdbcTemplate, ApproachMapper approachMapper, IntMapper intMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.approachMapper = approachMapper;
        this.intMapper = intMapper;

    }

    private int getExersizeTypeId(ExerciseType exerciseType) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", exerciseType.getTitle());

        try {
            Optional<Integer> exerciseTypeId = jdbcTemplate.query(SQL_GET_EXCERSIZE_TYPE_ID_BY_NAME, params, intMapper).stream().findFirst();

            if (exerciseTypeId.isPresent()) {
                return exerciseTypeId.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public int createApproach(int trainingID) throws CoreException {
        int exersizeTypeId = this.getExersizeTypeId(ExerciseType.PullUp);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_id", trainingID);
        params.addValue("amount", DEFAULT_INT);
        params.addValue("expected_amount", DEFAULT_INT);
        params.addValue("excersize_type_id", exersizeTypeId);
        params.addValue("completed", DEFAULT_BOOLEAN);

        try {
            Optional<Integer> approachId = jdbcTemplate.query(SQL_CREATE_APPROACH, params, intMapper).stream().findFirst();

            if (approachId.isPresent()) {
                return approachId.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public Approach getApproachByID(int approachID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_id", approachID);

        try {
            Optional<Approach> approachPlan = jdbcTemplate.query(SQL_GET_APPROACH_BY_ID, params, approachMapper).stream().findFirst();

            if (approachPlan.isPresent()) {
                return approachPlan.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public Approach[] getApproachByTrainingID(int trainingID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_id", trainingID);

        try {

            Approach[] approachPlans = jdbcTemplate.query(SQL_GET_APPROACH_BY_TRAINING_ID, params, approachMapper).toArray(new Approach[]{});

            return approachPlans;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterApproach(Approach approach) throws CoreException {
        
        int exercizeTypeId = this.getExersizeTypeId(approach.getType());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_id", approach.getID());
        params.addValue("expected_amount", approach.getExpectedAmount());
        params.addValue("training_id", approach.GetTrainingID());
        params.addValue("excersize_type_id", exercizeTypeId);
        params.addValue("amount", approach.getAmount());
        params.addValue("completed", approach.isCompleted());

        try {
            jdbcTemplate.update(SQL_ALTER_APPROACH, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeApproach(int approachID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_id", approachID);

        try {
            jdbcTemplate.update(SQL_REMOVE_APPROACH, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }
    
}
