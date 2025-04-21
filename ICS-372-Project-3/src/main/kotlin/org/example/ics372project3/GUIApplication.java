package org.example.ics372project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Loads GUI-view.fxml file form the same package as GUIApplication.FXML Loader pareses the FXML and connects it to the controller (defined inside the FXML via fx:controller)
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("GUI-view.fxml"));
        Parent root = fxmlLoader.load();   //loads the GUI from the FXML into a JavaFX object tree


        Scene scene = new Scene(root, 500, 650);
        stage.setTitle("Welcome to the Dealership app!");
        stage.setScene(scene);
        stage.show();

        //Send scene & stage to controller
        GUIController controller = fxmlLoader.getController();  //after loading the FXML, this retrieves the controller instance( controller) that's linked in your GUI-view.fxml
        controller.setPrimaryStageAndScene(stage, scene);   //calls a method to pass the Scene & Stage to your controller so that controller can change the scene root later(like vehicle / dealer views)
    }

    public static void main(String[] args) {
        launch();
    }
}