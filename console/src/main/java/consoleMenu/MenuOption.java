package consoleMenu;

import consoleMenu.interfaces.InterfaceAction;

public class MenuOption {

    private InterfaceAction action;
    private String name;

    public MenuOption(InterfaceAction action, String name) {
        this.action = action;
        this.name = name;
    }

    public InterfaceAction getAction() {
        return action;
    }

    public void setAction(InterfaceAction action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
