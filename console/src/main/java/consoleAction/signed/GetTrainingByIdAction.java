package consoleAction.signed;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingService;
import model.Approach;
import model.Training;

public class GetTrainingByIdAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    
    public GetTrainingByIdAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {

        Informator.blueSystemMessage("Вывод информации о тренировке");

        int userId = profile.getUserId();
        int trainingId = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительно число");

        try {

            Training training = trainingService.getTrainingByID(trainingId, userId);
            Approach[] approaches = trainingService.getApproachByTrainingID(trainingId, userId);

            System.out.printf("Тренировка № %d ; пользователя № %d ; завершена %b\n", training.getID(), training.GetHolderUserID(), training.GetCompleted());

            if (approaches.length == 0) {
                Informator.systemMessage("Подходов нет");
            }

            for (Approach apr : approaches) {
                System.out.printf("    Подход № %d ; тренировки № %d ; Фактическое количество : %d ; Ожидаемое количество : %d ; Тип : %s\n", apr.getID(), apr.GetTrainingID(), apr.getAmount(), apr.getExpectedAmount(), apr.getType().getTitle());
            }

            Informator.greenSystemMessage("Успех");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }

}
