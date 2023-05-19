package consoleMenu;

import java.util.ArrayList;
import java.util.Scanner;

import consoleMenu.interfaces.InterfaceAction;
import consoleMenu.interfaces.InterfaceMenu;
import consoleUtil.ConsoleColors;
import consoleUtil.InfoGetter;

public class Menu implements InterfaceMenu {

    private static final String DELIMITER_START = "+---> ";
    private static final String DELIMITER_FINISH = " <---+";
    private static final String END = "+--->           <---+";

    private ArrayList<MenuOption> options;
    private ArrayList<MenuSection> sections; 

    private Scanner in;

    public Menu(Scanner in) {
        this.options = new ArrayList<MenuOption>();
        this.sections = new ArrayList<MenuSection>();

        this.in = in;
    }

    @Override
    public void addOption(MenuOption menuOption) {
        this.options.add(menuOption);
    }

    @Override
    public void addSection(MenuSection menuSection) {
        this.sections.add(menuSection);
    }

    @Override
    public void establish() {

        int currentOptionNumber = 0;
        ArrayList<InterfaceAction> actions = new ArrayList<InterfaceAction>();
        
        System.out.printf("%s[ Меню ]%s\n", ConsoleColors.BLUE_BOLD_BRIGHT, ConsoleColors.RESET);

        for (int i = 0; i < sections.size(); i++) {
            MenuSection ms = sections.get(i);

            System.out.printf("%s%s%s%s%s\n", DELIMITER_START, ConsoleColors.GREEN_BRIGHT, ms.getName(), ConsoleColors.RESET, DELIMITER_FINISH);

            for (MenuOption mo : ms.getOptions()) {
                actions.add(mo.getAction());
                currentOptionNumber += 1;

                printOption(currentOptionNumber, mo.getName());
            }

            if  (i == sections.size() - 1) {
                System.out.printf("%s\n", END);
            }

        }

        for (MenuOption mo : this.options) {
            actions.add(mo.getAction());
            currentOptionNumber += 1;

            printOption(currentOptionNumber, mo.getName());
        }

        if (currentOptionNumber > 0) {
            
            int choice = InfoGetter.getPositiveIntWithLimit(in, "Выбирай", "Жду число соответствующее пункту", currentOptionNumber);

            System.out.printf("[ Ты выбрал %s[%d]%s ]\n", 
                ConsoleColors.PURPLE_BRIGHT, choice, ConsoleColors.RESET);

            actions.get(choice - 1).perform();

        }
    }

    private void printOption(int id, String name) {
        System.out.printf("%s[%d]%s %s\n", ConsoleColors.PURPLE_BRIGHT, id, ConsoleColors.RESET, name);
    }
    
    
}
