package services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import exceptions.CoreException;
import interfaces.repositories.InterfaceExerciseTypeRepository;
import interfaces.services.InterfaceExerciseTypeService;
import model.ExerciseType;

@Component
public class ExerciseTypeService implements InterfaceExerciseTypeService {

    private InterfaceExerciseTypeRepository exerciseTypeRepository;

    private Logger logger = null;

    public ExerciseTypeService(InterfaceExerciseTypeRepository exerciseTypeRepository, Logger logger) {
        this.exerciseTypeRepository = exerciseTypeRepository;
        this.logger = logger;
    }

    public ExerciseType[] getExerciseTypeList() throws CoreException {
        logger.log(Level.INFO, "unlnown user tries to get list of exercise types");

        try {
            ExerciseType[] result = this.exerciseTypeRepository.getExerciseTypeList();

            return result;

        } catch (CoreException e) {
            logger.log(Level.WARNING, "exception accured while grtting excersise type list", e);

            throw e;
        }
    }
    
}
