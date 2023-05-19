package mongodataRepositories.items;

public enum RequestItems {
    request_id("request_id"),
    client_from_id("client_from_id"),
    client_to_id("client_to_id"),
    message("message"),
    satisfied("satisfied"),
    time_changed("time_changed");

    private String title;

    RequestItems(String title) {
        this.title = title;
    }
    public String get() {
        return this.title;
    }
}
