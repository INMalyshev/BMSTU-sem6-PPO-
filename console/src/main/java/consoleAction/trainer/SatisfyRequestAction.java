package consoleAction.trainer;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceRequestService;
import interfaces.services.InterfaceTrainingPlanService;
import interfaces.services.InterfaceTrainingService;
import interfaces.services.InterfaceUserService;
import model.Request;
import model.TrainingPlan;
import model.User;


public class SatisfyRequestAction implements InterfaceAction {

    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    private InterfaceTrainingPlanService trainingPlanService;
    private InterfaceUserService userService;
    private InterfaceRequestService requestService;

    

    public SatisfyRequestAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService,
            InterfaceTrainingPlanService trainingPlanService, InterfaceUserService userService,
            InterfaceRequestService requestService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
        this.trainingPlanService = trainingPlanService;
        this.userService = userService;
        this.requestService = requestService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Выполни запрос пользователя");

        int requestId = InfoGetter.getPositiveInt(in, "Введи id запроса", "Жду целое положительно число");

        try {
            Request request = requestService.getRequest(requestId, profile.getUserId());

            if (request.getUserFromId() == 0) {
                throw new CoreException();
            }

            int trainingPlanID = InfoGetter.getPositiveInt(in, "Введи id плана тренировки", "Жду целое положительно число");

            User userFrom = userService.getUser(request.getUserFromId(), request.getUserFromId());
            TrainingPlan plan = trainingPlanService.getTrainingPlanByID(trainingPlanID, profile.getUserId());

            if (userFrom == null | plan == null) {
                throw new CoreException();
            }

            trainingService.createTraininByTrainingPlanID(request.getUserFromId(), trainingPlanID, profile.getUserId());

            request.setSatisfied(true);

            requestService.alterRequest(request, profile.getUserId());

            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }
    
}
