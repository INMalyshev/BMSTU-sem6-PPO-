package services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import exceptions.CoreException;

import interfaces.services.*;
import interfaces.repositories.*;

import model.*;

import validators.PermissionValidator;
import validators.ValueValidator;

@Component
public class TrainingService implements InterfaceTrainingService {

    private InterfaceTrainingRepository trainingRepository;
    private InterfaceApproachRepository approachRepository;

    private InterfaceTrainingPlanService planService;
    private InterfaceUserService userService;

    private Logger logger = null;

    public TrainingService(InterfaceTrainingRepository trainingRepository,
            InterfaceApproachRepository approachRepository, InterfaceTrainingPlanService planService,
            InterfaceUserService userService, Logger logger) {
        this.trainingRepository = trainingRepository;
        this.approachRepository = approachRepository;
        this.planService = planService;
        this.userService = userService;
        this.logger = logger;
    }

    @Override
    public int createTraining(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create training for user " + userID);

        User requester = getRequester(requesterUserID);

        validateTrainingServiceAccess(requester);
        validateEqualIDs(userID, requesterUserID);
    
        try {
            return this.trainingRepository.createTraining(userID);
        } catch (CoreException e) {
            String mes = "failed to create training for user " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public int createTraininByTrainingPlanID(int userID, int trainingPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create training for user " + userID + " from training plan " + trainingPlanID);

        User requester = getRequester(requesterUserID);
        TrainingPlan trainingPlan = this.planService.getTrainingPlanByID(trainingPlanID, requesterUserID);
        ApproachPlan[] approachPlans = this.planService.getApproachPlanByTrainingPlanID(trainingPlanID, requesterUserID);

        validateTrainingServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        int trainingID;
        try {
            trainingID = this.trainingRepository.createTraining(userID);
        } catch (CoreException e) {
            String mes = "failed to create training for user " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        for (int i = 0; i < approachPlans.length; i++) {
            try {
                this.createApproachByApproachPlanID(trainingID, approachPlans[i].getID(), requesterUserID);
            } catch (CoreException e) {
                String mes = "failed to create approach by approach plan " + approachPlans[i].getID();
                logger.log(Level.WARNING, mes, e);
                throw e;
            }
        }

        return trainingID;
    }

    @Override
    public Training getTrainingByID(int trainingID, int requesterUserID) throws CoreException {
        
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get training by id " + trainingID);
        
        Training training = null;
        try {
            training = this.trainingRepository.getTrainingByID(trainingID);
        } catch (CoreException e) {
            String mes = "failed to get training by id " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        User requester = getRequester(requesterUserID);

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, training.GetHolderUserID());

        return training;
    }

    @Override
    public Training[] getTrainingsByUserID(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get trainings by user id " + userID);

        User requester = getRequester(requesterUserID);

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, userID);

        try {
            return this.trainingRepository.getTrainingsByUserID(userID);
        } catch (CoreException e) {
            String mes = "failed to get trainings by user id " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public Training[] getDoneTrainingsByUserID(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get done trainings by user id " + userID);

        User requester = getRequester(requesterUserID);

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, userID);

        try {
            return this.trainingRepository.getDoneTrainingsByUserID(userID);
        } catch (CoreException e) {
            String mes = "failed to get done trainings by user id " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public Training[] getPlannedTrainingsByUserID(int userID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get planned trainings by user id " + userID);

        User requester = getRequester(requesterUserID);

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, userID);

        try {
            return this.trainingRepository.getPlannedTrainingsByUserID(userID);
        } catch (CoreException e) {
            String mes = "failed to get planned trainings by user id " + userID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void alterTraining(Training training, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to alter training " + training.getID());

        User requester = getRequester(requesterUserID);

        validateNotNull(training);
        validateTrainingServiceAccess(requester);
        validateEqualIDs(training.GetHolderUserID(), requesterUserID);

        this.trainingRepository.alterTraining(training);
        try {
            this.trainingRepository.alterTraining(training);
        } catch (CoreException e) {
            String mes = "failed to alter training " + training.getID();
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeTraining(int trainingID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove training " + trainingID);

        User requester = getRequester(requesterUserID);
        Training training = getTraining(trainingID);

        validateTrainingServiceAccess(requester);
        validateNotNull(training);
        validateEqualIDs(training.GetHolderUserID(), requesterUserID);
        
        Approach[] aprs = null;
        try {
            aprs = this.approachRepository.getApproachByTrainingID(trainingID);
        } catch (CoreException e) {
            String mes = "failed to get approaches by training id " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        for (int i = 0; i < aprs.length; i++) {
            try {
                this.approachRepository.removeApproach(aprs[i].getID());
            } catch (CoreException e) {
                String mes = "failed to remove approache " + aprs[i].getID();
                logger.log(Level.WARNING, mes, e);
                throw e;
            }
        }

        try {
            this.trainingRepository.removeTraining(trainingID);
        } catch (CoreException e) {
            String mes = "failed to remove training " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public int createApproach(int trainingID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create approach for training " + trainingID);

        User requester = getRequester(requesterUserID);
        Training training = getTraining(trainingID);

        validateTrainingServiceAccess(requester);
        validateEqualIDs(training.GetHolderUserID(), requesterUserID);

        try {
            return this.approachRepository.createApproach(trainingID);
        } catch (CoreException e) {
            String mes = "failed to create approach for training " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    // todo
    @Override
    public int createApproachByApproachPlanID(int trainingID, int approachPlanID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to create approach by approach plan " + approachPlanID + " for training " + trainingID);

        User requester = getRequester(requesterUserID);

        ApproachPlan approachPlan = null;
        try {
            approachPlan = this.planService.getApproachPlanByID(approachPlanID, requesterUserID);
        } catch (CoreException e) {
            String mes = "failed to get approach plan  " + approachPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        TrainingPlan trainingPlan = null;
        try {
            trainingPlan = this.planService.getTrainingPlanByID(approachPlan.getTrainingPlanID(), requesterUserID);
        } catch (CoreException e) {
            String mes = "failed to get training plan  " + approachPlanID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        validateTrainingServiceAccess(requester);
        validateEqualIDs(trainingPlan.getCreatorUserID(), requesterUserID);

        int approachID;
        try {
            approachID = this.approachRepository.createApproach(trainingID);
        } catch (CoreException e) {
            String mes = "failed to create approach for training " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        Approach approach = null;
        try {
            approach = this.approachRepository.getApproachByID(approachID);
        } catch (CoreException e) {
            String mes = "failed to get approach " + approachID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        approach.cloneApproachPlan(approachPlan);

        try {
            this.approachRepository.alterApproach(approach);
        } catch (CoreException e) {
            String mes = "failed to alter approach " + approachID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        return approachID;
    }

    @Override
    public Approach getApproachByID(int approachID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get approach by approach " + approachID);

        User requester = getRequester(requesterUserID);

        Approach approach = null;
        try {
            approach = this.approachRepository.getApproachByID(approachID);
        } catch (CoreException e) {
            String mes = "failed to get approach " + approachID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        Training training = getTraining(approach.GetTrainingID());

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, training.GetHolderUserID());

        return approach;
    }

    @Override
    public Approach[] getApproachByTrainingID(int trainingID, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to get approachs by trainig id " + trainingID);

        User requester = getRequester(requesterUserID);

        Approach[] approaches = null;
        try {
            approaches = this.approachRepository.getApproachByTrainingID(trainingID);
        } catch (CoreException e) {
            String mes = "failed to get approach by training id " + trainingID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        Training training = getTraining(trainingID);

        validateTrainingServiceAccess(requester);
        validateSignedUserObjectGetAccess(requester, training.GetHolderUserID());

        return approaches;
    }

    @Override
    public void alterApproach(Approach approach, int requesterUserID) throws CoreException {
        logger.log(Level.INFO, "user " + requesterUserID + " tries to alter approach " + approach.getID());

        validateNotNull(approach);

        User requester = getRequester(requesterUserID);
        Training training = getTraining(approach.GetTrainingID());

        validateTrainingServiceAccess(requester);
        validateEqualIDs(training.GetHolderUserID(), requesterUserID);

        try {
            this.approachRepository.alterApproach(approach);
        } catch (CoreException e) {
            String mes = "failed to alter approach " + approach.getID();
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    @Override
    public void removeApproach(int approachID, int requesterUserID) throws CoreException {

        logger.log(Level.INFO, "user " + requesterUserID + " tries to remove approach " + approachID);

        User requester = getRequester(requesterUserID);
        Approach approach = null;
        try {
            approach = this.approachRepository.getApproachByID(approachID);
        } catch (CoreException e) {
            String mes = "failed to get approach " + approachID;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }

        Training training = getTraining(approach.GetTrainingID());

        validateTrainingServiceAccess(requester);
        validateEqualIDs(training.GetHolderUserID(), requesterUserID);

        try {
            this.approachRepository.removeApproach(approachID);
        } catch (CoreException e) {
            String mes = "failed to remove approach " + approachID;
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

    private void validateTrainingServiceAccess(User requester) throws CoreException {
        try {
            PermissionValidator.validateTrainingServiceAccess(requester);
        } catch (CoreException e) {
            String mes = "permission validation faild for user " + requester.getID();
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

    private void validateNotNull(Object obj) throws CoreException {
        try {
            ValueValidator.validateNotNull(obj);
        } catch (CoreException e) {
            String mes = "not null validation failed";
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
    }

    private Training getTraining(int id) throws CoreException {
        Training training = null;
        try {
            training = this.trainingRepository.getTrainingByID(id);
        } catch (CoreException e) {
            String mes = "failed to get training by id " + id;
            logger.log(Level.WARNING, mes, e);
            throw e;
        }
        return training;
    }
}
