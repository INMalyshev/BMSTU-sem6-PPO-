package consoleAction.signed;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingService;

public class DeleteTrainingByIdAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    
    public DeleteTrainingByIdAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Удаление плана тренировки");

        int trainingID = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительно число");
        int userId = profile.getUserId();

        try {
            trainingService.removeTraining(trainingID, userId);

            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }

}
