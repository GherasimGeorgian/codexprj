package com.project;

import com.project.domain.AccountType;
import com.project.domain.Client;
import com.project.repository.interfaces.IClientRepository;
import com.project.repository.orm.ClientORMRepository;
import com.project.repository.orm.SessionFactoryInit;
import com.project.service.ServiceCodex;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


//        FXMLLoader loader=new FXMLLoader();
//        loader.setLocation(getClass().getResource("/views/login.fxml"));
//        AnchorPane root=loader.load();
//
//        primaryStage.setScene(new Scene(root, 700, 400));
//        primaryStage.setTitle("LoginPage");
//        primaryStage.show();

        SessionFactoryInit sessionFactory = new SessionFactoryInit();

        IClientRepository clientRepository = new ClientORMRepository(sessionFactory);
        ServiceCodex service = new ServiceCodex(clientRepository);

        service.adaugaClient(new Client("admin","admin","email1","fr1","lr1",new Date(),"077777",AccountType.CLIENT));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
