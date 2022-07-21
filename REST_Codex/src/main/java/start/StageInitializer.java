package start;

import chat.network.utils.AbstractServer;
import chat.network.utils.ChatRpcConcurrentServer;
import chat.persistance.MessageRepository;
import chat.persistance.UserRepository;
import chat.persistance.repository.jdbc.MessageRepositoryJdbc;
import chat.persistance.repository.jdbc.UserRepositoryJdbc;
import chat.server.ChatServicesImpl;
import chat.services.IChatServices;
import com.project.controller.Controller;

import com.project.service.ServiceCodex;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

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

            //service.getClients();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/login.fxml"));
            AnchorPane root = loader.load();
            Controller ctrl =
                    loader.<Controller>getController();
            //ctrl.setServer();
            Stage stage = event.getStage();
            stage.setScene(new Scene(root, 700, 400));
            stage.setTitle("LoginPage");
            stage.show();



            new Thread( () -> {
                // UserRepository userRepo=new UserRepositoryMock();
                int defaultPort=55555;
                Properties serverProps=new Properties();
                try {
                    serverProps.load(Controller.class.getResourceAsStream("/chatserver.properties"));
                    System.out.println("Server properties set. ");
                    serverProps.list(System.out);
                } catch (IOException e) {
                    System.err.println("Cannot find chatserver.properties "+e);
                    return;
                }
                UserRepository userRepo=new UserRepositoryJdbc(serverProps);
                MessageRepository messRepo=new MessageRepositoryJdbc(serverProps);
                IChatServices chatServerImpl=new ChatServicesImpl(userRepo, messRepo);
                int chatServerPort=defaultPort;
                try {
                    chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
                }catch (NumberFormatException nef){
                    System.err.println("Wrong  Port Number"+nef.getMessage());
                    System.err.println("Using default port "+defaultPort);
                }
                System.out.println("Starting server on port: "+chatServerPort);
                AbstractServer server = new ChatRpcConcurrentServer(chatServerPort, chatServerImpl);
                try {
                    server.start();
                } catch (ServerException e) {
                    System.err.println("Error starting the server" + e.getMessage());
                }finally {
                    try {
                        server.stop();
                    }catch(ServerException e){
                        System.err.println("Error stopping server "+e.getMessage());
                    }
                }

            }).start();


        }catch(IOException e){
            throw new RuntimeException();
        }

    }
}
