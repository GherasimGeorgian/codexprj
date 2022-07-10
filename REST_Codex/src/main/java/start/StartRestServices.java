package start;

import codex.services.rest.CodexControllerREST;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ComponentScan(basePackageClasses={Application.class, UICodex.class, CodexControllerREST.class})
public class StartRestServices {
    public static void main(String[] args) {

       // SpringApplication.run(StartRestServices.class, args);
        Application.launch(UICodex.class,args);
    }
}