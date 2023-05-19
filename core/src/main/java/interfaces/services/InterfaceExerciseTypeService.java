package interfaces.services;

import model.ExerciseType;
import exceptions.CoreException;

public interface InterfaceExerciseTypeService {
    public ExerciseType[] getExerciseTypeList() throws CoreException;
}