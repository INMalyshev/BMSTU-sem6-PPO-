package mongodataRepositories.items;

public enum ExcersizeTypeItems {
    excersize_type_id("excersize_type_id"),
    name("name");

    private String title;

    ExcersizeTypeItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
