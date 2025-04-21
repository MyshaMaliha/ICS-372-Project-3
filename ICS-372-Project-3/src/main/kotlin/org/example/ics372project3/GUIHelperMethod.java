package org.example.ics372project3;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GUIHelperMethod {

    /**
     *  Displays an alert dialog with the given message.
     * @param message (The message to display in the alert)
     */
    // Helper methods
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Car Dealership System");

        //Creating a label with wrapping enabled
        Label contentLabel = new Label(message);
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(400);

        alert.getDialogPane().setContent(contentLabel);
        //alert.setContentText(message);

        //Apply Styles
        DialogPane dialogPane =alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }
    /**
     * Prompts the user for input with a given message.
     * @param prompt (The prompt message displayed to the user)
     * @return The user’s input or null if canceled.
     */
    public String getUserInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(prompt);

        //Apply styles
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }


    //making a  ScrollPane to make the alert's content scrollable
    public void showAlert2(String message) {
        //Create a TextArea with the content
        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);   //Wrap the text so it does not go out of the TextArea
        textArea.setEditable(false);  //Disable editing of the TextArea

        //Create a ScrollPane and add the TextArea to it
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);  //Ensures the content fits the width of the dialog

        //Create a custom alert with the scrollable content
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Dealer and Vehicle Information");

        //Set the dialog's content to the ScrollPane
        dialog.getDialogPane().setContent(scrollPane);

        //Add an ok Button ti close the dialog
        ButtonType okButton = new ButtonType("OK");
        dialog.getDialogPane().getButtonTypes().add(okButton);

        //Apply Styles
        DialogPane dialogPane =dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        //shoe the dialog and wait for the user to close it
        dialog.showAndWait();

    }
    @FXML
    public void handleBackToHome(ActionEvent event) throws IOException {
        try {
            //create a FXMLLoader & tells it where to find the FXMl file. getClass().getReasourse() tries to find "GUI-view.fxml" relative to the current class's package
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI-view.fxml"));
            //Load the FXMl & returns the root node of the UI layout
            Parent homeRoot = loader.load();

            //Get current stage & scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //gets the current stage(window) by working up form the UI node that triggered the event
            //Steps:
            //event.getSource() gets the button that was clicked
            //(Node) cast it so we can call .getScene()
            //.getScene() → gives you the scene that button is part of
            //.getWindows() grabs the stage that owns the scene

            Scene currentScene = currentStage.getScene();  //gets the existing scene so we can wrap out its contents without creating a new scene

            //Retrieves the controller class that is defined in the fx:controller attribute of GUI-viw.fxml.
            GUIController homeController = loader.getController();
            //calling method to pass teh stage & scene to the controller
            homeController.setPrimaryStageAndScene(currentStage, currentScene);

            //Swaps out the root node of the scene to show the new UI(the one from "GUI-view,fxml"). This is actually making the screen change
            currentScene.setRoot(homeRoot);
            currentStage.setTitle("Welcome to the Dealership app! ");
        } catch(IOException e){
            showAlert("Error returning to home page" + e.getMessage());
        }
    }
}