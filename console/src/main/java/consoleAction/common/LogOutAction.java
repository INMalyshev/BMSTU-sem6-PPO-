package consoleAction.common;

import consoleMenu.interfaces.InterfaceAction;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;

public class LogOutAction implements InterfaceAction {
    
    private RuntimeProfile profile;

    public LogOutAction(RuntimeProfile profile) {
        this.profile = profile;
    }

    @Override
    public void perform() {

        Informator.blueSystemMessage(" Выход из аккаунта ");

        this.profile.setUserId(-1);
        this.profile.setAuthorized(false);

        Informator.greenSystemMessage(" Успех ");
    }

}
