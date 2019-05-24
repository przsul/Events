package utp;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage primaryStage;
	public static EntityManagerFactory ENTITY_MANAGER_FACTORY;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	Main.ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("Events");

    	Main.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainview.fxml"));
        primaryStage.setTitle("Wydarzenia");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.centerOnScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
