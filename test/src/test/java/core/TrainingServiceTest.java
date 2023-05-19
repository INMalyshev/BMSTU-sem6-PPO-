package core;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import exceptions.CoreAccessDeniedException;
import exceptions.CoreException;

import interfaces.repositories.*;
import interfaces.services.*;

import services.*;
import model.*;

public class TrainingServiceTest {

    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(CoreTestConfiguration.class);
    static private Logger logger = ctx.getBean(Logger.class);

    private static int getRandomInt() {
        Random randomizer = new Random();
        return randomizer.nextInt(java.lang.Integer.MAX_VALUE);
    }

    // createApproach

    @Test
    public void SignedUserCreatesApproach() throws CoreException {
        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser2", Role.SignedUser, signedUser2ID);
        int trainerID = 15;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);

        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        int trainingID = 123;
        Training training = new Training(trainingID, signedUser1ID);

        InterfaceTrainingRepository trainingRepository = ctx.getBean(InterfaceTrainingRepository.class);
        Mockito.when(trainingRepository.getTrainingByID(trainingID)).thenReturn(training);

        int approachID = 123123;
        Approach approach = new Approach(approachID, trainingID, getRandomInt(), getRandomInt(), null);

        InterfaceApproachRepository approachRepository = ctx.getBean(InterfaceApproachRepository.class);
        Mockito.when(approachRepository.getApproachByID(approachID)).thenReturn(approach);
        Mockito.when(approachRepository.createApproach(trainingID)).thenReturn(approachID);

        InterfaceTrainingService trainingService = new TrainingService(trainingRepository, approachRepository, trainingPlanService, userService, logger);

        assertEquals(approachID, trainingService.createApproach(trainingID, signedUser1ID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUser1ID);
        Mockito.verify(trainingRepository, Mockito.times(1)).getTrainingByID(trainingID);
        Mockito.verify(approachRepository, Mockito.times(1)).createApproach(trainingID);
    }

    @Test
    public void TrainerCreatesApproach() throws CoreException {
        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser2", Role.SignedUser, signedUser2ID);
        int trainerID = 15;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);

        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        int trainingID = 123;
        Training training = new Training(trainingID, signedUser1ID);

        InterfaceTrainingRepository trainingRepository = ctx.getBean(InterfaceTrainingRepository.class);
        Mockito.when(trainingRepository.getTrainingByID(trainingID)).thenReturn(training);

        int approachID = 123123;
        Approach approach = new Approach(approachID, trainingID, getRandomInt(), getRandomInt(), null);

        InterfaceApproachRepository approachRepository = ctx.getBean(InterfaceApproachRepository.class);
        Mockito.when(approachRepository.getApproachByID(approachID)).thenReturn(approach);
        Mockito.when(approachRepository.createApproach(trainingID)).thenReturn(approachID);

        InterfaceTrainingService trainingService = new TrainingService(trainingRepository, approachRepository, trainingPlanService, userService, logger);

        assertThrows(CoreException.class, () -> trainingService.createApproach(trainingID, trainerID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
        Mockito.verify(trainingRepository, Mockito.times(1)).getTrainingByID(trainingID);
        Mockito.verify(approachRepository, Mockito.times(0)).createApproach(trainingID);
    }

    // getApproach

    @Test
    public void SignedUserGetsSignedUserApproach() throws CoreException {

        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser2", Role.SignedUser, signedUser2ID);
        int trainerID = 15;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);

        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        int trainingID = 123;
        Training training = new Training(trainingID, signedUser1ID);

        InterfaceTrainingRepository trainingRepository = ctx.getBean(InterfaceTrainingRepository.class);
        Mockito.when(trainingRepository.getTrainingByID(trainingID)).thenReturn(training);

        int approachID = 123123;
        Approach approach = new Approach(approachID, trainingID, getRandomInt(), getRandomInt(), null);

        InterfaceApproachRepository approachRepository = ctx.getBean(InterfaceApproachRepository.class);
        Mockito.when(approachRepository.getApproachByID(approachID)).thenReturn(approach);

        InterfaceTrainingService trainingService = new TrainingService(trainingRepository, approachRepository, trainingPlanService, userService, logger);

        assertEquals(approach, trainingService.getApproachByID(approachID, signedUser1ID));
        Mockito.verify(trainingRepository, Mockito.times(1)).getTrainingByID(trainingID);
        Mockito.verify(approachRepository, Mockito.times(1)).getApproachByID(approachID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(trainerID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUser1ID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(signedUser2ID);
    }

    @Test
    public void SignedUserGetsOtherSignedUserApproach() throws CoreException {

        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser2", Role.SignedUser, signedUser2ID);
        int trainerID = 15;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);

        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        int trainingID = 123;
        Training training = new Training(trainingID, signedUser1ID);

        InterfaceTrainingRepository trainingRepository = ctx.getBean(InterfaceTrainingRepository.class);
        Mockito.when(trainingRepository.getTrainingByID(trainingID)).thenReturn(training);

        int approachID = 123123;
        Approach approach = new Approach(approachID, trainingID, getRandomInt(), getRandomInt(), null);

        InterfaceApproachRepository approachRepository = ctx.getBean(InterfaceApproachRepository.class);
        Mockito.when(approachRepository.getApproachByID(approachID)).thenReturn(approach);

        InterfaceTrainingService trainingService = new TrainingService(trainingRepository, approachRepository, trainingPlanService, userService, logger);

        assertThrows(CoreAccessDeniedException.class, () -> trainingService.getApproachByID(approachID, signedUser2ID));
        Mockito.verify(trainingRepository, Mockito.times(1)).getTrainingByID(trainingID);
        Mockito.verify(approachRepository, Mockito.times(1)).getApproachByID(approachID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(trainerID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(signedUser1ID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(signedUser2ID);
    }

    @Test
    public void TrainerGetsSignedUserApproach() throws CoreException {

        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser2", Role.SignedUser, signedUser2ID);
        int trainerID = 15;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);

        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceApproachPlanRepository approachPlanRepository = ctx.getBean(InterfaceApproachPlanRepository.class);
        InterfaceTrainingPlanRepository trainingPlanRepository = ctx.getBean(InterfaceTrainingPlanRepository.class);
        InterfaceTrainingPlanService trainingPlanService = new TrainingPlanService(trainingPlanRepository, approachPlanRepository, userService, logger);

        int trainingID = 123;
        Training training = new Training(trainingID, signedUser1ID);

        InterfaceTrainingRepository trainingRepository = ctx.getBean(InterfaceTrainingRepository.class);
        Mockito.when(trainingRepository.getTrainingByID(trainingID)).thenReturn(training);

        int approachID = 123123;
        Approach approach = new Approach(approachID, trainingID, getRandomInt(), getRandomInt(), null);

        InterfaceApproachRepository approachRepository = ctx.getBean(InterfaceApproachRepository.class);
        Mockito.when(approachRepository.getApproachByID(approachID)).thenReturn(approach);

        InterfaceTrainingService trainingService = new TrainingService(trainingRepository, approachRepository, trainingPlanService, userService, logger);

        assertEquals(approach, trainingService.getApproachByID(approachID, trainerID));
        Mockito.verify(trainingRepository, Mockito.times(1)).getTrainingByID(trainingID);
        Mockito.verify(approachRepository, Mockito.times(1)).getApproachByID(approachID);
        Mockito.verify(userRepository, Mockito.times(2)).getUser(trainerID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(signedUser1ID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(signedUser2ID);
    }

}
