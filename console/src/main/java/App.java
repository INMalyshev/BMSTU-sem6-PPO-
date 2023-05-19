import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(ConsoleConfiguration.class);
    
    public static void main(String[] args) {
        
        MenuBuilder builder = new MenuBuilder(ctx);

        while (true) {
            builder.buildMenu().establish();
        }

    }

}
