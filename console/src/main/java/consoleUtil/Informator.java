package consoleUtil;

public class Informator {
    
    public static void blueSystemMessage(String message) {
        System.out.printf("%s[ %s ]%s\n", ConsoleColors.BLUE_BOLD_BRIGHT, message, ConsoleColors.RESET);
    }

    public static void redSystemMessage(String message) {
        System.out.printf("%s[ %s ]%s\n", ConsoleColors.RED_BOLD_BRIGHT, message, ConsoleColors.RESET);
    }

    public static void greenSystemMessage(String message) {
        System.out.printf("%s[ %s ]%s\n", ConsoleColors.GREEN_BOLD_BRIGHT, message, ConsoleColors.RESET);
    }

    public static void systemMessage(String message) {
        System.out.printf("[ %s ]\n", message);
    }

}
