package interfaces.repositories;

import model.TrainingPlan;
import exceptions.CoreException;

public interface InterfaceTrainingPlanRepository {

    // Training plan

    // returns ID
    public int createTrainingPlan(int userID) throws CoreException;

    public TrainingPlan getTrainingPlanByID(int trainingPlanID) throws CoreException;

    public TrainingPlan[] getTrainingPlanByUserID(int userID) throws CoreException;

    public void alterTrainingPlan(TrainingPlan trainingPlan) throws CoreException;

    public void removeTrainingPlan(int trainingPlanID) throws CoreException;

}
