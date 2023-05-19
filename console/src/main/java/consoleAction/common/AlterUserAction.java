package consoleAction.common;

import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.InfoGetter;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceUserService;
import model.User;

public class AlterUserAction implements InterfaceAction {
    
    private InterfaceUserService userService;
    private RuntimeProfile profile;
    private Scanner in;

    public AlterUserAction(Scanner in, RuntimeProfile profile, InterfaceUserService userService) {
        this.profile = profile;
        this.userService = userService;
        this.in = in;
    }

    @Override
    public void perform() {
        
        Informator.blueSystemMessage("Редактирование профиля");

        try {
            int userId = profile.getUserId();
            User user = userService.getUser(userId, userId);

            String newName = InfoGetter.getString(in, "Введи новое имя", "Имя -- это непустая строка");
            user.setName(newName);

            userService.alterUser(user, userId);

            Informator.greenSystemMessage("Успех");

        } catch (CoreException e) {
            Informator.redSystemMessage("Что-то пошло не так");
        }

    }

    

}
