package core;

import java.util.logging.Logger;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import interfaces.repositories.*;;

@Configuration
public class CoreTestConfiguration {
    
    @Bean
    public Logger logger() {
        return Mockito.mock(Logger.class);
    }

    @Bean
    @Scope("prototype")
    public InterfaceUserRepository interfaceUserRepository() {

        return Mockito.mock(InterfaceUserRepository.class);

    }

    @Bean
    @Scope("prototype")
    public InterfaceExerciseTypeRepository interfaceExerciseTypeRepository() {

        return Mockito.mock(InterfaceExerciseTypeRepository.class);
    }

    @Bean
    @Scope("prototype")
    public InterfaceApproachRepository interfaceApproachRepository() {

        return Mockito.mock(InterfaceApproachRepository.class);

    }

    @Bean
    @Scope("prototype")
    public InterfaceTrainingRepository interfaceTrainingRepository() {

        return Mockito.mock(InterfaceTrainingRepository.class);

    }

    @Bean
    @Scope("prototype")
    public InterfaceApproachPlanRepository interfaceApproachPlanRepository() {

        return Mockito.mock(InterfaceApproachPlanRepository.class);

    }

    @Bean
    @Scope("prototype")
    public InterfaceTrainingPlanRepository interfaceTrainingPlanRepository() {

        return Mockito.mock(InterfaceTrainingPlanRepository.class);

    }

    @Bean
    @Scope("prototype")
    public InterfaceRequestRepository interfaceRequestRepository() {

        return Mockito.mock(InterfaceRequestRepository.class);

    }

}
