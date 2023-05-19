package pgdataRepositories;

import exceptions.CoreException;
import interfaces.repositories.InterfaceExerciseTypeRepository;
import model.ExerciseType;

public class ExerciseTypeRepository implements InterfaceExerciseTypeRepository {

    @Override
    public ExerciseType[] getExerciseTypeList() throws CoreException {
        return ExerciseType.values();
    }
    
}
