package mongodataRepositories.items;

public enum TrainingItems {
    training_id("training_id"),
    completed("completed"),
    holder_user_id("holder_user_id");

    private String title;

    TrainingItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
