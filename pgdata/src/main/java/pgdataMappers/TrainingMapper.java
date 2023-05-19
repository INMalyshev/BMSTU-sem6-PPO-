package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.Training;

public class TrainingMapper implements RowMapper<Training> {

    @Override
    @Nullable
    public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
        int trainingId = rs.getInt("training_id");
        int holderUserId = rs.getInt("holder_user_id");
        boolean completed = rs.getBoolean("completed");

        Training training = new Training(trainingId, holderUserId);
        training.SetCompleted(completed);

        return training;
    }
    
}
