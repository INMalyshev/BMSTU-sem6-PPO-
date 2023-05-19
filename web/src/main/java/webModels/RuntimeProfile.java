package webModels;

import model.User;

public class RuntimeProfile {
    private boolean authorized;
    private User user;

    public RuntimeProfile() {
        this.authorized = false;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
