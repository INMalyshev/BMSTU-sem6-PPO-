package core;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import interfaces.repositories.InterfaceExerciseTypeRepository;
import model.ExerciseType;
import services.ExerciseTypeService;

public class ExerciseTypeServiceTest {
    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(CoreTestConfiguration.class);
    static private Logger logger = ctx.getBean(Logger.class);

    @Test
    public void GetExerciseTypeListTest() throws Exception {

        InterfaceExerciseTypeRepository exerciseTypeRepository = ctx.getBean(InterfaceExerciseTypeRepository.class);
        Mockito.when(exerciseTypeRepository.getExerciseTypeList()).thenReturn(ExerciseType.values());

        ExerciseTypeService service = new ExerciseTypeService(exerciseTypeRepository, logger);

        ExerciseType[] result = service.getExerciseTypeList();

        Mockito.verify(exerciseTypeRepository, Mockito.times(1)).getExerciseTypeList();
        assertArrayEquals(ExerciseType.values(), result);
    }
    
}
