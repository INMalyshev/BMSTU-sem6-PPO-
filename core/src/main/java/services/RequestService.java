package services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import exceptions.CoreException;
import interfaces.repositories.InterfaceRequestRepository;
import interfaces.services.InterfaceRequestService;
import interfaces.services.InterfaceUserService;
import model.Request;
import model.User;
import validators.PermissionValidator;

@Component
public class RequestService implements InterfaceRequestService {

    private InterfaceRequestRepository requestRepository;

    private InterfaceUserService userService;

    private Logger logger = null;

    public RequestService(InterfaceRequestRepository requestRepository, InterfaceUserService userService,
            Logger logger) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.logger = logger;
    }

    @Override
    public int createRequest(int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to create request");

        User requester = getRequester(requesterUserID);
        validateSignedUserAccess(requester);

        try {
            return requestRepository.createRequest();
        } catch (CoreException e) {
            String mes = "failed to create new request";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

    }

    @Override
    public Request getRequest(int requestId, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to get request with id " + requestId);

        try {
            return requestRepository.getRequest(requestId);
        } catch (CoreException e) {
            String mes = "failed to get request by id";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

    }

    @Override
    public Request[] getRequestsByTrainerId(int trainerId, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to get all requests by trainer id " + trainerId);

        User requester = getRequester(requesterUserID);
        validateTrainerAccess(requester);
        validateEqualIDs(trainerId, requesterUserID);

        try {
            return requestRepository.getRequestsByTrainerId(trainerId);
        } catch (CoreException e) {
            String mes = "failed to get request by trainer id";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

    }

    @Override
    public Request[] getRequestsBySignedUserId(int userId, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to get all requests by signed user id " + userId);

        User requester = getRequester(requesterUserID);
        validateSignedUserAccess(requester);
        validateEqualIDs(userId, requesterUserID);


        try {
            return requestRepository.getRequestsBySignedUserId(userId);
        } catch (CoreException e) {
            String mes = "failed to get request by signed user id";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

    }

    @Override
    public void alterRequest(Request request, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to alter request with id " + request.getID());

        try {
            requestRepository.alterRequest(request);
        } catch (CoreException e) {
            String mes = "failed to alter request";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeRequest(int requestId, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove request with id " + requestId);

        User requester = getRequester(requesterUserID);
        validateSignedUserAccess(requester);

        try {
            requestRepository.removeRequest(requestId);
        } catch (CoreException e) {
            String mes = "failed to remove request";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

    }
    
    private User getRequester(int requesterUserID) throws CoreException {
        User requester = null;

        try {
            requester = this.userService.getUser(requesterUserID, requesterUserID);
        } catch (CoreException e) {
            String mes = "failed to get requester user by id " + requesterUserID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        return requester;
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

    private void validateTrainerAccess(User requester) throws CoreException {
        try {
            PermissionValidator.validateRequestServiceTrainerAccess(requester);
        } catch (CoreException e) {
            String mes = "Request service (trainer side): " + requester.getRole().getTitle() + " access denied.";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private void validateSignedUserAccess(User requester) throws CoreException {
        try {
            PermissionValidator.validateRequestServiceSignedUserAccess(requester);
        } catch (CoreException e) {
            String mes = "Request service (signed user side): " + requester.getRole().getTitle() + " access denied.";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

}
