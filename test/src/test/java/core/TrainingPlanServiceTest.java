package core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import interfaces.services.*;
import interfaces.repositories.*;

import services.*;
import model.*;
import exceptions.*;

import org.mockito.Mockito;

public class TrainingPlanServiceTest {

    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(CoreTestConfiguration.class);
    static private Logger logger = ctx.getBean(Logger.class);

    private static int getRandomInt() {
        Random randomizer = new Random();
        return randomizer.nextInt(java.lang.Integer.MAX_VALUE);
    }

    // createApproachPlan

    @Test
    public void signedUserCreatesApproachPlan() throws Exception {
        // arrange 
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        // act & assert
        assertThrows(CoreAccessDeniedException.class, () -> trainingPlanService.createApproachPlan(getRandomInt(), signedUserID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUserID);
    }

    @Test
    public void trainerCreatesApproachPlan() throws Exception {
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);
        int trainingPlanID = 123;
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanID, trainerID);
        int newApproachPlanID = 321;

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.getTrainingPlanByID(trainingPlanID)).thenReturn(trainingPlan);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.createApproachPlan(trainingPlanID)).thenReturn(newApproachPlanID);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertEquals(newApproachPlanID, trainingPlanService.createApproachPlan(trainingPlanID, trainerID));
        Mockito.verify(trainingPlanRepository, Mockito.times(1)).getTrainingPlanByID(trainingPlanID);
        Mockito.verify(approachPlanRepository, Mockito.times(1)).createApproachPlan(trainingPlanID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
    }

    //getApproachPlanByID

    @Test
    public void signedUserGetsApproachPlan() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);
        int trainingPlanID = 123;
        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertThrows(CoreAccessDeniedException.class, () -> trainingPlanService.getApproachPlanByID(approachPlanID, signedUserID));
        Mockito.verify(approachPlanRepository, Mockito.times(1)).getApproachPlanByID(approachPlanID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUserID);
    }

    @Test
    public void trainerGetsApproachPlan() throws Exception {
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        int trainingPlanID = 123;
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanID, trainerID);

        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.getTrainingPlanByID(trainingPlanID)).thenReturn(trainingPlan);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertEquals(approachPlan, trainingPlanService.getApproachPlanByID(approachPlanID, trainerID));
        Mockito.verify(trainingPlanRepository, Mockito.times(1)).getTrainingPlanByID(trainingPlanID);
        Mockito.verify(approachPlanRepository, Mockito.times(1)).getApproachPlanByID(approachPlanID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
    }

    @Test
    public void signedUserCreatesTrainingPlan() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        int trainingPlanID = 123;
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanID, trainerID);

        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.getTrainingPlanByID(trainingPlanID)).thenReturn(trainingPlan);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertThrows(CoreAccessDeniedException.class, () -> trainingPlanService.createTrainingPlan(signedUserID, signedUserID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUserID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(trainerID);
        Mockito.verify(approachPlanRepository, Mockito.times(0)).getApproachPlanByID(approachPlanID);
        Mockito.verify(trainingPlanRepository, Mockito.times(0)).getTrainingPlanByID(trainingPlanID);
    }

    @Test
    public void trainerCreatesTrainingPlan() throws Exception {

        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        int trainingPlanID = 123;

        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.createTrainingPlan(trainerID)).thenReturn(trainingPlanID);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertEquals(trainingPlanID, trainingPlanService.createTrainingPlan(trainerID, trainerID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
        Mockito.verify(trainingPlanRepository, Mockito.times(1)).createTrainingPlan(trainerID);
    }

    @Test
    public void signedUserGetsTrainingPlan() throws Exception {

        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        int trainingPlanID = 123;
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanID, trainerID);

        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.getTrainingPlanByID(trainingPlanID)).thenReturn(trainingPlan);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertThrows(CoreAccessDeniedException.class, () -> trainingPlanService.getTrainingPlanByID(trainingPlanID, signedUserID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUserID);
        Mockito.verify(trainingPlanRepository, Mockito.times(1)).getTrainingPlanByID(trainingPlanID);
    }

    @Test
    public void trainerGetsTrainingPlan() throws Exception {

        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        int trainingPlanID = 123;
        TrainingPlan trainingPlan = new TrainingPlan(trainingPlanID, trainerID);

        int approachPlanID = 321;
        ApproachPlan approachPlan = new ApproachPlan(trainingPlanID, null, getRandomInt(), approachPlanID);

        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        Mockito.when(trainingPlanRepository.getTrainingPlanByID(trainingPlanID)).thenReturn(trainingPlan);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        Mockito.when(approachPlanRepository.getApproachPlanByID(approachPlanID)).thenReturn(approachPlan);
        
        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);

        InterfaceUserService userService = new UserService(userRepository, logger);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        assertEquals(trainingPlan, trainingPlanService.getTrainingPlanByID(trainingPlanID, trainerID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
        Mockito.verify(trainingPlanRepository, Mockito.times(1)).getTrainingPlanByID(trainingPlanID);
    }

}
