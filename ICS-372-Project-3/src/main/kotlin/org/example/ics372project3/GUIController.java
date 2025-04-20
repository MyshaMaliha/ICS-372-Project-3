package org.example.ics372project3;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GUIController {
    @FXML
    private Set<Dealer> dealerSet = new HashSet<>();
    private final String FILE_NAME = "Dealers_Vehicles.json";


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
                File_Reader.readFile(FILE_NAME, dealerSet);
            } catch (IOException e) {
                showAlert("Error loading the file: " + e.getMessage());
            }
        } else {
            System.out.println("existing file is empty not found");
        }
    }

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
        showAlert2(result.toString().isEmpty() ? "No delaers found." : result.toString());

    }
    /**
     * Enable acquisition for a dealer identifies by user input
     * updates the JSON file upon success
     *
     * @throws IOException if error occurs during file writing
     */
    @FXML
    private void enableDealer() throws IOException {
        String dealerID = getUserInput("Enter Dealer ID to Enable:");
        if (dealerID == null) return;

        boolean found = false;
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                d.enableAcquisition();
                showAlert("Dealer " + dealerID + " is now enabled.");
                found = true;
                File_Writer.exportJSON(dealerSet);
                break;
            }
        }
        if (!found) showAlert("Dealer ID not found.");

    }

    /**
     * Disable acquisition for a dealer identifies by  user input
     * updates the JSON file upon success
     *
     * @throws IOException if an error occurs during writing the file
     */
    @FXML
    private void disableDealer() throws IOException {
        String dealerID = getUserInput("Enter Dealer ID to Disable:");
        if (dealerID == null) return;

        boolean found = false;
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                d.disableAcquisition();
                showAlert("Dealer " + dealerID + " is now disabled.");
                found = true;
                File_Writer.exportJSON(dealerSet);
                break;
            }
        }
        if (!found) showAlert("Dealer ID not found.");

    }

    /**
     * add vehicles into the existing or new dealers
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
        dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == existingDealerButton) {
                handleExistingDealerInput();
            } else if (result.get() == newDealerButton) {
                handleNewDealerInput();
            }
        } File_Writer.exportJSON(dealerSet);
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
            dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");


        Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == fileButton) {
                    // First, ask how many dealer IDs they want to enter
                    String numberOfDealersInput = getUserInput("Enter the number of dealers you want to add vehicles to:");
                    if (numberOfDealersInput == null || numberOfDealersInput.trim().isEmpty()) return;

                    int numberOfDealers = Integer.parseInt(numberOfDealersInput);

                    // Loop through each dealer ID and check if it's valid and enabled
                    for (int i = 0; i < numberOfDealers; i++) {
                        String dealerID = getUserInput("Enter Dealer ID " + (i + 1) + ":");
                        if (dealerID == null || dealerID.trim().isEmpty()) return;

                        Dealer selectedDealer = findDealerByID(dealerID);
                        if (selectedDealer == null) {
                            showAlert("Dealer ID " + dealerID + " not found.");
                            return;
                        }

                        // Check if the dealer is enabled
                        if (!selectedDealer.isAcquisitionEnabled()) {
                            showAlert("Dealer " + dealerID + " is disabled. Vehicle cannot be added.");
                            return; // Stop if any dealer is disabled
                        }
                    }

                    // If all dealers are enabled, proceed with file input
                    handleFileInputForExistingDealer();
                } else if (result.get() == manualButton) {
                    // Ask for the dealer ID for manual input
                    String dealerID = getUserInput("Enter Dealer ID to add a vehicle:");
                    if (dealerID == null) return;

                    Dealer selectedDealer = findDealerByID(dealerID);
                    if (selectedDealer == null) {
                        showAlert("Dealer ID not found.");
                        return;
                    }

                    // Ask if the dealer is enabled or disabled
                    if (!selectedDealer.isAcquisitionEnabled()) {
                        showAlert("Dealer is disabled. Vehicle cannot be added.");
                        return;
                    }

                    // Process manual input for the selected dealer
                    handleManualInput(selectedDealer);
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
        String dealerID = getUserInput("Enter New Dealer ID:");
        if (dealerID == null) return;

        // Create a new dealer and add it to the dealer set
        Dealer newDealer = new Dealer(dealerID);
        dealerSet.add(newDealer);

        // Now ask whether the user wants to add via file or manual input
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADD Vehicle");
        alert.setHeaderText("Choose how you want to add a vehicle to the new dealer:");
        alert.setContentText("Select an option below:");

        ButtonType manualButton = new ButtonType("Manual");
        ButtonType fileButton = new ButtonType("File");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(manualButton, fileButton, cancelButton);
        // Apply Styles to Dialog Pane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == fileButton) {
                handleFileInputForNewDealer(newDealer);
            } else if (result.get() == manualButton) {
                handleManualInput(newDealer);
            }
        }
    }

    /**
     * Reads vehicle data from a file for an existing dealer and updates the dealership records.
     */
    private void handleFileInputForExistingDealer() {
        String numDealersInput = getUserInput("Enter the number of dealers in the file:");
        if (numDealersInput == null || numDealersInput.trim().isEmpty()) {
            showAlert("Invalid input for the number of dealers.");
            return;
        }

        int numDealers;
        try {
            numDealers = Integer.parseInt(numDealersInput);
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number.");
            return;
        }

        boolean allDealersEnabled = true;

        for (int i = 0; i < numDealers; i++) {
            String dealerID = getUserInput("Enter Dealer ID #" + (i + 1) + ":");

            if (dealerID == null || dealerID.trim().isEmpty()) {
                showAlert("Dealer ID cannot be empty.");
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
                showAlert("Dealer ID " + dealerID + " not found.");
                allDealersEnabled = false;
                break;  // Stop if the dealer is not found
            }

            // Check if the dealer is enabled
            if (!dealer.isAcquisitionEnabled()) {
                showAlert("Dealer " + dealerID + " is disabled. Cannot read the file.");
                allDealersEnabled = false;
                break;  // Stop if the dealer is disabled
            }
        }

        if (allDealersEnabled) {
            // If all dealers are enabled, proceed to read the file
            String filePath = getUserInput("Enter desired file path:");
            if (filePath != null && !filePath.trim().isEmpty()) {
                try {
                    File_Reader.readFile(filePath, dealerSet);  // Assuming this reads and adds vehicles
                    showAlert("Vehicles loaded from file.");
                    File_Writer.exportJSON(dealerSet);
                } catch (IOException e) {
                    showAlert("Error loading file: " + e.getMessage());
                }
            } else {
                showAlert("Invalid file path.");
            }
        }
    }

    /**
     * Reads vehicle data from a file for a new dealer and updates the dealership records.
     */
    private void handleFileInputForNewDealer(Dealer newDealer) {
        String filePath = getUserInput("Enter desired file path:");
        if (filePath != null && !filePath.trim().isEmpty()) {
            try {
                File_Reader.readFile(filePath, dealerSet);
                showAlert("Vehicles loaded from file.");
                File_Writer.exportJSON(dealerSet);
            } catch (IOException e) {
                showAlert("Error loading file: " + e.getMessage());
            }
        } else {
            showAlert("Invalid file path.");
        }
    }

    /**
     * Handles manual input for adding a vehicle.
     */
    private void handleManualInput(Dealer selectedDealer) {
        String id = getUserInput("Enter Vehicle ID:");
        String manufacturer = getUserInput("Enter manufacturer:");
        String model = getUserInput("Enter Model:");
        long acquisitionDate = Long.parseLong(getUserInput("Enter Acquisition Date (as long value):"));
        double price = Double.parseDouble(getUserInput("Enter price:"));
        String type = getUserInput("Enter Vehicle Type (SUV, sedan, Pickup, Sports Car):");
        boolean isLoaned = Boolean.parseBoolean(getUserInput("Loaned (true/false):"));

        Vehicle newVehicle = JSONReader.checkType(type, manufacturer, model, id, acquisitionDate, price, isLoaned);
        boolean added = selectedDealer.addVehicle(newVehicle);
        if (added) {
            showAlert("Vehicle added successfully to dealer " + selectedDealer.getDealerID());
        } else {
            showAlert("Failed to add vehicle.");
        }
    }

