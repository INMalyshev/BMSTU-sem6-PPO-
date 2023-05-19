package mongodataRepositories.items;


public enum ClientItems {
    client_id("client_id"),
    name("name"),
    role_id("role_id");

    private String title;

    ClientItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
