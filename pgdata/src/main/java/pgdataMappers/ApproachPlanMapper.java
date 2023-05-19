package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.ApproachPlan;
import model.ExerciseType;

public class ApproachPlanMapper implements RowMapper<ApproachPlan> {

    @Override
    @Nullable
    public ApproachPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        int approachPlanId = rs.getInt("approach_plan_id");
        int expectedAmount = rs.getInt("expected_amount");
        int trainingPlanId = rs.getInt("training_plan_id");
        String excersizeType = rs.getString("excersize_type");
        

        for (ExerciseType et : ExerciseType.values()) {
            if (excersizeType.equals(et.getTitle())) {
                return new ApproachPlan(trainingPlanId, et, expectedAmount, approachPlanId);
            }
        }

        return null;
    }
    
}
