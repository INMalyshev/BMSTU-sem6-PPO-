package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.Approach;
import model.ExerciseType;

public class ApproachMapper implements RowMapper<Approach> {

    @Override
    @Nullable
    public Approach mapRow(ResultSet rs, int rowNum) throws SQLException {

        int id = rs.getInt("approach_id");
        int trainingId = rs.getInt("training_id");
        int amount = rs.getInt("amount");
        int expectedAmount = rs.getInt("expected_amount");
        String excersizeType = rs.getString("excersize_type");

        for (ExerciseType et : ExerciseType.values()) {
            if (excersizeType.equals(et.getTitle())) {
                return new Approach(id, trainingId, amount, expectedAmount, et);
            }
        }

        return null;
    }
    
}
