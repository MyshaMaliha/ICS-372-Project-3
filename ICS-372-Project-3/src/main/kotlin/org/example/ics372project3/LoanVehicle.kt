package org.example.ics372project3

import java.util.*

/**
 * A utility class to handle loaning and returning of vehicles from a dealer's inventory.
 * It provides methods for loaning a vehicle, returning a loaned vehicle, and getting a list of loaned vehicles.
 */
class LoanVehicle {
    /**
     * Loans a vehicle if it is not a sports car.
     * @param dealer    The dealer from which the vehicle is being loaned.
     * @param vehicleID The ID of the vehicle to be loaned.
     * @return `true` if the vehicle is successfully loaned, `false` if the vehicle is a sports car or not found.
     */
    fun loanVehicle(dealer: Dealer, vehicleID: String): Boolean {
        for (v in dealer.getVehicleList()) {
            if (v.vehicleID == vehicleID) {
                if ((v.type?.lowercase(Locale.getDefault()) ?: "").contains("sports car")) {
                    return false //cannot rent sports car
                }
                v.isLoaned = true
                return true
            }
        }
        return false
    }

    /**
     * Returns a loaned vehicle back to the dealer's inventory.
     * @param dealer    The dealer to whom the vehicle is being returned.
     * @param vehicleID The ID of the vehicle to be returned.
     * @return `true` if the vehicle is successfully returned, `false` if the vehicle is not found or not loaned.
     */
//    fun returnVehicle(dealer: Dealer, vehicleID: String): Boolean {
//        for (v in dealer.getVehicleList()) {
//                  if (v.vehicleID == vehicleID && v.isLoaned) {
//                      v.isLoaned = false
//                      return true
//                  }
//              }
//
//        return false
//    }
    fun returnVehicle(dealer: Dealer, vehicleID: String): Boolean {
        for (v in dealer.getVehicleList()) {
            if (v.vehicleID == vehicleID && v.isLoaned == true) {
                v.isLoaned = false
                return true
            }
        }
        return false
    }


    /**
     * Gets a list of all vehicles that are currently loaned out by the dealer.
     * @param dealer The dealer whose loaned vehicles are to be retrieved.
     * @return A list of loaned vehicles.
     */

    fun getLoanedVehicles(dealer: Dealer): List<Vehicle> {
        val lonedVehicleList: MutableList<Vehicle> = ArrayList()
        for (v in dealer.getVehicleList()) {
            if (v.isLoaned == true) {
                lonedVehicleList.add(v)
            }
        }
        return lonedVehicleList
    }
}
