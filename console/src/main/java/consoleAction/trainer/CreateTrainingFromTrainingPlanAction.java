package consoleAction.trainer;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingService;

public class CreateTrainingFromTrainingPlanAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    
    public CreateTrainingFromTrainingPlanAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Назначить тренировку");

        Informator.greenSystemMessage("Введи id тренировки, которою хочешь назначиить");
        int trainingPlanID = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительное число");
        Informator.greenSystemMessage("Введи id пользователя, которому хочешь назначиить тренировку");
        int userID = InfoGetter.getPositiveInt(in, "Введи id пользователя", "Жду целое положительное число");

        int requesterId = profile.getUserId();

        try {
            trainingService.createTraininByTrainingPlanID(userID, trainingPlanID, requesterId);

            Informator.greenSystemMessage("Успех");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }

}
