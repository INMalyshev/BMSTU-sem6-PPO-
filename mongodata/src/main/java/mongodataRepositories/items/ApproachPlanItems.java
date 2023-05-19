package mongodataRepositories.items;

public enum ApproachPlanItems {
    approach_plan_id("approach_plan_id"),
    expected_amount("expected_amount"),
    training_plan_id("training_plan_id"),
    excersize_type_id("excersize_type_id");

    private String title;

    ApproachPlanItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
