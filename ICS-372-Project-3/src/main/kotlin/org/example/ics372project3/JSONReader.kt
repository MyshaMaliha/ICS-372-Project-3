package org.example.ics372project3

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.IOException
import java.util.*

/**
 * This class reads and parses dealer inventory data from a JSON file.
 * It extends the abstract FileReader and implements the parsing method for JSON files.
 */
class JSONReader(private val filePath: String) : FileReader() {

    /**
     * Parses the JSON file and loads Dealer and Vehicle data into the provided dealer set.
     * Supports dealer-only entries (dealers with no vehicles).
     *
     * @param dealerSet the set where parsed dealers will be stored
     * @throws IOException if an error occurs while reading the file
     */
    @Throws(IOException::class)
    override fun parse(dealerSet: MutableSet<Dealer?>) {
        val parser = JSONParser()

        try {
            val fileReader = java.io.FileReader(filePath)
            val mainJsonObj = parser.parse(fileReader) as JSONObject

            if (!mainJsonObj.containsKey("car_inventory")) {
                println("Empty JSON file or missing 'car_inventory' key.")
                return
            }

            val carInventory = mainJsonObj["car_inventory"] as? JSONArray ?: run {
                println("Car inventory is null or not a valid array.")
                return
            }

            if (carInventory.isEmpty()) {
                println("File is empty. Import dealer data into the file.")
                return
            }

            for (item in carInventory) {
                val vehicleJson = item as JSONObject

                val dealershipID = vehicleJson["dealership_id"] as? String ?: continue
                val dealerName = vehicleJson["dealer_name"] as? String ?: ""
                val isAcquisitionEnabled = vehicleJson["is_acquisition_enabled"] as? Boolean ?: true

                var dealer = dealerSet.find { it?.dealerID == dealershipID }

                // Check if this JSON object contains a vehicle or just dealer info
                if (!vehicleJson.containsKey("vehicle_id")) {
                    if (dealer == null) {
                        dealer = Dealer(dealershipID)
                        dealer.dealerName = dealerName
                        dealer.isAcquisitionEnabled = isAcquisitionEnabled
                        dealerSet.add(dealer)
                    } else {
                        dealer.dealerName = dealerName
                        dealer.isAcquisitionEnabled = isAcquisitionEnabled
                    }
                    continue
                }

                // It's a vehicle entry
                val type = vehicleJson["vehicle_type"] as? String ?: continue
                val manufacturer = vehicleJson["vehicle_manufacturer"] as? String ?: continue
                val model = vehicleJson["vehicle_model"] as? String ?: continue
                val id = vehicleJson["vehicle_id"] as? String ?: continue
                val acquisitionDate = (vehicleJson["acquisition_date"] as? Number)?.toLong() ?: continue
                val price = (vehicleJson["price"] as? Number)?.toDouble() ?: continue
                val isLoaned = vehicleJson["is_loaned"] as? Boolean ?: false

                val newVehicle = checkType(type, manufacturer, model, id, acquisitionDate, price, isLoaned)

                if (dealer == null) {
                    dealer = Dealer(dealershipID)
                    dealerSet.add(dealer)
                }

                dealer.dealerName = dealerName
                dealer.isAcquisitionEnabled = isAcquisitionEnabled
                newVehicle?.let { dealer.getVehicleList().add(it) }
            }

        } catch (e: ParseException) {
            println("Error parsing JSON file: ${e.message}")
        }
    }

    companion object {
        /**
         * Creates a specific type of Vehicle object based on the vehicle type string.
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
            return when (type.lowercase(Locale.getDefault())) {
                "suv" -> SUV(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "sedan" -> Sedan(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "pickup" -> Pickup(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                "sports car" -> SportsCar(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned)
                else -> {
                    println("Unknown vehicle type: $type")
                    null
                }
            }
        }
    }
}

