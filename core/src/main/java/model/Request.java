package model;

import java.util.Date;

public class Request extends NumberedItem {

    private static final String DEFAULT_MESSAGE_STRING = "Пользователь не оставил комментарий.";
    
    private int userFromId;
    private int userToId;
    private String message;
    private boolean satisfied;
    private Date changed;

    public Request(int id, int userFronId, int userToId, String message, boolean satisfied, Date changed) {
        this.setID(id);
        this.userFromId = userFronId;
        this.userToId = userToId;
        this.setMessage(message);
        this.satisfied = satisfied;
        this.changed = changed;
    }

    public int getUserFromId() {
        return userFromId;
    }

    public int getUserToId() {
        return userToId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSatisfied() {
        return satisfied;
    }
    
    public Date getChanged() {
        return changed;
    }

    public void setUserFromId(int userFronId) {
        this.userFromId = userFronId;
    }

    public void setUserToId(int userToId) {
        this.userToId = userToId;
    }

    public void setMessage(String message) {

        if (message == null) {
            message = DEFAULT_MESSAGE_STRING;
        }

        if (message.length() == 0) {
            message = DEFAULT_MESSAGE_STRING;
        }

        this.message = message;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public void updateChanged() {
        this.changed = new Date();
    }
    
}
