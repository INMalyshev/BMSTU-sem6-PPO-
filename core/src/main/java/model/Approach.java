package model;

public class Approach extends NumberedItem {
    
    private int trainingID;
    private int amount;
    private int expectedAmount;
    private ExerciseType type;
    private boolean completed = false;

    public Approach(int id, int trainingID, int amount, int expectedAmount, ExerciseType type) {
        this.setID(id);
        this.trainingID = trainingID;
        this.setAmount(amount);
        this.setExpectedAmount(expectedAmount);
        this.setType(type);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int GetTrainingID() {
        return this.trainingID;
    }

    public int getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(int expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void cloneApproachPlan(ApproachPlan plan) {
        this.setAmount(0);
        this.setExpectedAmount(plan.getExpectedAmount());
        this.setType(plan.getType());
        this.setCompleted(false);
    }
}
