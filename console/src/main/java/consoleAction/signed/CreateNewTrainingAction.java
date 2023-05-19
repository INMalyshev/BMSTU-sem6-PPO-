package consoleAction.signed;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingService;
import model.Approach;
import model.ExerciseType;

public class CreateNewTrainingAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private Scanner in;
    private InterfaceTrainingService trainingService;
    
    public CreateNewTrainingAction(RuntimeProfile profile, Scanner in, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.in = in;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {
        
        Informator.blueSystemMessage("Создание тренировки");

        int userID = profile.getUserId();

        try {
            int trainingID = trainingService.createTraining(userID, userID);

            System.out.printf("id созданной тренировки : %d\n", trainingID);

            boolean continueCreating = false;
            System.out.printf("Введи 'да', если хочешь добавить подход к тренировке, или что-нибудь другое, если нет: ");
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

                Informator.greenSystemMessage("Введи количество");
                int apAmount = InfoGetter.getPositiveInt(in, "Введи количество", "Жду целое число соответствующее одному из пунктов");

                int approachId = trainingService.createApproach(trainingID, userID);
                Approach approach = trainingService.getApproachByID(approachId, userID);

                approach.setAmount(apAmount);
                approach.setType(apType);
                approach.setCompleted(true);

                trainingService.alterApproach(approach, userID);

                System.out.printf("Введи 'да', если хочешь добавить еще один подход к тренировке, или что-нибудь другое, если нет: ");
                in.nextLine();
                continueCreating = in.nextLine().equals("да");
            }


            Informator.greenSystemMessage("План тренировки созан успешно");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }

}
