import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import webControllers.TrainerPartController;
import webControllers.UserPartController;
import webControllers.AuthController;

@SpringBootApplication(
    scanBasePackageClasses = {TrainerPartController.class, AuthController.class, UserPartController.class}
    )
@Import(WebConfiguration.class)
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
