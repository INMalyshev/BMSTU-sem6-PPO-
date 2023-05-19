package consoleAction.signed;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceTrainingService;
import model.Training;

public class GetAllUserTrainingsAction implements InterfaceAction {
    
    private RuntimeProfile profile;
    private InterfaceTrainingService trainingService;
    
    public GetAllUserTrainingsAction(RuntimeProfile profile, InterfaceTrainingService trainingService) {
        this.profile = profile;
        this.trainingService = trainingService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Полоучить все тренировки пользователя");

        int userId = profile.getUserId();

        try {

            Training[] trainings = trainingService.getTrainingsByUserID(userId, userId);
            
            for (Training training : trainings) {

                int trainingId = training.getID();
                int approachAmount = trainingService.getApproachByTrainingID(trainingId, userId).length;

                System.out.printf("Тренировка № %d ; пользователя № %d ; завершена %b ; Количество упражнений %d\n", training.getID(), training.GetHolderUserID(), training.GetCompleted(), approachAmount);

            }

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

        Informator.greenSystemMessage("Успех");
    }

}
