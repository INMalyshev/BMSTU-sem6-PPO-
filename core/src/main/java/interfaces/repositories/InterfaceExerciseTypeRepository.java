package interfaces.repositories;

import model.ExerciseType;
import exceptions.CoreException;

public interface InterfaceExerciseTypeRepository {
    public ExerciseType[] getExerciseTypeList() throws CoreException;
}
