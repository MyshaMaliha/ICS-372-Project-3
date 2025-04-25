package org.example.ics372project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class GUIFilePageController {

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
     * add vehicles into the existing or new dealers
     *
     * @throws IOException if file not write properly
     */
    @FXML
    private void addVehicle() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADD Vehicle");
        alert.setHeaderText("Choose how you want to add a vehicle: ");
        alert.setContentText("Select an option below:");

        // Create Buttons
        ButtonType existingDealerButton = new ButtonType("Existing Dealer");
        ButtonType newDealerButton = new ButtonType("New Dealer");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(existingDealerButton, newDealerButton, cancelButton);

        // Apply Styles to Dialog Pane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == existingDealerButton) {
                handleExistingDealerInput();
            } else if (result.get() == newDealerButton) {
                handleNewDealerInput();
            }
        }
        FileWriter.INSTANCE.exportJSON(dealerSet);
    }

    /**
     * Handles the vehicle addition process for an existing dealer.
     */
    private void handleExistingDealerInput() {
        // Ask whether the user wants to add via file or manual input first
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADD Vehicle");
        alert.setHeaderText("Choose how you want to add a vehicle to the existing dealer:");
        alert.setContentText("Select an option below:");

        ButtonType manualButton = new ButtonType("Manual");
        ButtonType fileButton = new ButtonType("File");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(manualButton, fileButton, cancelButton);
        // Apply Styles to Dialog Pane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == fileButton) {
                // First, ask how many dealer IDs they want to enter
                String numberOfDealersInput = helper.getUserInput("Enter the number of dealers you want to add vehicles to:");
                if (numberOfDealersInput == null || numberOfDealersInput.trim().isEmpty()) return;

                int numberOfDealers = Integer.parseInt(numberOfDealersInput);

                // Loop through each dealer ID and check if it's valid and enabled
                for (int i = 0; i < numberOfDealers; i++) {
                    String dealerID = helper.getUserInput("Enter Dealer ID " + (i + 1) + ":");
                    if (dealerID == null || dealerID.trim().isEmpty()) return;

                    Dealer selectedDealer = findDealerByID(dealerID);
                    if (selectedDealer == null) {
                        helper.showAlert("Dealer ID " + dealerID + " not found.");
                        return;
                    }

                    // Check if the dealer is enabled
                    if (!selectedDealer.isAcquisitionEnabled()) {
                        helper.showAlert("Dealer " + dealerID + " is disabled. Vehicle cannot be added.");
                        return; // Stop if any dealer is disabled
                    }
                }

                // If all dealers are enabled, proceed with file input
                handleFileInputForExistingDealer();
            } else if (result.get() == manualButton) {
//                // Ask for the dealer ID for manual input
//                String dealerID = helper.getUserInput("Enter Dealer ID to add a vehicle:");
//                if (dealerID == null) return;
//
//                Dealer selectedDealer = findDealerByID(dealerID);
//                if (selectedDealer == null) {
//                    helper.showAlert("Dealer ID not found.");
//                    return;
//                }
//
//                // Ask if the dealer is enabled or disabled
//                if (!selectedDealer.isAcquisitionEnabled()) {
//                    helper.showAlert("Dealer is disabled. Vehicle cannot be added.");
//                    return;
//                }

                // Process manual input for the selected dealer
                handleManualInput();
            }
        }
    }

    /**
     * Finds a dealer by its ID in the dealer set.
     */
    private Dealer findDealerByID(String dealerID) {
        for (Dealer dealer : dealerSet) {
            if (dealer.getDealerID().equals(dealerID)) {
                return dealer;
            }
        }
        return null;
    }

    /**
     * Handles the vehicle addition process for a new dealer.
     */
    private void handleNewDealerInput() {
//        String dealerID = helper.getUserInput("Enter New Dealer ID:");
//        if (dealerID == null) return;
//
//        // Create a new dealer and add it to the dealer set
//        Dealer newDealer = new Dealer(dealerID);
//        dealerSet.add(newDealer);



        // Now ask whether the user wants to add via file or manual input
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADD Vehicle");
        alert.setHeaderText("Choose how you want to add a vehicle to the new dealer:");
        alert.setContentText("Select an option below:");

        ButtonType manualButton = new ButtonType("Manual");
        ButtonType fileButton = new ButtonType("File");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

       alert.getButtonTypes().setAll(manualButton, fileButton, cancelButton);
         //Apply Styles to Dialog Pane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == fileButton) {
                handleFileInputForNewDealer();
            } else if (result.get() == manualButton) {
                handleManualInput();
            }
        }
    }

    /**
     * Reads vehicle data from a file for an existing dealer and updates the dealership records.
     */
    private void handleFileInputForExistingDealer() {
        String numDealersInput = helper.getUserInput("Enter the number of dealers in the file:");
        if (numDealersInput == null || numDealersInput.trim().isEmpty()) {
            helper.showAlert("Invalid input for the number of dealers.");
            return;
        }

        int numDealers;
        try {
            numDealers = Integer.parseInt(numDealersInput);
        } catch (NumberFormatException e) {
            helper.showAlert("Please enter a valid number.");
            return;
        }

        boolean allDealersEnabled = true;

        for (int i = 0; i < numDealers; i++) {
            String dealerID =helper. getUserInput("Enter Dealer ID #" + (i + 1) + ":");

            if (dealerID == null || dealerID.trim().isEmpty()) {
                helper.showAlert("Dealer ID cannot be empty.");
                allDealersEnabled = false;
                break;
            }

            // Check if the dealer exists using an enhanced for loop
            Dealer dealer = null;
            for (Dealer d : dealerSet) {
                if (d.getDealerID().equals(dealerID)) {
                    dealer = d;
                    break; // Exit the loop once the dealer is found
                }
            }

            if (dealer == null) {
                helper.showAlert("Dealer ID " + dealerID + " not found.");
                allDealersEnabled = false;
                break;  // Stop if the dealer is not found
            }

            // Check if the dealer is enabled
            if (!dealer.isAcquisitionEnabled()) {
                helper.showAlert("Dealer " + dealerID + " is disabled. Cannot read the file.");
                allDealersEnabled = false;
                break;  // Stop if the dealer is disabled
            }
        }

        if (allDealersEnabled) {
            // If all dealers are enabled, proceed to read the file
            String filePath = helper.getUserInput("Enter desired file path:");
            if (filePath != null && !filePath.trim().isEmpty()) {
                try {
                    FileReader.readFile(filePath, dealerSet);  // Assuming this reads and adds vehicles
                    helper.showAlert("Vehicles loaded from file.");
                    FileWriter.INSTANCE.exportJSON(dealerSet);
                } catch (IOException e) {
                    helper.showAlert("Error loading file: " + e.getMessage());
                }
            } else {
                helper.showAlert("Invalid file path.");
            }
        }
    }

    /**
     * Reads vehicle data from a file for a new dealer and updates the dealership records.
     */
    private void handleFileInputForNewDealer() {
        String filePath = helper.getUserInput("Enter desired file path:");
        if (filePath != null && !filePath.trim().isEmpty()) {
            try {
                FileReader.readFile(filePath, dealerSet);
                helper.showAlert("Vehicles loaded from file.");
                FileWriter.INSTANCE.exportJSON(dealerSet);
            } catch (IOException e) {
                helper.showAlert("Error loading file: " + e.getMessage());
            }
        } else {
            helper.showAlert("Invalid file path.");
        }
    }

    /**
     * Handles manual input for adding a vehicle.
     */
    private void handleManualInput() {
        String dealerID = helper.getUserInput("Enter Dealer ID:");
        if (dealerID == null) return;

        Dealer selectedDealer = findDealerByID(dealerID);
        if( selectedDealer == null){
            selectedDealer = new Dealer(dealerID);
            dealerSet.add(selectedDealer);
        }

        // Ask if the dealer is enabled or disabled
        if (!selectedDealer.isAcquisitionEnabled()) {
            helper.showAlert("Dealer is disabled. Vehicle cannot be added.");
            return;
        }


        String id = helper.getUserInput("Enter Vehicle ID:");
        String manufacturer = helper.getUserInput("Enter manufacturer:");
        String model = helper.getUserInput("Enter Model:");
        long acquisitionDate = Long.parseLong(helper.getUserInput("Enter Acquisition Date (as long value):"));
        double price = Double.parseDouble(helper.getUserInput("Enter price:"));
        String type = helper.getUserInput("Enter Vehicle Type (SUV, sedan, Pickup, Sports Car):");
        boolean isLoaned = Boolean.parseBoolean(helper.getUserInput("Loaned (true/false):"));

        Vehicle newVehicle = JSONReader.checkType(type, manufacturer, model, id, acquisitionDate, price, isLoaned);
        boolean added = selectedDealer.addVehicle(newVehicle);
        if (added) {
            helper.showAlert("Vehicle added successfully to dealer " + selectedDealer.getDealerID());
        } else {
            helper.showAlert("Failed to add vehicle.");
        }
    }

    /**
     * Exports the dealership data to a JSON file.
     * @throws IOException if error occurs while updating the file
     */
    @FXML
    private void exportJSON() throws IOException {
        try {
            FileWriter.INSTANCE.exportJSON(dealerSet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileWriter.INSTANCE.exportJSON(dealerSet);
        helper.showAlert("Exported JSON file successfully.");
    }

    @FXML
    private void handleBackToHome(ActionEvent event) throws IOException {
        helper.handleBackToHome(event);
    }



}
