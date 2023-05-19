package mongodataRepositories.items;

public enum RoleItems {
    role_id("role_id"),
    name("name");

    private String title;

    RoleItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}

