package consoleUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InfoGetter {
    
    public static int getPositiveIntWithLimit(Scanner in, String message, String errorMessahe, int limit) {
        boolean askAgin = true;
        boolean failed = false;

        int choice = 0;

        while (askAgin) {
            askAgin = false;

            if (failed) {
                System.out.printf("%s[ %s ]%s ", ConsoleColors.RED_BOLD_BRIGHT, errorMessahe, ConsoleColors.RESET);
            }

            System.out.printf("%s[ %s ]%s ", ConsoleColors.GREEN_BOLD_BRIGHT, message, ConsoleColors.RESET);

            try {

                choice = in.nextInt();

                if (choice < 1 | choice > limit) {
                    askAgin = true;
                }

            } catch (InputMismatchException e) {
                askAgin = true;
                in.nextLine();
            } finally {
                failed = true;
            }
        }

        return choice;
    }

    public static int getPositiveInt(Scanner in, String message, String errorMessahe) {
        boolean askAgin = true;
        boolean failed = false;

        int choice = 0;

        while (askAgin) {
            askAgin = false;

            if (failed) {
                System.out.printf("%s[ %s ]%s ", ConsoleColors.RED_BOLD_BRIGHT, errorMessahe, ConsoleColors.RESET);
            }

            System.out.printf("%s[ %s ]%s ", ConsoleColors.GREEN_BOLD_BRIGHT, message, ConsoleColors.RESET);

            try {
                choice = in.nextInt();

                if (choice < 1) {
                    askAgin = true;
                }

            } catch (InputMismatchException e) {
                askAgin = true;
                in.nextLine();
            } finally {
                failed = true;
            }
        }

        return choice;
    }

    public static String getString(Scanner in, String message, String errorMessahe) {
        boolean askAgin = true;
        boolean failed = false;

        String choice = "";

        while (askAgin) {
            askAgin = false;

            if (failed) {
                System.out.printf("%s[ %s ]%s ", ConsoleColors.RED_BOLD_BRIGHT, errorMessahe, ConsoleColors.RESET);
            }

            System.out.printf("%s[ %s ]%s ", ConsoleColors.GREEN_BOLD_BRIGHT, message, ConsoleColors.RESET);

            try {

                if (in.hasNextLine()) {
                    in.nextLine();
                }

                choice = in.nextLine();

                if (choice.length() < 1) {
                    askAgin = true;
                }

            } catch (InputMismatchException e) {
                askAgin = true;
                in.nextLine();
            } finally {
                failed = true;
            }
        }

        return choice;
    }

}
