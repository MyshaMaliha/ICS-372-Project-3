package org.example.ics372project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GUIVehiclePageController {

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
     * Loans a vehicle to a dealer based on user input.
     * Prompts the user for a dealer ID and vehicle ID, then processes the loan if valid.
     * Sports cars cannot be loaned.
     * @throws IOException if an error occurs during file writing.
     */
    @FXML
    private void loanVehicle() throws IOException {

        String dealerID = helper.getUserInput("Enter Dealer ID:");
        if (dealerID == null || dealerID.trim().isEmpty()) {
            return;
        }

        Dealer dealer = dealerSet.stream()
                .filter(d -> d.getDealerID().equals(dealerID))
                .findFirst()
                .orElse(null);

        if (dealer == null) {
            helper.showAlert("Dealer ID not found.");
            return;
        }

        String vehicleID =  helper.getUserInput("Enter Vehicle ID:");
        if (vehicleID == null || vehicleID.trim().isEmpty()) {
            helper.showAlert("Vehicle ID cannot be empty.");
            return;
        }
        LoanVehicle lv = new LoanVehicle();

        if (lv.loanVehicle(dealer, vehicleID)) {
            helper.showAlert("Vehicle " + vehicleID + " has been loaned.");
        }else {
            helper.showAlert("Vehicle ID not found or cannot be loaned (sports cars are not allowed).");
        }
        FileWriter.exportJSON(dealerSet);

    }
    /**
     * Returns a previously loaned vehicle to a dealer.
     * Prompts the user for a dealer ID and vehicle ID, then processes the return if valid.
     *@throws IOException if an error occurs during file writing.
     */
    @FXML
    private void returnVehicle() throws IOException {
        String dealerID =  helper.getUserInput("Enter Dealer ID:");
        if (dealerID == null || dealerID.trim().isEmpty()) {

            return;
        }

        Dealer dealer = dealerSet.stream()
                .filter(d -> d.getDealerID().equals(dealerID))
                .findFirst()
                .orElse(null);

        if (dealer == null) {
            helper.showAlert("Dealer ID not found.");
            return;
        }

        String vehicleID =  helper.getUserInput("Enter Vehicle ID:");
        if (vehicleID == null || vehicleID.trim().isEmpty()) {
            helper.showAlert("Vehicle ID cannot be empty.");
            return;
        }

        LoanVehicle lv  = new LoanVehicle();

        if (lv.returnVehicle(dealer,vehicleID)) {
            helper.showAlert("Vehicle " + vehicleID + " has been returned.");
        } else {
            helper.showAlert("Vehicle ID not found or was not loaned out.");
        }
        FileWriter.exportJSON(dealerSet);

    }
    /**
     * Displays a list of all loaned vehicles across dealers.
     * Retrieves loaned vehicles from each dealer and shows them in an alert dialog.
     *@throws IOException if an error occurs during file writing.
     */
    @FXML
    private void showLoanedVehicles() throws IOException {
        StringBuilder result = new StringBuilder("Loaned Vehicles:\n");

        LoanVehicle lv  = new LoanVehicle();
        for (Dealer d : dealerSet) {
            List<Vehicle> loanedVehicles = lv.getLoanedVehicles(d);
            if (!loanedVehicles.isEmpty()) {
                result.append("\nDealer: ").append(d.getDealerID()).append("\n");
                for (Vehicle v : loanedVehicles) {
                    result.append("Vehicle: ").append(v.getVehicleID()).append("\n");
                }
            }
        }

        helper.showAlert(result.toString().isEmpty() ? "No vehicles are currently loaned." : result.toString());
        FileWriter.exportJSON(dealerSet);

    }
    @FXML
     private void handleBackToHome(ActionEvent event) throws IOException {
        helper.handleBackToHome(event);
    }

//    @FXML
//    private void handleBackToHome(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI-view.fxml"));
//        Parent homeRoot = loader.load();
//
//        GUIController homeController = loader.getController();
//
//        //Get current stage & scene
//        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
//        Scene currentScene =  currentStage.getScene();
//
//        homeController.setPrimaryStageAndScene(currentStage, currentScene);
//
//        currentScene.setRoot(homeRoot);
//        currentStage.setTitle("Welcome to the Dealership app!");
//
//    }
}
