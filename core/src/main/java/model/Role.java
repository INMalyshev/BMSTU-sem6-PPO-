package model;

public enum Role {
    Trainer("Тренер"),
    SignedUser("Авторизованный пользователь");

    private String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
