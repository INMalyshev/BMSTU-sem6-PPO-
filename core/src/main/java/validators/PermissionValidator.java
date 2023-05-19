package validators;

import exceptions.*;
import model.*;

public class PermissionValidator {

    public static void validateEqualIDs(int intentionID, int requesterID) throws CoreException {
        if (intentionID != requesterID) {
            throw new CoreAccessDeniedException("Identifier mismatch: requesterID - " + requesterID + ", intentionID - " + intentionID);
        }
    }

    public static void validateSignedUserObjectGetAccess(User requester, int intentionUserID) throws CoreException {
        if (requester.getRole() != Role.Trainer && requester.getID() != intentionUserID) {
            throw new CoreAccessDeniedException("User service GET " + requester.getRole().getTitle() + " access denied.");
        }
    }

    // user service

    public static void validateUserServiceAccess(User requester) throws CoreException {

    }

    // training plan service

    public static void validateTrainingPlanServiceAccess(User requester) throws CoreException {
        if (requester.getRole() != Role.Trainer) {
            throw new CoreAccessDeniedException("Plan service " + requester.getRole().getTitle() + " access denied.");
        }
    }

    // training service

    public static void validateTrainingServiceAccess(User requester) throws CoreException {

    }

    // exercise type service

    public static void validateExerciseTypeServiceAccess(User requester) throws CoreException {

    }

    // request service

    public static void validateRequestServiceSignedUserAccess(User requester) throws CoreException {
        if (requester.getRole() != Role.SignedUser) {
            throw new CoreAccessDeniedException("Request service (signed user side): " + requester.getRole().getTitle() + " access denied.");
        }
    }

    public static void validateRequestServiceTrainerAccess(User requester) throws CoreException {
        if (requester.getRole() != Role.Trainer) {
            throw new CoreAccessDeniedException("Request service (trainer side): " + requester.getRole().getTitle() + " access denied.");
        }
    }

}
