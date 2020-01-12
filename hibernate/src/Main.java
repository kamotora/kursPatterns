import controllers.Controller;
import controllers.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import util.HibernateSessionFactory;

import javax.persistence.metamodel.EntityType;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ControllerLogin controllerLogin = new ControllerLogin();
        controllerLogin.showWindow();
    }


    public static void main(String[] args) throws Exception {
        final Session session = HibernateSessionFactory.getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                System.out.println("Finded entity: "+entityName);
            }
        } finally {
            session.close();
        }

        launch(args);
    }
}