package consoleAction.common;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceUserService;
import model.User;

public class ShowUserInfoAction implements InterfaceAction {

    private RuntimeProfile profile;
    private InterfaceUserService userService;

    public ShowUserInfoAction(RuntimeProfile profile, InterfaceUserService userService) {
        this.profile = profile;
        this.userService = userService;
    }

    @Override
    public void perform() {
        Informator.blueSystemMessage("Информация о пользователе");

        if (profile.isAuthorized()) {
            try {
                int userId = profile.getUserId();
                User user = userService.getUser(userId, userId);

                System.out.printf("id   : %d\n", user.getID());
                System.out.printf("Имя  : %s\n", user.getName());
                System.out.printf("Роль : %s\n", user.getRole().getTitle());

            } catch (CoreException e) {
                Informator.redSystemMessage("Что-то пошло не так");
            }
        } else {
            Informator.redSystemMessage("Пользователь не авторизирован");
        }
    }
    
    
}
