package consoleAction.usigned;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceUserService;

public class CreateUserAction implements InterfaceAction {

    private InterfaceUserService userService;
    private RuntimeProfile profile;
    
    public CreateUserAction(InterfaceUserService userService, RuntimeProfile profile) {
        this.userService = userService;
        this.profile = profile;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Создание пользователя");

        try {
            int userId = userService.createUser();

            System.out.printf("id нового пользователя : %s\n", userId);

            profile.setUserId(userId);
            profile.setAuthorized(true);

            Informator.systemMessage("Выполнена авторизация пользователя");
            
            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }
    }
    
}
