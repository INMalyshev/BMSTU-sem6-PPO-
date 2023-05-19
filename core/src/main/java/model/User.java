package model;

public class User extends NumberedItem{

    private String name;
    private Role role;

    public User(String name, Role role, int id) {
        this.setID(id);
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}