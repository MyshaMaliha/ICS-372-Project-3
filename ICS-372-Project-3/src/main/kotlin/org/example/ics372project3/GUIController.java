package org.example.ics372project3;

import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIController {

    @FXML
    private Set<Dealer> dealerSet = new HashSet<>();
    private final String FILE_NAME = "Dealers_Vehicles.json";
    private Stage primaryStage;
    private Scene primaryScene;

    public void setPrimaryStageAndScene(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.primaryScene = scene;
    }

    GUIHelperMethod helper = new GUIHelperMethod();
    /**
     * Initializes the GUI by loading dealership data from file Dealer_Vehicle.json if it exists.
     * This method is automatically called by JAVAFX before displaying the GUI
     */
    @FXML
    public void initialize() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            System.out.println("Loading dealership data...");
            try {
                FileReader.readFile(FILE_NAME, dealerSet);
            } catch (IOException e) {
                helper.showAlert("Error loading the file: " + e.getMessage());
            }
        } else {
            System.out.println("existing file is empty not found");
        }
    }


    @FXML
    private void handleVehiclePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vehiclepage.fxml"));
        Parent vehicleRoot = loader.load();

        GUIVehiclePageController vehiclePageController = loader.getController();
        vehiclePageController.setDealerSet(dealerSet);
        vehiclePageController.setFileName(FILE_NAME);

        primaryScene.setRoot(vehicleRoot);
        primaryStage.setTitle("Vehicle Operation");
    }

    @FXML
    private void handleDealerPage(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dealerpage.fxml"));
        Parent dealerRoot = loader.load();

        GUIDealerPageController dealerPageController = loader.getController();
        dealerPageController.setDealerSet(dealerSet);
        dealerPageController.setFileName(FILE_NAME);

        primaryScene.setRoot(dealerRoot);
        primaryStage.setTitle("Dealer Operation");

    }

    @FXML
    private void handleFilePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("filepage.fxml"));
        Parent fileRoot = loader.load();

        GUIFilePageController filePageController = loader.getController();
        filePageController.setDealerSet(dealerSet);
        filePageController.setFileName(FILE_NAME);

        primaryScene.setRoot(fileRoot);
        primaryStage.setTitle("File Operation");

    }

}