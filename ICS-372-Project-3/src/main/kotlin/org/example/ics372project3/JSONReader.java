package org.example.ics372project3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Set;

/**
 * This class reads and parses dealer inventory data from a JSON file
 * It extends abstract File_Reader and implements the parsing method for JSON File
 */
public class JSONReader extends FileReader {
    private String filePath;

    public JSONReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * parses the JSON File and Loads dealer and Vehicle data into the provided dealer set
     * IF the JSON file is empty or improperly formatted,appropriate message will be shown
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException (if errors occurs while reading  the file)
     */
    @Override
    public void parse(Set<Dealer> dealerSet) throws IOException {
        JSONParser parser = new JSONParser();

        try  {
            java.io.FileReader fileReader = new java.io.FileReader(filePath);
            JSONObject mainJsonObj = (JSONObject) parser.parse(fileReader);

            //Check the key exists before accessing it
            if(!mainJsonObj.containsKey("car_inventory")){
                System.out.println("Empty JSON File.");
            }

            JSONArray carInventory = (JSONArray) mainJsonObj.get("car_inventory");

            //Check the carInventory is null before iterating it
            if (carInventory == null) {
                System.out.println("Car Inventory is null.");
                return;
            }
            if(carInventory.isEmpty()){
                System.out.println("File is empty. Import dealer data into the file.");
                return;
            }

            for (Object vehicleObj : carInventory) {
                JSONObject vehicle = (JSONObject) vehicleObj;

                String type = (String) vehicle.get("vehicle_type");
                String dealerName;
                if(vehicle.containsKey("dealer_name")){
                    dealerName = (String) vehicle.get("dealer_name");}
                else{
                    dealerName = "";
                }
                boolean isAcquisitionEnabled;
                if(vehicle.containsKey("is_acquisition_enabled")){
                    isAcquisitionEnabled = (boolean) vehicle.get("is_acquisition_enabled");
                }else{
                    isAcquisitionEnabled = true;
                }

                String dealershipID = (String) vehicle.get("dealership_id");
                String manufacturer = (String) vehicle.get("vehicle_manufacturer");
                String model = (String) vehicle.get("vehicle_model");
                String id = (String) vehicle.get("vehicle_id");
                long acquisitionDate = ((Number) vehicle.get("acquisition_date")).longValue();
                long price = ((Number) vehicle.get("price")).longValue();

                boolean vehicleIsLoaned = false;
                if(vehicle.containsKey("is_loaned")){
                    vehicleIsLoaned = (boolean) vehicle.get("is_loaned");
                }else{
                    vehicleIsLoaned = false;
                }
                Vehicle newVehicle = checkType(type, manufacturer, model, id, acquisitionDate, price, vehicleIsLoaned);

                boolean found = false;
                for (Dealer d : dealerSet) {
                    if (d.getDealerID().equals(dealershipID)) {
                         //d.addVehicle(newVehicle);
                        d.getVehicleList().add(newVehicle);  //Temporary bypass "isAcquisitionEnabled" to ensure all JSON vehicles load
                        d.setDealerName(dealerName);
                        d.setAcquisitionEnabled(isAcquisitionEnabled);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Dealer d = new Dealer(dealershipID);
                    if(dealerName.length()>0 ){
                        d.setDealerName(dealerName);
                    }
                    //d.addVehicle(newVehicle);
                    d.getVehicleList().add(newVehicle);  //Temporary bypass "isAcquisitionEnabled" to ensure all JSON vehicles load
                    d.setAcquisitionEnabled(isAcquisitionEnabled);//Correctly set from JSON
                    dealerSet.add(d);
                }
            }
        } catch (ParseException e) {
            System.out.println("Error parsing JSON file: " + e.getMessage());
        }
    }

    /**
     * Creates a specific type of Vehicle Object.
     *
     * @param type            Vehicle type
     * @param manufacturer    Vehicle manufacturer
     * @param model           Vehicle model
     * @param id              Vehicle ID
     * @param acquisitionDate Vehicle AcquisitionDate
     * @param price           Vehicle price
     * @param vehicleIsLoaned
     * @return Vehicle        A new specific type Vehicle object will be returned
     */
    static Vehicle checkType(String type, String manufacturer, String model, String id, long acquisitionDate, double price, boolean vehicleIsLoaned) {
        Vehicle newVehicle = null;
        switch (type.toLowerCase()) {
            case "suv":
                newVehicle = new SUV(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
                break;
            case "sedan":
                newVehicle = new Sedan(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
                break;
            case "pickup":
                newVehicle = new Pickup(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
                break;
            case "sports car":
                newVehicle = new SportsCar(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
                break;
            default:
                System.out.println("Unknown vehicle type: " + type);
                break;
        }
        return newVehicle;
    }
}

