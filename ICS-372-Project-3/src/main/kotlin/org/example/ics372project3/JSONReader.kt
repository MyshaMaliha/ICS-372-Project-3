package org.example.ics372project3

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.IOException
import java.util.*

/**
 * This class reads and parses dealer inventory data from a JSON file
 * It extends abstract File_Reader and implements the parsing method for JSON File
 */
 class JSONReader(val filePath: String) : FileReader() {
    /**
     * parses the JSON File and Loads dealer and Vehicle data into the provided dealer set
     * IF the JSON file is empty or improperly formatted,appropriate message will be shown
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException (if errors occurs while reading  the file)
     */
    @Throws(IOException::class)
     override fun parse(dealerSet: MutableSet<Dealer?>) {
        val parser = JSONParser()

        try {
            val fileReader = java.io.FileReader(filePath)
            val mainJsonObj = parser.parse(fileReader) as JSONObject

            //Check the key exists before accessing it
            if (!mainJsonObj.containsKey("car_inventory")) {
                println("Empty JSON File.")
            }

            val carInventory = mainJsonObj["car_inventory"] as JSONArray?

            //Check the carInventory is null before iterating it
            if (carInventory == null) {
                println("Car Inventory is null.")
                return
            }
            if (carInventory.isEmpty()) {
                println("File is empty. Import dealer data into the file.")
                return
            }

            for (vehicleObj in carInventory) {
                val vehicle = vehicleObj as JSONObject

                val type = vehicle["vehicle_type"] as String
                val dealerName: String
                dealerName = if (vehicle.containsKey("dealer_name")) {
                    vehicle["dealer_name"] as String
                } else {
                    ""
                }
                val isAcquisitionEnabled: Boolean
                isAcquisitionEnabled = if (vehicle.containsKey("is_acquisition_enabled")) {
                    vehicle["is_acquisition_enabled"] as Boolean
                } else {
                    true
                }

                val dealershipID = vehicle["dealership_id"] as String
                val manufacturer = vehicle["vehicle_manufacturer"] as String
                val model = vehicle["vehicle_model"] as String
                val id = vehicle["vehicle_id"] as String
                val acquisitionDate = (vehicle["acquisition_date"] as Number).toLong()
                val price = (vehicle["price"] as Number).toLong()

                var vehicleIsLoaned = false
                vehicleIsLoaned = if (vehicle.containsKey("is_loaned")) {
                    vehicle["is_loaned"] as Boolean
                } else {
                    false
                }
                val newVehicle = checkType(type, manufacturer, model, id, acquisitionDate, price.toDouble(), vehicleIsLoaned)

                var found = false

//           if(dealerSet != null){
                for (d in dealerSet) {
                    if (d?.dealerID == dealershipID) {
                        //d.addVehicle(newVehicle);
                        if (newVehicle != null) {
                            d.getVehicleList().add(newVehicle)
                        } //Temporary bypass "isAcquisitionEnabled" to ensure all JSON vehicles load
                        d.dealerName = dealerName
                        d.isAcquisitionEnabled = isAcquisitionEnabled
                        found = true
                        break
                    }
                }

              if (!found) {
                    val d = Dealer(dealershipID)
                    if (dealerName.length > 0) {
                        d.dealerName = dealerName
                    }
                    //d.addVehicle(newVehicle);
                  if(newVehicle != null) {
                      d.getVehicleList()
                          .add(newVehicle) //Temporary bypass "isAcquisitionEnabled" to ensure all JSON vehicles load
                  }
                    d.isAcquisitionEnabled = isAcquisitionEnabled //Correctly set from JSON
                    dealerSet.add(d)
                }
            }

        } catch (e: ParseException) {
            println("Error parsing JSON file: " + e.message)
        }
    }

    companion object {
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
        @JvmStatic
        fun checkType(
            type: String,
            manufacturer: String,
            model: String,
            id: String,
            acquisitionDate: Long,
            price: Double,
            vehicleIsLoaned: Boolean
        ): Vehicle? {
            var newVehicle: Vehicle? = null
            when (type.lowercase(Locale.getDefault())) {
                "suv" -> newVehicle = SUV(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "sedan" -> newVehicle = Sedan(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "pickup" -> newVehicle = Pickup(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "sports car" -> newVehicle = SportsCar(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                else -> println("Unknown vehicle type: $type")
            }
            return newVehicle
        }
    }
}

