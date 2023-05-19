import java.util.Map;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;

import consoleMenu.Menu;
import consoleMenu.MenuOption;
import consoleMenu.MenuSection;
import consoleMenu.interfaces.InterfaceMenu;
import consoleMenu.interfaces.InterfaceMenuBuilder;
import consoleModel.RuntimeProfile;
import consoleUtil.Informator;
import exceptions.CoreException;
import interfaces.services.InterfaceUserService;
import model.Role;
import model.User;

public class MenuBuilder implements InterfaceMenuBuilder {

    private ApplicationContext ctx;

    public MenuBuilder(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public InterfaceMenu buildMenu() {

        RuntimeProfile profile = ctx.getBean(RuntimeProfile.class);
        Map<String, MenuOption> menuOptions = ctx.getBeansOfType(MenuOption.class);

        InterfaceMenu menu = new Menu(ctx.getBean(Scanner.class));

        if (profile.isAuthorized()) {
            
            MenuSection userSection = new MenuSection("Для всех авторизованных");
            userSection.addOption(menuOptions.get("showUserInfoOption"));
            userSection.addOption(menuOptions.get("alterUserOption"));
            userSection.addOption(menuOptions.get("logOutOption"));

            menu.addSection(userSection);

            try {

                User user = ctx.getBean(InterfaceUserService.class).getUser(profile.getUserId(), profile.getUserId());

                if (user.getRole() == Role.Trainer) {
                    MenuSection trainerSection = new MenuSection("Для тренера");
                    trainerSection.addOption(menuOptions.get("showTrainingPlansOption"));
                    trainerSection.addOption(menuOptions.get("showSingleTrainingOption"));
                    trainerSection.addOption(menuOptions.get("createTrainingPlanOption"));
                    trainerSection.addOption(menuOptions.get("createTrainingFromTrainingPlanOption"));
                    trainerSection.addOption(menuOptions.get("deleteTrainingPlanOption"));
                    trainerSection.addOption(menuOptions.get("showTrainerRequestsOption"));
                    trainerSection.addOption(menuOptions.get("satisfyRequestOption"));

                    menu.addSection(trainerSection);
                }

                if (user.getRole() == Role.SignedUser) {
                    MenuSection signedUserSection = new MenuSection("Для авторизованного пользователя");
                    signedUserSection.addOption(menuOptions.get("getTrainingByIdOption"));
                    signedUserSection.addOption(menuOptions.get("getAllUserTrainingsOption"));
                    signedUserSection.addOption(menuOptions.get("getDoneUserTrainingsOption"));
                    signedUserSection.addOption(menuOptions.get("getPlannedUserTrainingsOption"));
                    signedUserSection.addOption(menuOptions.get("createNewTrainingOption"));
                    signedUserSection.addOption(menuOptions.get("performePlannedOption"));
                    signedUserSection.addOption(menuOptions.get("deleteTrainingByIdOption"));
                    signedUserSection.addOption(menuOptions.get("createRequestOption"));
                    signedUserSection.addOption(menuOptions.get("showWaitingRequestsOption"));
                    signedUserSection.addOption(menuOptions.get("removeRequestOption"));

                    menu.addSection(signedUserSection);
                }


            } catch (CoreException e) {
                Informator.redSystemMessage("Не удалось построить меню -- фатальная ошибка");
                Informator.redSystemMessage("Выход");
                System.exit(1);
            }

        } else {
            
            MenuSection loginSection = new MenuSection("Авторизация");
            loginSection.addOption(menuOptions.get("enterIdOption"));
            loginSection.addOption(menuOptions.get("createUserOption"));

            menu.addSection(loginSection);
        }

        menu.addOption(menuOptions.get("exitOption"));

        return menu;

    }
    
}
