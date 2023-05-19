package consoleMenu;

import java.util.ArrayList;

public class MenuSection {
    
    private String name;
    private ArrayList<MenuOption> options;

    public MenuSection(String name) {
        this.name = name;
        this.options = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void addOption(MenuOption mo) {
        this.options.add(mo);
    }

    public MenuOption[] getOptions() {
        return this.options.toArray(new MenuOption[]{});
    }

}
