package consoleAction.trainer;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceRequestService;
import model.Request;

public class ShowTrainerRequestsAction implements InterfaceAction {

    private RuntimeProfile profile;
    private InterfaceRequestService requestService;

    public ShowTrainerRequestsAction(RuntimeProfile profile, InterfaceRequestService requestService) {
        this.profile = profile;
        this.requestService = requestService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Список адресованных тебе запросов на тренировку");

        try {
            int userId = profile.getUserId();
            Request[] requests = requestService.getRequestsByTrainerId(userId, userId);

            if (0 == requests.length) {
                System.out.printf("Запрос нет!\n");
            }

            for (Request r : requests) {
                showRequest(r);
            }

            Informator.greenSystemMessage("Конец вывода");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }

    private void showRequest(Request request) {

        System.out.printf("Запрос                     id : %d\n", request.getID());
        System.out.printf("    От пользователя           : %d\n", request.getUserFromId());
        System.out.printf("    Последнее обновление      : %s\n", request.getChanged());
        System.out.printf("    Сообщение от пользователя : %s\n", request.getMessage());
        System.out.printf("\n");

    }
    
}
