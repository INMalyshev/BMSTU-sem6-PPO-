package consoleAction.signed;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceRequestService;
import model.Request;

public class RemoveRequestAction implements InterfaceAction {

    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceRequestService requestService;

    public RemoveRequestAction(RuntimeProfile profile, Scanner in, InterfaceRequestService requestService) {
        this.profile = profile;
        this.in = in;
        this.requestService = requestService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Удалить запрос на создание тренировки");

        int requestId = InfoGetter.getPositiveInt(in, "Введи id запроса", "Жду целое положительное число");

        try {
            Request request = requestService.getRequest(requestId, profile.getUserId());

            if (request == null) {
                throw new CoreException();
            }

            requestService.removeRequest(requestId, profile.getUserId());

            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }
    
}
