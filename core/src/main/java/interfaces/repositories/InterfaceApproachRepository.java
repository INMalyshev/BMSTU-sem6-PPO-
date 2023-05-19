package interfaces.repositories;

import model.Approach;
import exceptions.CoreException;

public interface InterfaceApproachRepository {

    // returns approachID
    public int createApproach(int trainingID) throws CoreException;

    public Approach getApproachByID(int approachID) throws CoreException;

    public Approach[] getApproachByTrainingID(int trainingID) throws CoreException;

    public void alterApproach(Approach approach) throws CoreException;

    public void removeApproach(int approachID) throws CoreException;
    
}
