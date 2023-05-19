package interfaces.services;

import model.User;

import exceptions.CoreException;

public interface InterfaceUserService {

    // returns userID
    public int createUser() throws CoreException;
    
    public User getUser(int userID, int requesterUserID) throws CoreException;
    
    public void alterUser(User user, int requesterUserID) throws CoreException;
    
    public void removeUser(int userID, int requesterUserID) throws CoreException;

    public User[] getTrainers(int requesterUserID) throws CoreException;

    public User[] getSignedUsers(int requesterUserID) throws CoreException;

}