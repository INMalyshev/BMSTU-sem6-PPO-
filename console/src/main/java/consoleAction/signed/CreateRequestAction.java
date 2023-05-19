package consoleAction.signed;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceRequestService;
import interfaces.services.InterfaceUserService;
import model.Request;
import model.User;

public class CreateRequestAction implements InterfaceAction {

    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceUserService userService;
    private InterfaceRequestService requestService;

    public CreateRequestAction(RuntimeProfile profile, Scanner in, InterfaceUserService userService,
            InterfaceRequestService requestService) {
        this.profile = profile;
        this.in = in;
        this.userService = userService;
        this.requestService = requestService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Создание запроса на получение тренировки");

        int trainerId = InfoGetter.getPositiveInt(in, "Введи id трениера", "Жду целое положительно число");

        try {
            User trainer = userService.getUser(trainerId, trainerId);

            if (trainer == null) {
                throw new CoreException();
            }

            String mes = InfoGetter.getString(in, "Введи сообщение тренеру", "Жду непустую строку");

            int requestId = requestService.createRequest(profile.getUserId());
            Request request = requestService.getRequest(requestId, profile.getUserId());

            request.setMessage(mes);
            request.setSatisfied(false);
            request.setUserFromId(profile.getUserId());
            request.setUserToId(trainerId);
            request.updateChanged();

            requestService.alterRequest(request, profile.getUserId());

            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }
    
}
