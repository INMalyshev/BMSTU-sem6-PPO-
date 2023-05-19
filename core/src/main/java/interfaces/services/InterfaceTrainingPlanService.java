package interfaces.services;

import model.TrainingPlan;
import model.ApproachPlan;

import exceptions.CoreException;

public interface InterfaceTrainingPlanService {

    // Training plan

    // returns ID
    public int createTrainingPlan(int userID, int requesterUserID) throws CoreException;

    public TrainingPlan getTrainingPlanByID(int trainingPlanID, int requesterUserID) throws CoreException;

    public TrainingPlan[] getTrainingPlanByUserID(int userID, int requesterUserID) throws CoreException;

    public void alterTrainingPlan(TrainingPlan trainingPlan, int requesterUserID) throws CoreException;

    public void removeTrainingPlan(int trainingPlanID, int requesterUserID) throws CoreException;

    // Training approach plan

    // returns ID
    public int createApproachPlan(int trainingPlanID, int requesterUserID) throws CoreException;

    public ApproachPlan getApproachPlanByID(int approachPlanID, int requesterUserID) throws CoreException;

    public ApproachPlan[] getApproachPlanByTrainingPlanID(int trainingPlanID, int requesterUserID) throws CoreException;

    public void alterApproachPlan(ApproachPlan approachPlan, int requesterUserID) throws CoreException;

    public void removeApproachPlan(int approachPlanID, int requesterUserID) throws CoreException;
    
}
