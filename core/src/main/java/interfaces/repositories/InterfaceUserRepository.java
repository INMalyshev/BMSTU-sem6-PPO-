package interfaces.repositories;

import model.Role;
import model.User;

import exceptions.CoreException;

public interface InterfaceUserRepository {

    // returns userID
    public int createUser() throws CoreException;
    
    public User getUser(int userID) throws CoreException;
    
    public void alterUser(User user) throws CoreException;
    
    public void removeUser(int userID) throws CoreException;

    public User[] getUsersByRole(Role userRole) throws CoreException;

}
