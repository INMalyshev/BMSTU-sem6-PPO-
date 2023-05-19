package mongodataRepositories.items;

public enum ApproachItems {
    approach_id("approach_id"),
    training_id("training_id"),
    amount("amount"),
    expected_amount("expected_amount"),
    excersize_type_id("excersize_type_id"),
    completed("completed");

    private String title;

    ApproachItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
