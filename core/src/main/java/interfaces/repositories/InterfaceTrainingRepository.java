package interfaces.repositories;

import model.Training;

import exceptions.CoreException;

public interface InterfaceTrainingRepository {

    // returns trainingID
    public int createTraining(int userID) throws CoreException;

    public Training getTrainingByID(int trainingID) throws CoreException;

    public Training[] getTrainingsByUserID(int userID) throws CoreException;

    public Training[] getDoneTrainingsByUserID(int userID) throws CoreException;

    public Training[] getPlannedTrainingsByUserID(int userID) throws CoreException;

    public void alterTraining(Training training) throws CoreException;
    
    public void removeTraining(int trainingID) throws CoreException;
    
}
