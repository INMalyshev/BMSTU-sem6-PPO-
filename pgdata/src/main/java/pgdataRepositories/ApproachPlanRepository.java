package pgdataRepositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceApproachPlanRepository;
import model.ApproachPlan;
import model.ExerciseType;
import pgdataMappers.ApproachPlanMapper;
import pgdataMappers.IntMapper;

public class ApproachPlanRepository implements InterfaceApproachPlanRepository {

    private static final int DEFAULT_INT = -999;

    private static final String SQL_CREATE_APPROACH_PLAN = """
            INSERT INTO approach_plan (expected_amount, training_plan_id, excersize_type_id)
            VALUES (:expected_amount, :training_plan_id, :excersize_type_id)
            RETURNING approach_plan_id
            """;
    
    private static final String SQL_GET_APPROACH_PLAN_BY_ID = """
            SELECT
                approach_plan_id,
                expected_amount,
                training_plan_id,
                name as excersize_type
            FROM
                approach_plan
                JOIN excersize_type USING(excersize_type_id)
            WHERE
                approach_plan_id = :approach_plan_id
            """;

    private static final String SQL_GET_APPROACH_PLAN_BY_TRAINING_PLAN_ID = """
            SELECT
                approach_plan_id,
                expected_amount,
                training_plan_id,
                name as excersize_type
            FROM
                approach_plan
                JOIN excersize_type USING(excersize_type_id)
            WHERE
                training_plan_id = :training_plan_id
            """;
    
    private static final String SQL_ALTER_APPROACH_PLAN = """
            UPDATE 
                approach_plan
            SET
                expected_amount = :expected_amount,
                training_plan_id = :training_plan_id,
                excersize_type_id = :excersize_type_id
            WHERE
                approach_plan_id = :approach_plan_id
            """;

    private static final String SQL_REMOVE_APPROACH_PLAN = """
            DELETE FROM
                approach_plan
            WHERE
                approach_plan_id = :approach_plan_id
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
    private final ApproachPlanMapper approachPlanMapper;
    private final IntMapper intMapper;

    public ApproachPlanRepository(NamedParameterJdbcTemplate jdbcTemplate, ApproachPlanMapper approachPlanMapper, IntMapper intMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.approachPlanMapper = approachPlanMapper;
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
    public int createApproachPlan(int trainingPlanID) throws CoreException {

        int exersizeTypeId = this.getExersizeTypeId(ExerciseType.PullUp);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_plan_id", trainingPlanID);
        params.addValue("excersize_type_id", exersizeTypeId);
        params.addValue("expected_amount", DEFAULT_INT);

        try {
            Optional<Integer> approachPlanId = jdbcTemplate.query(SQL_CREATE_APPROACH_PLAN, params, intMapper).stream().findFirst();

            if (approachPlanId.isPresent()) {
                return approachPlanId.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public ApproachPlan getApproachPlanByID(int approachPlanID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_plan_id", approachPlanID);

        try {
            Optional<ApproachPlan> approachPlan = jdbcTemplate.query(SQL_GET_APPROACH_PLAN_BY_ID, params, approachPlanMapper).stream().findFirst();

            if (approachPlan.isPresent()) {
                return approachPlan.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public ApproachPlan[] getApproachPlanByTrainingPlanID(int trainingPlanID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_plan_id", trainingPlanID);

        try {

            ApproachPlan[] approachPlans = jdbcTemplate.query(SQL_GET_APPROACH_PLAN_BY_TRAINING_PLAN_ID, params, approachPlanMapper).toArray(new ApproachPlan[]{});

            return approachPlans;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterApproachPlan(ApproachPlan approachPlan) throws CoreException {

        int exercizeTypeId = this.getExersizeTypeId(approachPlan.getType());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_plan_id", approachPlan.getID());
        params.addValue("expected_amount", approachPlan.getExpectedAmount());
        params.addValue("training_plan_id", approachPlan.getTrainingPlanID());
        params.addValue("excersize_type_id", exercizeTypeId);

        try {
            jdbcTemplate.update(SQL_ALTER_APPROACH_PLAN, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeApproachPlan(int approachPlanID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("approach_plan_id", approachPlanID);

        try {
            jdbcTemplate.update(SQL_REMOVE_APPROACH_PLAN, params);
        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }
    
}
