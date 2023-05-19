package consoleModel;

public class RuntimeProfile {
    private boolean authorized;
    private int userId;

    public RuntimeProfile() {
        this.authorized = false;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
