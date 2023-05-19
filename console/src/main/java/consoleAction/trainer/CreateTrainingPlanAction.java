package consoleAction.trainer;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingPlanService;
import model.ApproachPlan;
import model.ExerciseType;

public class CreateTrainingPlanAction implements InterfaceAction {

    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingPlanService trainingPlanService;
    
    public CreateTrainingPlanAction(RuntimeProfile profile, Scanner in, InterfaceTrainingPlanService trainingPlanService) {
        this.profile = profile;
        this.in = in;
        this.trainingPlanService = trainingPlanService;
    }

    @Override
    public void perform() {
        
        Informator.blueSystemMessage("Создание плана тренировки");

        try {
            int userID = profile.getUserId();
            int trainingPlanID = trainingPlanService.createTrainingPlan(userID, userID);

            System.out.printf("id созданного плана тренировки : %d\n", trainingPlanID);

            boolean continueCreating = false;
            System.out.printf("Введи 'да', если хочешь добавить план подхода к тренировке, или что-нибудь другое, если нет: ");
            in.nextLine();
            continueCreating = in.nextLine().equals("да");
            
            while (continueCreating) {

                ExerciseType apType = ExerciseType.PullUp;

                Informator.systemMessage("Выбор типа упражнения");
                ExerciseType[] types = ExerciseType.values();
                for (int i = 0; i < types.length; i++) {
                    System.out.printf("[%d] : %s\n", i + 1, types[i].getTitle());
                }

                Informator.greenSystemMessage("Выбери номер типа упражнения");
                int typeId = InfoGetter.getPositiveIntWithLimit(in, "Введи номер", "Жду целое число соответствующее одному из пунктов", types.length);
                typeId -= 1;

                apType = types[typeId];

                Informator.greenSystemMessage("Введи ожидаемое количество");
                int apExpectedAmount = InfoGetter.getPositiveInt(in, "Введи количество", "Жду целое число соответствующее одному из пунктов");

                int approachPlanId = trainingPlanService.createApproachPlan(trainingPlanID, userID);
                ApproachPlan approachPlan = trainingPlanService.getApproachPlanByID(approachPlanId, userID);

                approachPlan.setExpectedAmount(apExpectedAmount);
                approachPlan.setType(apType);

                trainingPlanService.alterApproachPlan(approachPlan, userID);

                System.out.printf("Введи 'да', если хочешь добавить еще один план подхода к тренировке, или что-нибудь другое, если нет: ");
                in.nextLine();
                continueCreating = in.nextLine().equals("да");
            }


            Informator.greenSystemMessage("План тренировки созан успешно");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }
    
}
