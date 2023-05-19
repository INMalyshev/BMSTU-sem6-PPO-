package core;

import org.mockito.Mockito;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import exceptions.CoreAccessDeniedException;
import interfaces.repositories.InterfaceUserRepository;
import interfaces.services.InterfaceUserService;
import model.Role;
import model.User;
import services.UserService;


public class UserServiseTest {
    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(CoreTestConfiguration.class);
    static private Logger logger = ctx.getBean(Logger.class);

    // createUser

    @Test
    public void CreateUserTest() throws Exception {
        int signedUserID = 13;

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.createUser()).thenReturn(signedUserID);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertEquals(signedUserID, service.createUser());
        Mockito.verify(userRepository, Mockito.times(1)).createUser();
    }

    // getUserByID

    @Test
    public void SignedUserGetsSignedUser() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertEquals(signedUser, service.getUser(signedUserID, signedUserID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(Mockito.anyInt());
    }

    @Test
    public void TrainerGetsSignedUser() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertEquals(signedUser, service.getUser(signedUserID, trainerID));
        Mockito.verify(userRepository, Mockito.times(2)).getUser(Mockito.anyInt());
    }

    @Test
    public void SignedUserGetsTrainer() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.getUser(trainerID, signedUserID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(signedUserID);
    }

    @Test
    public void SignedUserGetsOtherSignedUser() throws Exception {
        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser1", Role.SignedUser, signedUser2ID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.getUser(signedUser1ID, signedUser2ID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(signedUser2ID);
    }

    // alterUser

    @Test
    public void SignedUserAltersOtherSignedUser() throws Exception {
        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser1", Role.SignedUser, signedUser2ID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.alterUser(signedUser2, signedUser1ID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(signedUser1ID);
        Mockito.verify(userRepository, Mockito.times(0)).alterUser(Mockito.any());
    }

    @Test
    public void TrainerAltersOtherSignedUser() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.alterUser(signedUser, trainerID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(trainerID);
        Mockito.verify(userRepository, Mockito.times(0)).alterUser(Mockito.any());
    }

    // removeUser

    @Test
    public void SignedUserRemovesOtherSignedUser() throws Exception {
        int signedUser1ID = 13;
        User signedUser1 = new User("SignedUser1", Role.SignedUser, signedUser1ID);
        int signedUser2ID = 14;
        User signedUser2 = new User("SignedUser1", Role.SignedUser, signedUser2ID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUser1ID)).thenReturn(signedUser1);
        Mockito.when(userRepository.getUser(signedUser2ID)).thenReturn(signedUser2);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.removeUser(signedUser2ID, signedUser1ID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(signedUser1ID);
        Mockito.verify(userRepository, Mockito.times(0)).getUser(signedUser2ID);
    }

    @Test
    public void TrainerRemovesOtherSignedUser() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);
        int trainerID = 14;
        User trainer = new User("trainer", Role.Trainer, trainerID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        Mockito.when(userRepository.getUser(trainerID)).thenReturn(trainer);
        InterfaceUserService service = new UserService(userRepository, logger);

        assertThrows(CoreAccessDeniedException.class, () -> service.removeUser(signedUserID, trainerID));
        Mockito.verify(userRepository, Mockito.times(1)).getUser(trainerID);
        Mockito.verify(userRepository, Mockito.times(0)).removeUser(signedUserID);
    }

    @Test
    public void getSignedUsers_Test() throws Exception {
        int signedUserID = 13;
        User signedUser = new User("SignedUser", Role.SignedUser, signedUserID);

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(signedUserID)).thenReturn(signedUser);
        Mockito.when(userRepository.getUsersByRole(Role.SignedUser)).thenReturn(new User[]{signedUser});
        InterfaceUserService service = new UserService(userRepository, logger);

        assertEquals(1, service.getSignedUsers(signedUserID).length);
        Mockito.verify(userRepository, Mockito.times(1)).getUser(Mockito.anyInt());
        Mockito.verify(userRepository, Mockito.times(1)).getUsersByRole(Role.SignedUser);
    }

}
