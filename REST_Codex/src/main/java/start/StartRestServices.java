package start;

import codex.services.rest.CodexControllerREST;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.SecurityConfig;


@SpringBootApplication
@ComponentScan(basePackageClasses={Application.class, UICodex.class, CodexControllerREST.class, SecurityConfig.class})
public class StartRestServices {
    public static void main(String[] args) {

       // SpringApplication.run(StartRestServices.class, args);
        Application.launch(UICodex.class,args);

    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}