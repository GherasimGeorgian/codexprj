package start;

import com.project.domain.AccountType;
import com.project.domain.Client;
import com.project.repository.interfaces.IClientRepository;
import com.project.repository.orm.ClientORMRepository;
import com.project.repository.orm.SessionFactoryInit;
import com.project.service.ServiceCodex;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;

public class UICodex extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(StartRestServices.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public ConfigurableApplicationContext getAppContext() {

            return applicationContext;
        }

        public Stage getStage() {

            return ((Stage) getSource());
        }
    }
}
