package pgdataRepositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import exceptions.CoreException;
import exceptions.DataLayerException;
import interfaces.repositories.InterfaceTrainingRepository;
import model.Training;
import pgdataMappers.IntMapper;
import pgdataMappers.TrainingMapper;

public class TrainingRepository implements InterfaceTrainingRepository {

    private static final String SQL_CREATE_TRAINING = """
            INSERT INTO training (completed, holder_user_id)
            VALUES (:completed, :holder_user_id)
            RETURNING training_id
            """;

    private static final String SQL_GET_TRAINING_BY_ID = """
            SELECT
                training_id,
                completed,
                holder_user_id
            FROM
                training
            WHERE
                training_id = :training_id
            """;

    private static final String SQL_GET_TRAININGS_BY_USER_ID = """
            SELECT
                training_id,
                completed,
                holder_user_id
            FROM
                training
            WHERE
                holder_user_id = :holder_user_id
            """;

    private static final String SQL_GET_DONE_TRAININGS_BY_USER_ID = """
            SELECT
                training_id,
                completed,
                holder_user_id
            FROM
                training
            WHERE
                holder_user_id = :holder_user_id
                AND completed
            """;

    private static final String SQL_GET_PLANNED_TRAININGS_BY_USER_ID = """
            SELECT
                training_id,
                completed,
                holder_user_id
            FROM
                training
            WHERE
                holder_user_id = :holder_user_id
                AND NOT completed
            """;

    private static final String SQL_ALTER_TRAINING = """
            UPDATE
                training
            SET
                completed = :completed,
                holder_user_id = :holder_user_id
            WHERE
                training_id = :training_id
            """;
    
    private static final String SQL_DELETE_TRAINING = """
            DELETE FROM training
            WHERE training_id = :training_id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TrainingMapper trainingMapper;
    private final IntMapper intMapper;

    public TrainingRepository(NamedParameterJdbcTemplate jdbcTemplate, TrainingMapper trainingMapper, IntMapper intMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.trainingMapper = trainingMapper;
        this.intMapper = intMapper;

    }

    @Override
    public int createTraining(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("holder_user_id", userID);
        params.addValue("completed", false);

        try {

            Optional<Integer> trainingID = jdbcTemplate.query(SQL_CREATE_TRAINING, params, intMapper).stream().findFirst();

            if (trainingID.isPresent()) {
                return trainingID.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public Training getTrainingByID(int trainingID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_id", trainingID);

        try {
            
            Optional<Training> training = jdbcTemplate.query(SQL_GET_TRAINING_BY_ID, params, trainingMapper).stream().findFirst();

            if (training.isPresent()) {
                return training.get();
            }

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }

        throw new DataLayerException();
    }

    @Override
    public Training[] getTrainingsByUserID(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("holder_user_id", userID);

        try {

            Training[] trainings = jdbcTemplate.query(SQL_GET_TRAININGS_BY_USER_ID, params, trainingMapper).toArray(new Training[]{});

            return trainings;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training[] getDoneTrainingsByUserID(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("holder_user_id", userID);

        try {

            Training[] trainings = jdbcTemplate.query(SQL_GET_DONE_TRAININGS_BY_USER_ID, params, trainingMapper).toArray(new Training[]{});

            return trainings;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public Training[] getPlannedTrainingsByUserID(int userID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("holder_user_id", userID);

        try {

            Training[] trainings = jdbcTemplate.query(SQL_GET_PLANNED_TRAININGS_BY_USER_ID, params, trainingMapper).toArray(new Training[]{});

            return trainings;

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void alterTraining(Training training) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("completed", training.GetCompleted());
        params.addValue("holder_user_id", training.GetHolderUserID());
        params.addValue("training_id", training.getID());

        try {

            jdbcTemplate.update(SQL_ALTER_TRAINING, params);

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }

    @Override
    public void removeTraining(int trainingID) throws CoreException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("training_id", trainingID);

        try {

            jdbcTemplate.update(SQL_DELETE_TRAINING, params);

        } catch (DataAccessException e) {
            throw new DataLayerException(e);
        }
    }
    
}
