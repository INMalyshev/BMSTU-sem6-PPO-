package core;

import java.util.logging.Logger;

import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import interfaces.repositories.InterfaceRequestRepository;
import interfaces.repositories.InterfaceUserRepository;
import interfaces.services.InterfaceRequestService;
import interfaces.services.InterfaceUserService;
import model.Request;
import model.Role;
import model.User;
import services.RequestService;
import services.UserService;

public class RequestServiceTest {
    
    static private ApplicationContext ctx = new AnnotationConfigApplicationContext(CoreTestConfiguration.class);
    static private Logger logger = ctx.getBean(Logger.class);

    // create request

    @Test
    public void createRequestTest() throws Exception{
        int requestId = 1;
        int userId = 2;

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(userId)).thenReturn(new User("", Role.SignedUser, userId));
        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);
        Mockito.when(requestRepository.createRequest()).thenReturn(requestId);
        InterfaceRequestService service = new RequestService(requestRepository, userService, logger);

        assertEquals(requestId, service.createRequest(userId));
        Mockito.verify(requestRepository, Mockito.times(1)).createRequest();
    }

    // get request

    @Test
    public void getRequestByIdTest() throws Exception{
        int requestId = 1;
        int userId = 2;

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);
        Mockito.when(requestRepository.getRequest(requestId)).thenReturn(new Request(requestId, userId, userId, "", false, null));
        InterfaceRequestService service = new RequestService(requestRepository, userService, logger);

        assertEquals(requestId, service.getRequest(requestId, userId).getID());
        Mockito.verify(requestRepository, Mockito.times(1)).getRequest(requestId);
    }

    // get request by trainer id

    @Test
    public void getRequestByTrainerIdTest() throws Exception{
        int userId = 2;

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(userId)).thenReturn(new User("", Role.Trainer, userId));
        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);
        Mockito.when(requestRepository.getRequestsByTrainerId(userId)).thenReturn(new Request[]{});
        InterfaceRequestService service = new RequestService(requestRepository, userService, logger);

        assertEquals(0, service.getRequestsByTrainerId(userId, userId).length);
        Mockito.verify(requestRepository, Mockito.times(1)).getRequestsByTrainerId(userId);
    }

    // get request by signed user id

    @Test
    public void getRequestBySignedUserIdTest() throws Exception{
        int userId = 2;

        InterfaceUserRepository userRepository = ctx.getBean(InterfaceUserRepository.class);
        Mockito.when(userRepository.getUser(userId)).thenReturn(new User("", Role.SignedUser, userId));
        InterfaceUserService userService = new UserService(userRepository, logger);

        InterfaceRequestRepository requestRepository = ctx.getBean(InterfaceRequestRepository.class);
        Mockito.when(requestRepository.getRequestsBySignedUserId(userId)).thenReturn(new Request[]{});
        InterfaceRequestService service = new RequestService(requestRepository, userService, logger);

        assertEquals(0, service.getRequestsBySignedUserId(userId, userId).length);
        Mockito.verify(requestRepository, Mockito.times(1)).getRequestsBySignedUserId(userId);
    }

}
