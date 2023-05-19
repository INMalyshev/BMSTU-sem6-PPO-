package interfaces.services;

import exceptions.CoreException;
import model.Request;

public interface InterfaceRequestService {
    
    public int createRequest(int requesterUserID) throws CoreException;

    public Request getRequest(int requestId, int requesterUserID) throws CoreException;

    public Request[] getRequestsByTrainerId(int trainerId, int requesterUserID) throws CoreException;

    public Request[] getRequestsBySignedUserId(int userId, int requesterUserID) throws CoreException;

    public void alterRequest(Request request, int requesterUserID) throws CoreException;

    public void removeRequest(int requestId, int requesterUserID) throws CoreException;

}
