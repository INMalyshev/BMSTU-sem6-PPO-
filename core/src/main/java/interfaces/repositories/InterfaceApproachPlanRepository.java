package interfaces.repositories;

import exceptions.CoreException;
import model.ApproachPlan;

public interface InterfaceApproachPlanRepository {
    
    // returns ID
    public int createApproachPlan(int trainingPlanID) throws CoreException;
    
    public ApproachPlan getApproachPlanByID(int approachPlanID) throws CoreException;
    
    public ApproachPlan[] getApproachPlanByTrainingPlanID(int trainingPlanID) throws CoreException;
    
    public void alterApproachPlan(ApproachPlan approachPlan) throws CoreException;
    
    public void removeApproachPlan(int approachPlanID) throws CoreException;
}
