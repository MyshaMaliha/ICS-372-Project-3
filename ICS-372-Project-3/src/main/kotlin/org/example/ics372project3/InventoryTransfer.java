package org.example.ics372project3;

import java.io.IOException;
import java.util.Set;

/**
 * transfers inventory from one dealer to another
 */
public class InventoryTransfer {
    /**
     * Transfers a vehicle from one dealer to another.
     *
     * @param dealerSet The set of all dealers.
     * @param dealerIdFrom The ID of the dealer transferring the vehicle.
     * @param dealerIdTo The ID of the dealer receiving the vehicle.
     * @param vehicleId The ID of the vehicle being transferred.
     * @throws IOException If there is an error updating the inventory file.
     * @return true if the transfer was successful, false otherwise.
     */
    public boolean transferVehicle(Set<Dealer> dealerSet, String dealerIdFrom, String dealerIdTo, String vehicleId) throws IOException {

        // validate dealer IDs
        Dealer dealerFrom = findDealer(dealerIdFrom, dealerSet);
        Dealer dealerTo = findDealer(dealerIdTo, dealerSet);
        if(!dealerFrom.isAcquisitionEnabled() || !dealerTo.isAcquisitionEnabled()){
            return false;
        }

        if (dealerFrom == null || dealerTo == null) {
            System.out.println("One or both Dealer IDs are invalid.");
            return false;
        }

        if (dealerFrom.getVehicleList().isEmpty() ) {
            System.out.println("Dealer " + dealerIdFrom + " has no vehicles to transfer.");
            return false;
        }

        if (dealerIdFrom.equals(dealerIdTo)) {
            System.out.println("Cannot transfer vehicle to the same dealer.");
            return false;
        }

        Vehicle vehicle = findVehicleById(dealerFrom, vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle ID not found.");
            return false;
        }

        // Perform transfer
        dealerFrom.removeVehicle(vehicle);
        dealerTo.addVehicle(vehicle);

        System.out.println("Vehicle " + vehicleId + " successfully transferred from " +
                dealerIdFrom + " to " + dealerIdTo + ".");

        File_Writer.exportJSON(dealerSet);
        return true;
    }


    /**
     *  method that returns the dealerId from the set given to
     * find a dealer that matches the id
     * @param dealerID the dealer ID of the dealer that needs to be found
     * @param dealerSet the set of dealers used to search for the dealer
     * @return the dealer that matches the dealerId, or null if not found
     */
    public Dealer findDealer(String dealerID, Set<Dealer> dealerSet){
        for(Dealer dealer: dealerSet){
            if(dealer.getDealerID().equals(dealerID)){
                return dealer;
            }
        }
        return null;
    }

    /**
     *  method that returns the Vehicle from dealer set given
     *
     * @param dealer the dealer whose vehicles will be searched
     * @param vehicleID the vehicle ID to find
     * @return the vehicle if found, otherwise null
     */

    public Vehicle findVehicleById(Dealer dealer, String vehicleID) {
        for (Vehicle v : dealer.getVehicleList()) {
            if (v.getVehicleID().equals(vehicleID)) {
                return v;
            }
        }
        return null;
    }


}
