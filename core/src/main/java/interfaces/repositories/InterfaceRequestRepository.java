package interfaces.repositories;

import exceptions.CoreException;
import model.Request;

public interface InterfaceRequestRepository {
    
    public int createRequest() throws CoreException;

    public Request getRequest(int requestId) throws CoreException;

    public Request[] getRequestsByTrainerId(int trainerId) throws CoreException;

    public Request[] getRequestsBySignedUserId(int userId) throws CoreException;

    public void alterRequest(Request request) throws CoreException;

    public void removeRequest(int requestId) throws CoreException;

}
