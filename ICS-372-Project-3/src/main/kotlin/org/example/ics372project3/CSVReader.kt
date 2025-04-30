package org.example.ics372project3;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.Scanner;


public class CSVReader extends FileReader{
    private String filePath;
    public CSVReader(String filePath){
        this.filePath = filePath;
    }
    public void parse(Set<Dealer> dealerSet) throws IOException {
        try{
            File file = new File(filePath);
            Scanner s = new Scanner(file);
            while(s.hasNextLine()){
                // get all the fields separated by commas
                String line = s.nextLine();
                String[] parts = line.split(",");
                String dealershipId = parts[0];
                String dealerName = parts[1];
                Boolean is_acquisition_enabled = bool(parts[2]);
                String vehicleType = parts[3];
                String vehicle_manufacturer = parts[4];
                String vehicle_model = parts[5];
                String vehicle_id = parts[6];
                double price = Double.parseDouble(parts[7]);
                long acquisition_date = Long.parseLong(parts[8]);
                Boolean is_loaned = bool(parts[9]);

                // check the type of vehicle, then create vehicle object
                Vehicle vehicle = checkType(vehicleType, vehicle_manufacturer, vehicle_model, vehicle_id, acquisition_date, price, is_loaned);

                Dealer dealer = getOrCreateDealer(dealershipId, dealerSet, dealerName);
                if(vehicle != null){
                    dealer.addVehicle(vehicle);
                }


            }
        } catch ( IOException e) {
            throw new IOException(e);
        }


    }

    private static Boolean bool(String val){
        if(val.equalsIgnoreCase("true")){
            return true;
        }
        return false;
    }

    /**
     * Creates a vehicle object based on the vehicle type
     * @param type  The type of the vehicle(e.g. SUV. Sedan..)
     * @param manufacturer  The manufacturer of the vehicle
     * @param model  The model of the vehicle
     * @param id  The id of the vehicle
     * @param acquisitionDate The acquisitionDate of the vehicle
     * @param price  The price of the vehicle
     * @param vehicleIsLoaned  weather  the vehicle is loaned
     * @return the vehicle object, or null if the type is unknown
     */
    private static Vehicle checkType(String type, String manufacturer, String model, String id, long acquisitionDate, double price, boolean vehicleIsLoaned) {
        return switch (type.toLowerCase()) {
            case "suv" -> new SUV(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "sedan" -> new Sedan(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "pickup" -> new Pickup(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "sports car" -> new SportsCar(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            default -> {
                System.out.println("Unknown vehicle type: " + type + " (Dealer ID: " + id + ")");
                yield null;
            }
        };
    }

    /**
     * Retrieves an existing dealer or creates a new dealer if the dealer does not exist
     * @param dealerID  ID of the Dealer
     * @param dealerSet  The set of the dealers to search or add to
     * @param dealerName the name of the dealer
     * @return the existing or  newly created dealer
     */
    private Dealer getOrCreateDealer(String dealerID, Set<Dealer> dealerSet, String dealerName) {
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                return d;
            }
        }
        Dealer newDealer = new Dealer(dealerID);
        newDealer.setDealerName(dealerName);
        dealerSet.add(newDealer);
        return newDealer;
    }


}


