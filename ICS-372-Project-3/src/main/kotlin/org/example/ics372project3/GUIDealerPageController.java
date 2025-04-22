package org.example.ics372project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.Set;

public class GUIDealerPageController {
    private Set<Dealer> dealerSet;
    private String fileName;

    //passing data (dealerSet and file) from main GUIController to the GUIDealerController
    public void setDealerSet(Set<Dealer> dealerSet) {
        this.dealerSet = dealerSet;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    GUIHelperMethod helper = new GUIHelperMethod();

    /**
     * Display all dealers and their vehicle List in an alert window.
     */
    @FXML
    private void checkDealers() {   //printing each dealer and it's vehicle record
        StringBuilder result = new StringBuilder();
        for (Dealer d : dealerSet) {
            result.append("---Dealer : ").append(d.getDealerID()).append(" ---\n");
            result.append("---Dealer Name : ").append(d.getDealerName()).append(" ---\n");
            result.append("---AcquisitionEnabled:  ").append(d.isAcquisitionEnabled()).append("---\n");

            for (Vehicle v : d.getVehicleList()) {
                result.append("Vehicle ID : ").append(v.getVehicleID()).append("\nModel: ").append((v.getModel())).append("\n");
                result.append("Loaned: ").append(v.isLoaned()).append("\n");
            }
            result.append("\n");
        }
        helper.showAlert2(result.toString().isEmpty() ? "No delaers found." : result.toString());

    }
    /**
     * Enable acquisition for a dealer identifies by user input
     * updates the JSON file upon success
     *
     * @throws IOException if error occurs during file writing
     */
    @FXML
    private void enableDealer() throws IOException {
        String dealerID = helper.getUserInput("Enter Dealer ID to Enable:");
        if (dealerID == null) return;

        boolean found = false;
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                d.enableAcquisition();
                helper.showAlert("Dealer " + dealerID + " is now enabled.");
                found = true;
                FileWriter.exportJSON(dealerSet);
                break;
            }
        }
        if (!found) helper.showAlert("Dealer ID not found.");

    }

    /**
     * Disable acquisition for a dealer identifies by  user input
     * updates the JSON file upon success
     *
     * @throws IOException if an error occurs during writing the file
     */
    @FXML
    private void disableDealer() throws IOException {
        String dealerID = helper.getUserInput("Enter Dealer ID to Disable:");
        if (dealerID == null) return;

        boolean found = false;
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                d.disableAcquisition();
                helper.showAlert("Dealer " + dealerID + " is now disabled.");
                found = true;
                FileWriter.exportJSON(dealerSet);
                break;
            }
        }
        if (!found) helper.showAlert("Dealer ID not found.");

    }
    /**
     *  Prompts the user for dealer ID and new name, then updates the dealership records
     *  updates the JSON file upon success
     * @throws IOException if error occurs while updating the file
     */
    @FXML
    private void updateDealerName() throws IOException {
        String dealerID = helper.getUserInput("Enter Dealer ID to update name:");
        if (dealerID == null) return;

        Dealer dealer = dealerSet.stream().filter(d -> d.getDealerID().equals(dealerID)).findFirst().orElse(null);
        if (dealer == null) {
            helper.showAlert("Dealer ID not found.");
            return;
        }

        String newName = helper.getUserInput("Enter new Dealer Name:");
        dealer.setDealerName(newName) ;

        helper.showAlert("Dealer name updated successfully.");
        FileWriter.exportJSON(dealerSet);

    }


    /**
     * transfers vehicle from one dealer to another dealer
     * updates the JSON file upon success
     * @throws IOException if error occurs while updating the file
     */
    @FXML

    private void transferInventory() throws IOException{
        InventoryTransfer iT = new InventoryTransfer();

        if(dealerSet.isEmpty()){
            helper.showAlert("No dealers available for transfer.");
            return;
        }

        boolean continueToTransfer = true;

        while(continueToTransfer){
            // asks the user input for dealer IDs and Vehicle ID
            String dealerIdFrom = helper.getUserInput("Enter dealer ID transferring from");
            if (dealerIdFrom == null) return;
            String dealerIdTo = helper.getUserInput("Enter dealer ID transferring to");
            if (dealerIdTo == null) return;

            String vehicleId = helper.getUserInput("Enter Vehicle ID to transfer:");
            if (vehicleId == null) return;

            // call the transferVehicle method to transfer.
            boolean success = iT.transferVehicle(dealerSet, dealerIdFrom, dealerIdTo, vehicleId);

            // if it did not transfer, print error message
            if (!success) {
                helper.showAlert("Transfer failed. Please check your inputs and try again.");
            }

            // asks the user if they want to transfer another vehicle
            String response = helper.getUserInput("Do you want to transfer another vehicle? (yes/no)");
            if (response == null || !response.equalsIgnoreCase("yes")) {
                continueToTransfer = false;
            }


        }
    }

    @FXML
    private void handleBackToHome(ActionEvent event) throws IOException {
        helper.handleBackToHome(event);
    }

//    @FXML
//    private void handleBackToHome(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI-view.fxml"));
//        Parent homePageRoot = loader.load();  <---was creating a new instance of GUIController
//    }
//@FXML
//private void handleBackToHome(ActionEvent event) throws IOException {
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI-view.fxml"));
//    Parent homeRoot = loader.load();
//
//    GUIController homeController = loader.getController();
//
//    // Get current stage and scene
//    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//    Scene currentScene = currentStage.getScene();
//
//    // set the scene and stage again!
//    homeController.setPrimaryStageAndScene(currentStage, currentScene);
//
//    currentScene.setRoot(homeRoot);
//    currentStage.setTitle("Welcome to the Dealership app!");
//}
}
