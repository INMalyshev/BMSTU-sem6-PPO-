package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.TrainingPlan;

public class TrainingPlanMapper implements RowMapper<TrainingPlan> {

    @Override
    @Nullable
    public TrainingPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        int trainingPlanId = rs.getInt("training_plan_id");
        int creatorUserId = rs.getInt("creator_user_id");

        return new TrainingPlan(trainingPlanId, creatorUserId);
    }
    
}
