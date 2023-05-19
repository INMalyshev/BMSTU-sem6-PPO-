package consoleAction.usigned;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceUserService;

public class EnterUserIdAction implements InterfaceAction {

    private RuntimeProfile profile;
    private InterfaceUserService userService;
    private Scanner in;

    public EnterUserIdAction(Scanner in, RuntimeProfile profile, InterfaceUserService userService) {
        this.profile = profile;
        this.userService = userService;

        this.in = in;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Авторизация");

        // InfoGetter.removeBlankLine();        
        int userId = InfoGetter.getPositiveInt(in, "Введи свой id", "id - положительное целое число");

        try {
            userService.getUser(userId, userId);
            this.profile.setUserId(userId);
            this.profile.setAuthorized(true);
            Informator.greenSystemMessage("Успех");
        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошлл не так");
        }

    }
    
    
}
