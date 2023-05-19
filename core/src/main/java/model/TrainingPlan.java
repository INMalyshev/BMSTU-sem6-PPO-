package model;

public class TrainingPlan extends NumberedItem {

    private int creatorUserID;

    public TrainingPlan(int id, int creatorUserID) {
        this.setID(id);
        this.creatorUserID = creatorUserID;
    }

    public int getCreatorUserID() {
        return creatorUserID;
    }
}
