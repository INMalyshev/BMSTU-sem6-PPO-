package model;

public class Training extends NumberedItem {

    private boolean completed = false;
    private int holderUserID;

    public Training(int id, int holderUserID) {
        this.setID(id);
        this.holderUserID = holderUserID;
    }

    public boolean GetCompleted() {
        return this.completed;
    }

    public void SetCompleted(boolean completed) {
        this.completed = completed;
    }

    public int GetHolderUserID() {
        return this.holderUserID;
    }

}