package org.example.ics372project3

import org.example.ics372project3.FileWriter.exportJSON
import java.io.IOException

/**
 * transfers inventory from one dealer to another
 */
class InventoryTransfer {
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
    @Throws(IOException::class)
    fun transferVehicle(dealerSet: Set<Dealer>, dealerIdFrom: String, dealerIdTo: String, vehicleId: String): Boolean {
        // validate dealer IDs

        val dealerFrom = findDealer(dealerIdFrom, dealerSet)
        val dealerTo = findDealer(dealerIdTo, dealerSet)
        if (!dealerFrom!!.isAcquisitionEnabled || !dealerTo!!.isAcquisitionEnabled) {
            return false
        }

        if (dealerFrom == null || dealerTo == null) {
            println("One or both Dealer IDs are invalid.")
            return false
        }

        if (dealerFrom.getVehicleList().isEmpty()) {
            println("Dealer $dealerIdFrom has no vehicles to transfer.")
            return false
        }

        if (dealerIdFrom == dealerIdTo) {
            println("Cannot transfer vehicle to the same dealer.")
            return false
        }

        val vehicle = findVehicleById(dealerFrom, vehicleId)
        if (vehicle == null) {
            println("Vehicle ID not found.")
            return false
        }

        // Perform transfer
        dealerFrom.removeVehicle(vehicle)
        dealerTo.addVehicle(vehicle)

        println(
            "Vehicle " + vehicleId + " successfully transferred from " +
                    dealerIdFrom + " to " + dealerIdTo + "."
        )

        exportJSON(dealerSet)
        return true
    }


    /**
     * method that returns the dealerId from the set given to
     * find a dealer that matches the id
     * @param dealerID the dealer ID of the dealer that needs to be found
     * @param dealerSet the set of dealers used to search for the dealer
     * @return the dealer that matches the dealerId, or null if not found
     */
    fun findDealer(dealerID: String, dealerSet: Set<Dealer>): Dealer? {
        for (dealer in dealerSet) {
            if (dealer.dealerID == dealerID) {
                return dealer
            }
        }
        return null
    }

    /**
     * method that returns the Vehicle from dealer set given
     *
     * @param dealer the dealer whose vehicles will be searched
     * @param vehicleID the vehicle ID to find
     * @return the vehicle if found, otherwise null
     */
    fun findVehicleById(dealer: Dealer, vehicleID: String): Vehicle? {
        for (v in dealer.getVehicleList()) {
            if (v.vehicleID == vehicleID) {
                return v
            }
        }
        return null
    }
}
