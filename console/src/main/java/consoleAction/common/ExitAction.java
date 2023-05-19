package consoleAction.common;

import consoleMenu.interfaces.InterfaceAction;
import consoleUtil.ConsoleColors;

public class ExitAction implements InterfaceAction {

    @Override
    public void perform() {
        System.out.printf("%s[ Пока ]%s\n", ConsoleColors.GREEN_BOLD_BRIGHT, ConsoleColors.RESET);

        System.exit(0);
    }
    
}
