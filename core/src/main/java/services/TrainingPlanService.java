package services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import exceptions.CoreException;
import interfaces.repositories.*;
import interfaces.services.*;

import model.*;

import validators.*;

@Component
public class TrainingPlanService implements InterfaceTrainingPlanService {

    private InterfaceTrainingPlanRepository trainingPlanRepository;
    private InterfaceApproachPlanRepository approachPlanRepository;

    private InterfaceUserService userService;

    private Logger logger = null;

    public TrainingPlanService(InterfaceTrainingPlanRepository trainingPlanRepository,
            InterfaceApproachPlanRepository approachPlanRepository, InterfaceUserService userService, Logger logger) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.approachPlanRepository = approachPlanRepository;
        this.userService = userService;
        this.logger = logger;
    }

    @Override
    public int createTrainingPlan(int UserID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create training plan for user " + UserID);

        User requester = getRequester(requesterUserID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(UserID, requesterUserID);

        try {
            return this.trainingPlanRepository.createTrainingPlan(UserID);
        } catch (CoreException e) {
            String mes = "failed to create training plan for user " + UserID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public TrainingPlan getTrainingPlanByID(int trainingPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tryes to get training plan by training plan id " + trainingPlanID);

        User requester = getRequester(requesterUserID);
        TrainingPlan plan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(plan.getCreatorUserID(), requesterUserID);

        return plan;
    }

    @Override
    public TrainingPlan[] getTrainingPlanByUserID(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tryes to get training plan by user id " + userID);

        User requester = getRequester(requesterUserID);
        TrainingPlan[] plans = null;

        try {
            plans = this.trainingPlanRepository.getTrainingPlanByUserID(userID);
        } catch (CoreException e) {
            String mes = "failed to get training plans by user id " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(userID, requesterUserID);

        return plans;
    }

    @Override
    public void alterTrainingPlan(TrainingPlan trainingPlan, int requesterUserID) throws CoreException {
        String logMessage = "user " + requesterUserID + " tries to alter training plan";
        logger.log(Level.INFO, logMessage);

        User requester = getRequester(requesterUserID);
        validateNotNull(trainingPlan);
        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        try {
            this.trainingPlanRepository.alterTrainingPlan(trainingPlan);
        } catch (CoreException e) {
            String mes = "failed to alter training plan " + trainingPlan.getID();
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeTrainingPlan(int trainingPlanID, int requesterUserID) throws CoreException { 
        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove training plan by id " + trainingPlanID);

        User requester = getRequester(requesterUserID);
        TrainingPlan plan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(plan.getCreatorUserID(), requesterUserID);

        ApproachPlan[] aprs = null;

        try {
            aprs = this.approachPlanRepository.getApproachPlanByTrainingPlanID(trainingPlanID);
        } catch (CoreException e) {
            String mes = "failed to get approach plans by training plan id " + trainingPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        for (int i = 0; i < aprs.length; i++) {
            try {
                this.approachPlanRepository.removeApproachPlan(aprs[i].getID());
            } catch (CoreException e)  {
                String mes = "failed to remove approach plan by id " + aprs[i].getID();
                logger.log(Level.WARNING, mes, e);
                throw e;
            }
        }

        try {
            this.trainingPlanRepository.removeTrainingPlan(trainingPlanID);
        } catch (CoreException e)  {
            String mes = "failed to remove training plan by id " + trainingPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public int createApproachPlan(int trainingPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create approach plan by training plan id " + trainingPlanID);

        User requester = getRequester(requesterUserID);

        TrainingPlan plan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(plan.getCreatorUserID(), requesterUserID);

        try {
            return this.approachPlanRepository.createApproachPlan(trainingPlanID);
        } catch (CoreException e) {
            String mes = "failed to create aproach plan for training " + trainingPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public ApproachPlan getApproachPlanByID(int approachPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get approach plan by id " + approachPlanID);

        User requester = getRequester(requesterUserID);

        ApproachPlan approachPlan = null;
        try {
            approachPlan = this.approachPlanRepository.getApproachPlanByID(approachPlanID);
        } catch (CoreException e) {
            String mes = "failed to get approach plan by id " + approachPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        int trainingPlanID = approachPlan.getTrainingPlanID();
        
        TrainingPlan trainingPlan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        return approachPlan;
    }

    @Override
    public ApproachPlan[] getApproachPlanByTrainingPlanID(int trainingPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get approach plan by training plan id " + trainingPlanID);

        User requester = getRequester(requesterUserID);

        TrainingPlan trainingPlan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        try {
            return this.approachPlanRepository.getApproachPlanByTrainingPlanID(trainingPlanID);
        } catch (CoreException e) {
            String mes = "failed to get approach plan by training plan id " + trainingPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void alterApproachPlan(ApproachPlan approachPlan, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to alter approach plan");

        validateNotNull(approachPlan);

        User requester = getRequester(requesterUserID);

        int trainingPlanID = approachPlan.getTrainingPlanID();
        
        TrainingPlan trainingPlan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        try {
            this.approachPlanRepository.alterApproachPlan(approachPlan);
        } catch (CoreException e) {
            String mes = "failed to alter approach plan";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeApproachPlan(int approachPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove approach plan by id " + approachPlanID);

        User requester = getRequester(requesterUserID);
        ApproachPlan approachPlan = null;
        try {
            approachPlan = this.approachPlanRepository.getApproachPlanByID(approachPlanID);
        } catch (CoreException e) {
            String mes = "failed to get approach plan by id " + approachPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
        int trainingPlanID = approachPlan.getTrainingPlanID();

        TrainingPlan trainingPlan = geTrainingPlan(trainingPlanID);

        validateTrainingPlanServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        this.approachPlanRepository.removeApproachPlan(approachPlanID);

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

    private void validateTrainingPlanServiceAccess(User requester) throws CoreException {
        try {
            PermissionValidator.validateTrainingPlanServiceAccess(requester);
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

    private void validateNotNull(Object obj) throws CoreException {
        try {
            ValueValidator.validateNotNull(obj);
        } catch (CoreException e) {
            String mes = "not null validation failed";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private TrainingPlan geTrainingPlan(int id) throws CoreException {
        TrainingPlan plan = null;
        try {
            plan = this.trainingPlanRepository.getTrainingPlanByID(id);
        } catch (CoreException e) {
            String mes = "failed to get training plan " + id;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
        return plan;
    }
}
