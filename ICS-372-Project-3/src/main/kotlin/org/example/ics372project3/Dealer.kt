package org.example.ics372project3

//import java.io.IO.println
import java.util.*

class Dealer( val dealerID: String) {

    var dealerName: String = "" //new field dealerName, default empty string

    var isAcquisitionEnabled: Boolean = true

    //initially all dealer's isAcquisition is TRUE
    private val vehicleList: MutableList<Vehicle> = ArrayList()

    fun enableAcquisition() {
        this.isAcquisitionEnabled = true
    }

    fun disableAcquisition() {
        this.isAcquisitionEnabled = false
    }

    fun addVehicle(vehicle: Vehicle): Boolean {
        if (isAcquisitionEnabled) {
            vehicleList.add(vehicle)
            return true
        }
        println("Dealer disabled.")
        return false
    }

    fun getVehicleList(): List<Vehicle> {
        return vehicleList
    }

    /**
     * loops through the list of vehicles and removes
     * if a match was found based on the given vehicle object
     *
     * @param vehicle the vehicle object that is being removed
     */
    fun removeVehicle(vehicle: Vehicle) {
        vehicleList.removeIf { v: Vehicle -> v.vehicleID == vehicle.vehicleID }
    }
}


