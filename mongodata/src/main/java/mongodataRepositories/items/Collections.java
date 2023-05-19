package mongodataRepositories.items;

public enum Collections {
    role("role"),
    client("client"),
    approach("approach"),
    approach_plan("approach_plan"),
    training("training"),
    training_plan("training_plan"),
    excersize_type("excersize_type"),
    request("request");

    private String title;

    Collections(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
