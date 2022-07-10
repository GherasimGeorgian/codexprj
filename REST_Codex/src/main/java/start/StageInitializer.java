package start;

import com.project.service.ServiceCodex;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Configuration
@ImportResource("classpath:beans.xml")
@Component
public class StageInitializer implements ApplicationListener<UICodex.StageReadyEvent> {

    @Autowired
    private ServiceCodex service;

    @Override
    public void onApplicationEvent(UICodex.StageReadyEvent event) {
        try {
            //AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
            //ServiceCodex service = (ServiceCodex)context.getBean("service");

            service.getClients();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/login.fxml"));
            AnchorPane root = loader.load();

            Stage stage = event.getStage();
            stage.setScene(new Scene(root, 700, 400));
            stage.setTitle("LoginPage");
            stage.show();


        }catch(IOException e){
            throw new RuntimeException();
        }



//
//        primaryStage.setScene(new Scene(root, 700, 400));
//        primaryStage.setTitle("LoginPage");
//        primaryStage.show();
    }
}
