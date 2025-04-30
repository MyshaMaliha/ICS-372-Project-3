package org.example.ics372project3;
import javax.swing.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Optional;
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
                FileWriter.INSTANCE.exportJSON(dealerSet);
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
                FileWriter.INSTANCE.exportJSON(dealerSet);
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
        if (dealerID == null || dealerID.trim().isEmpty()) return;

        Dealer dealer = dealerSet.stream().filter(d -> d.getDealerID().equals(dealerID)).findFirst().orElse(null);
        if (dealer == null ) {
            helper.showAlert("Dealer ID not found.");
            return;
        }

        String newName = helper.getUserInput("Enter new Dealer Name:");
         if( !(newName == null)) {
             dealer.setDealerName(newName);

             helper.showAlert("Dealer name updated successfully.");
             FileWriter.INSTANCE.exportJSON(dealerSet);
         }
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
            if (success) {
                helper.showAlert("Vehicle transferred successfully");
            } else {
                helper.showAlert("Transfer failed. The receiving Dealer ID is disable");
            }


            // Ask if the user wants to transfer another vehicle using Yes/No dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Continue Vehicle");
            alert.setHeaderText("Do you want to transfer another vehicle?");
            alert.setContentText("Select an option below:");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");

            alert.getButtonTypes().setAll(yesButton, noButton);

            //Apply styles to Dialog Pane
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                if(result.get() == yesButton){
                    transferInventory();
                }
                else if(result.get() == noButton){
                   continueToTransfer = false;
                }
            }


        }
    }

    @FXML
    private void handleBackToHome(ActionEvent event) throws IOException {
        helper.handleBackToHome(event);
    }


}
