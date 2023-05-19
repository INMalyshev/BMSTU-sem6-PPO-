package interfaces.services;

import model.Training;
import model.Approach;

import exceptions.CoreException;

public interface InterfaceTrainingService {

    // Training
    
    // returns trainingID
    public int createTraining(int userID, int requesterUserID) throws CoreException;

    // returns trainingID
    public int createTraininByTrainingPlanID(int userID, int trainingPlanID, int requesterUserID) throws CoreException; 

    public Training getTrainingByID(int trainingID, int requesterUserID) throws CoreException;
    
    public Training[] getTrainingsByUserID(int userID, int requesterUserID) throws CoreException;
    
    public Training[] getDoneTrainingsByUserID(int userID, int requesterUserID) throws CoreException;
    
    public Training[] getPlannedTrainingsByUserID(int userID, int requesterUserID) throws CoreException;
    
    public void alterTraining(Training training, int requesterUserID) throws CoreException;
    
    public void removeTraining(int trainingID, int requesterUserID) throws CoreException;

    // Approach
    
    // returns approachID
    public int createApproach(int trainingID, int requesterUserID) throws CoreException; 
    
    // returns approachID
    public int createApproachByApproachPlanID(int trainingID, int approachPlanID, int requesterUserID) throws CoreException; 
    
    public Approach getApproachByID(int approachID, int requesterUserID) throws CoreException;
    
    public Approach[] getApproachByTrainingID(int trainingID, int requesterUserID) throws CoreException;
    
    public void alterApproach(Approach approach, int requesterUserID) throws CoreException;
    
    public void removeApproach(int approachID, int requesterUserID) throws CoreException;

}