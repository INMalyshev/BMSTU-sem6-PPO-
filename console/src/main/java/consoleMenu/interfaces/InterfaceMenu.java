package consoleMenu.interfaces;

import consoleMenu.MenuOption;
import consoleMenu.MenuSection;

public interface InterfaceMenu {
    
    public void addOption(MenuOption menuOption);
    public void addSection(MenuSection menuSection);

    public void establish();

}
