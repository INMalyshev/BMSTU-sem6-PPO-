package mongodataRepositories.items;

public enum TrainingPlanItems {
    training_plan_id("training_plan_id"),
    creator_user_id("creator_user_id");

    private String title;

    TrainingPlanItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
