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

public class PerformePlannedAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    
    public PerformePlannedAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Выполнение существующей тренировки");

        int trainingId = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительное число");
        int userId = profile.getUserId();

        try {

            Training training = trainingService.getTrainingByID(trainingId, userId);
            Approach[] approaches = trainingService.getApproachByTrainingID(trainingId, userId);
            int approachAmount = approaches.length;

            System.out.printf("Тренировка № %d ; Количество упражнений %d\n", training.getID(), approachAmount);

            for (Approach appr : approaches) {
                System.out.printf("Выполни %s ; ожидаемое количество %d\n", appr.getType().getTitle(), appr.getExpectedAmount());
                int amount = InfoGetter.getPositiveInt(in, "Введи количество", "Жду целое положительное число");
                appr.setAmount(amount);
                appr.setCompleted(true);
                trainingService.alterApproach(appr, userId);
            }

            training.SetCompleted(true);
            trainingService.alterTraining(training, userId);

        } catch (CoreException e ) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

        Informator.greenSystemMessage("Успех");
    }

}
