package consoleAction.trainer;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingPlanService;

public class DeleteTrainingPlanAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingPlanService trainingPlanService;
    
    public DeleteTrainingPlanAction(RuntimeProfile profile, Scanner in, InterfaceTrainingPlanService trainingPlanService) {
        this.profile = profile;
        this.in = in;
        this.trainingPlanService = trainingPlanService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Удаление плана тренировки");

        int trainingPlanID = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительно число");
        int userId = profile.getUserId();

        try {
            trainingPlanService.removeTrainingPlan(trainingPlanID, userId);

            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }

}
