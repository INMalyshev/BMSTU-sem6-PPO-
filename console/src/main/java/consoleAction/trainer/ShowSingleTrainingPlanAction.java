package consoleAction.trainer;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingPlanService;
import model.ApproachPlan;
import model.TrainingPlan;

public class ShowSingleTrainingPlanAction implements InterfaceAction {

    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingPlanService trainingPlanService;
    
    public ShowSingleTrainingPlanAction(RuntimeProfile profile, Scanner in, InterfaceTrainingPlanService trainingPlanService) {
        this.profile = profile;
        this.in = in;
        this.trainingPlanService = trainingPlanService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Просмотр конкретного плана тренировки");

        try {
            int userId = profile.getUserId();
            int trainingPlanId = InfoGetter.getPositiveInt(in, "Введи id тренировки", "Жду целое положительное число");

            TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanByID(trainingPlanId, userId);
            ApproachPlan[] approachPlans = trainingPlanService.getApproachPlanByTrainingPlanID(trainingPlanId, userId);

            showTrainingPlan(trainingPlan);

            if (approachPlans.length == 0) {
                Informator.systemMessage("Пустая тренировка");
            }

            for (ApproachPlan ap : approachPlans) {
                showApproachPlan(ap);
            }

            Informator.greenSystemMessage("Конец вывода");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }

    private void showTrainingPlan(TrainingPlan trainingPlan) {

        try {
            ApproachPlan[] approachPlans = trainingPlanService.getApproachPlanByTrainingPlanID(trainingPlan.getID(), profile.getUserId());

            System.out.printf("План тренировки -> id : %d ; количество упражнений : %s\n", trainingPlan.getID(), approachPlans.length);

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }

    private void showApproachPlan(ApproachPlan approachPlan) {
        System.out.printf("    Упражнение : %s ; количество : %d\n", approachPlan.getType().getTitle(), approachPlan.getExpectedAmount());
    }

}
