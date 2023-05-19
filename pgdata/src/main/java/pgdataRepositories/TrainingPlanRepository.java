package pgdataRepositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceTrainingPlanRepository;
import model.TrainingPlan;
import pgdataMappers.IntMapper;
import pgdataMappers.TrainingPlanMapper;

public class TrainingPlanRepository implements InterfaceTrainingPlanRepository {

    private static final String SQL_CREATE_TRAINING_PLAN = """
            INSERT INTO training_plan (creator_user_id)
            VALUES (:creator_user_id)
            RETURNING training_plan_id
            """;
    
    private static final String SQL_GET_TRAINING_PLAN_BY_ID = """
            SELECT
                training_plan_id,
                creator_user_id
            FROM
                training_plan
            WHERE
                training_plan_id = :training_plan_id
            """;
    
    private static final String SQL_GET_TRAINING_PLAN_BY_USER_ID = """
            SELECT
                training_plan_id,
                creator_user_id
            FROM
                training_plan
            WHERE
                creator_user_id = :creator_user_id
            """;

    private static final String SQL_ALTER_TRAINING_PLAN = """
            UPDATE
                training_plan
            SET
                creator_user_id = :creator_user_id
            WHERE
                training_plan_id = :training_plan_id
            """;

    private static final String SQL_DELETE_TRAINING_PLAN = """
            DELETE FROM
                training_plan
            WHERE
                training_plan_id = :training_plan_id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TrainingPlanMapper trainingPlanMapper;
    private final IntMapper intMapper;

    public TrainingPlanRepository(NamedParameterJdbcTemplate jdbcTemplate, TrainingPlanMapper trainingPlanMapper, IntMapper intMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.trainingPlanMapper = trainingPlanMapper;
        this.intMapper = intMapper;

    }

    @Override
    public int createTrainingPlan(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creator_user_id", userID);

        try {

            Optional<Integer> trainingPlanID = jdbcTemplate.query(SQL_CREATE_TRAINING_PLAN, params, intMapper).stream().findFirst();

            if (trainingPlanID.isPresent()) {
                return trainingPlanID.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public TrainingPlan getTrainingPlanByID(int trainingPlanID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_plan_id", trainingPlanID);

        try {

            Optional<TrainingPlan> trainingPlan = jdbcTemplate.query(SQL_GET_TRAINING_PLAN_BY_ID, params, trainingPlanMapper).stream().findFirst();

            if (trainingPlan.isPresent()) {
                return trainingPlan.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public TrainingPlan[] getTrainingPlanByUserID(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creator_user_id", userID);

        try {

            TrainingPlan[] trainingPlans = jdbcTemplate.query(SQL_GET_TRAINING_PLAN_BY_USER_ID, params, trainingPlanMapper).toArray(new TrainingPlan[]{});

            return trainingPlans;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterTrainingPlan(TrainingPlan trainingPlan) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("creator_user_id", trainingPlan.getCreatorUserID());
        params.addValue("training_plan_id", trainingPlan.getID());

        try {

            jdbcTemplate.update(SQL_ALTER_TRAINING_PLAN, params);

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeTrainingPlan(int trainingPlanID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_plan_id", trainingPlanID);

        try {

            jdbcTemplate.update(SQL_DELETE_TRAINING_PLAN, params);

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }
    
}
