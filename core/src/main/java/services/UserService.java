package services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import exceptions.CoreException;
import interfaces.repositories.InterfaceUserRepository;
import interfaces.services.InterfaceUserService;
import model.Role;
import model.User;

import validators.PermissionValidator;

@Component
public class UserService implements InterfaceUserService {

    private InterfaceUserRepository userRepository;

    private Logger logger = null;

    public UserService(InterfaceUserRepository userRepository, Logger logger) {
        this.userRepository = userRepository;
        this.logger = logger;
    }

    @Override
    public int createUser() throws CoreException {
        logger.log(Level.INFO, "creatining new user");

        try {
            return this.userRepository.createUser();
        } catch (CoreException e) {
            String mes = "failed to create new user";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public User getUser(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get user " + userID);

        User requesterUser = getRequester(requesterUserID);

        validateUserServiceAccess(requesterUser);
        validateSignedUserObjectGetAccess(requesterUser, userID);

        try {
            return this.userRepository.getUser(userID);
        } catch (CoreException e) {
            String mes = "failed to get user " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void alterUser(User user, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to alter user " + user.getID());

        User requesterUser = getRequester(requesterUserID);

        validateUserServiceAccess(requesterUser);
        validateEqualIDs(user.getID(), requesterUserID);

        try {
            this.userRepository.alterUser(user);
        } catch (CoreException e) {
            String mes = "failed to alter user " + user.getID();
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeUser(int userID, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove user " + userID);

        User requesterUser = getRequester(requesterUserID);

        validateUserServiceAccess(requesterUser);
        validateEqualIDs(userID, requesterUserID);

        try {
            this.userRepository.removeUser(userID);
        } catch (CoreException e) {
            String mes = "failed to remove user " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private void validateUserServiceAccess(User requester) throws CoreException {
        try {
            PermissionValidator.validateUserServiceAccess(requester);
        } catch (CoreException e) {
            String mes = "permission validation faild for user " + requester.getID();
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private void validateEqualIDs(int UserID, int requesterUserID) throws CoreException {
        try {
            PermissionValidator.validateEqualIDs(UserID, requesterUserID);
        } catch (CoreException e) {
            String mes = "equal id validation faild for users " + UserID + " and " + requesterUserID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private void validateSignedUserObjectGetAccess(User req, int useId) throws CoreException {
        try {
            PermissionValidator.validateSignedUserObjectGetAccess(req, useId);
        } catch (CoreException e) {
            String mes = "rejected access for user " + req.getID() + " to " + useId;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private User getRequester(int requesterUserID) throws CoreException {
        User requester = null;

        try {
            requester = this.userRepository.getUser(requesterUserID);
        } catch (CoreException e) {
            String mes = "failed to get requester user by id " + requesterUserID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        return requester;
    }

    @Override
    public User[] getTrainers(int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get trainer list");

        User requesterUser = getRequester(requesterUserID);

        validateUserServiceAccess(requesterUser);

        try {
            return this.userRepository.getUsersByRole(Role.Trainer);
        } catch (CoreException e) {
            String mes = "failed to get trainer list";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public User[] getSignedUsers(int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get signed user list");

        User requesterUser = getRequester(requesterUserID);

        validateUserServiceAccess(requesterUser);

        try {
            return this.userRepository.getUsersByRole(Role.SignedUser);
        } catch (CoreException e) {
            String mes = "failed to get signed user list";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }
    
}
