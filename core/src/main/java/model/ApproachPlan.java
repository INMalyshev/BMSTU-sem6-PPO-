package model;

public class ApproachPlan extends NumberedItem {
    private ExerciseType type;
    private int expectedAmount;
    private int trainingPlanID;

    public ApproachPlan(int trainingPlanID, ExerciseType type, int amount, int id) {
        this.setID(id);
        this.expectedAmount = amount;
        this.type = type;
        this.trainingPlanID = trainingPlanID;
    }

    public ExerciseType getType() {
        return this.type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public int getExpectedAmount() {
        return this.expectedAmount;
    }

    public void setExpectedAmount(int amount) {
        this.expectedAmount = amount;
    }

    public int getTrainingPlanID() {
        return trainingPlanID;
    }
}
