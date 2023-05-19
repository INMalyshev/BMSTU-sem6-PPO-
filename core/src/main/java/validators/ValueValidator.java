package validators;

import exceptions.*;

public class ValueValidator {
    
    public static void validateNotNegativeInt(int value) throws CoreException {
        if (value < 0) {
            throw new CoreValueException();
        }
    }

    public static void validateNotNull(Object instance) throws CoreException {
        if (instance == null) {
            throw new CoreAccessDeniedException("Null object accured.");
        }
    }

}