/**

    /**
     *  Prompts the user for dealer ID and new name, then updates the dealership records
     *  updates the JSON file upon success
     * @throws IOException if error occurs while updating the file
     */
    @FXML
    private void updateDealerName() throws IOException {
        String dealerID = getUserInput("Enter Dealer ID to update name:");
        if (dealerID == null) return;

        Dealer dealer = dealerSet.stream().filter(d -> d.getDealerID().equals(dealerID)).findFirst().orElse(null);
        if (dealer == null) {
            showAlert("Dealer ID not found.");
            return;
        }

        String newName = getUserInput("Enter new Dealer Name:");
        dealer.setDealerName(newName) ;

        showAlert("Dealer name updated successfully.");
        File_Writer.exportJSON(dealerSet);

    }

    /**
     * Exports the dealership data to a JSON file.
     * @throws IOException if error occurs while updating the file
     */
    @FXML
    private void exportJSON() throws IOException {
        try {
            File_Writer.exportJSON(dealerSet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File_Writer.exportJSON(dealerSet);
        showAlert("Exported JSON file successfully.");
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
            showAlert("No dealers available for transfer.");
            return;
        }

        boolean continueToTransfer = true;

        while(continueToTransfer){
            // asks the user input for dealer IDs and Vehicle ID
            String dealerIdFrom = getUserInput("Enter dealer ID transferring from");
            if (dealerIdFrom == null) return;
            String dealerIdTo = getUserInput("Enter dealer ID transferring to");
            if (dealerIdTo == null) return;

            String vehicleId = getUserInput("Enter Vehicle ID to transfer:");
            if (vehicleId == null) return;

            // call the transferVehicle method to transfer.
            boolean success = iT.transferVehicle(dealerSet, dealerIdFrom, dealerIdTo, vehicleId);

            // if it did not transfer, print error message
            if (!success) {
                showAlert("Transfer failed. Please check your inputs and try again.");
            }

            // asks the user if they want to transfer another vehicle
            String response = getUserInput("Do you want to transfer another vehicle? (yes/no)");
            if (response == null || !response.equalsIgnoreCase("yes")) {
                continueToTransfer = false;
            }


        }


    }

    /**
     * Loans a vehicle to a dealer based on user input.
     * Prompts the user for a dealer ID and vehicle ID, then processes the loan if valid.
     * Sports cars cannot be loaned.
     * @throws IOException if an error occurs during file writing.
     */
    @FXML
    private void loanVehicle() throws IOException {
        String dealerID = getUserInput("Enter Dealer ID:");
        if (dealerID == null || dealerID.trim().isEmpty()) {
            return;
        }

        Dealer dealer = dealerSet.stream()
                .filter(d -> d.getDealerID().equals(dealerID))
                .findFirst()
                .orElse(null);

        if (dealer == null) {
            showAlert("Dealer ID not found.");
            return;
        }

        String vehicleID = getUserInput("Enter Vehicle ID:");
        if (vehicleID == null || vehicleID.trim().isEmpty()) {
            showAlert("Vehicle ID cannot be empty.");
            return;
        }
        LoanVehicle lv = new LoanVehicle();

        if (lv.loanVehicle(dealer, vehicleID)) {
            showAlert("Vehicle " + vehicleID + " has been loaned.");
        }else {
            showAlert("Vehicle ID not found or cannot be loaned (sports cars are not allowed).");
        }
        File_Writer.exportJSON(dealerSet);

    }
    /**
     * Returns a previously loaned vehicle to a dealer.
     * Prompts the user for a dealer ID and vehicle ID, then processes the return if valid.
     *@throws IOException if an error occurs during file writing.
     */
    @FXML
    private void returnVehicle() throws IOException {
        String dealerID = getUserInput("Enter Dealer ID:");
        if (dealerID == null || dealerID.trim().isEmpty()) {

            return;
        }

        Dealer dealer = dealerSet.stream()
                .filter(d -> d.getDealerID().equals(dealerID))
                .findFirst()
                .orElse(null);

        if (dealer == null) {
            showAlert("Dealer ID not found.");
            return;
        }

        String vehicleID = getUserInput("Enter Vehicle ID:");
        if (vehicleID == null || vehicleID.trim().isEmpty()) {
            showAlert("Vehicle ID cannot be empty.");
            return;
        }

        LoanVehicle lv  = new LoanVehicle();

        if (lv.returnVehicle(dealer,vehicleID)) {
            showAlert("Vehicle " + vehicleID + " has been returned.");
        } else {
            showAlert("Vehicle ID not found or was not loaned out.");
        }
        File_Writer.exportJSON(dealerSet);

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

        showAlert(result.toString().isEmpty() ? "No vehicles are currently loaned." : result.toString());
        File_Writer.exportJSON(dealerSet);

    }


    /**
     *  Displays an alert dialog with the given message.
     * @param message (The message to display in the alert)
     */
    // Helper methods
    private void showAlert(String message) {
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
        dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }
    /**
     * Prompts the user for input with a given message.
     * @param prompt (The prompt message displayed to the user)
     * @return The user’s input or null if canceled.
     */
    private String getUserInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(prompt);

        //Apply styles
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
        //making a  ScrollPane to make the alert's content scrollable

        private void showAlert2(String message) {
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
            dialogPane.getStylesheets().add(getClass().getResource("org/example/ics372project3/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");

            //shoe the dialog and wait for the user to close it
            dialog.showAndWait();

        }

}