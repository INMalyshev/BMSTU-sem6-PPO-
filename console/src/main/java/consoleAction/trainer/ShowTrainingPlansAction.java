package consoleAction.trainer;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingPlanService;
import model.ApproachPlan;
import model.TrainingPlan;

public class ShowTrainingPlansAction implements InterfaceAction {

    private RuntimeProfile profile;
    private InterfaceTrainingPlanService trainingPlanService;

    public ShowTrainingPlansAction(RuntimeProfile profile, InterfaceTrainingPlanService trainingPlanService) {
        this.profile = profile;
        this.trainingPlanService = trainingPlanService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Список твоих планов тренировок");

        try {
            int userId = profile.getUserId();
            TrainingPlan[] trainingPlans = trainingPlanService.getTrainingPlanByUserID(userId, userId);

            for (TrainingPlan tp : trainingPlans) {
                showTrainingPlan(tp);
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
    
}
