package org.example.ics372project3

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.File
import java.io.IOException
import java.util.function.Function


object FileWriter {  //FileWriter is a static utility class
    /**
     * Exports the dealer inventory data into "Dealers_Vehicle.json" File
     * the JSON File contains a structured inventory of all vehicles categorized by dealership
     *
     *
     *  This method iterates through the provided set of dealers, extract vehicle details and organize them into a structured JSON File.
     * Each vehicle record includes dealership details, vehicle type, manufacturer, model, price, acquisition date, loaned status.
     *
     * @param dealerSet (the set of dealers whose inventory will be exported)
     * @throws IOException (if error occurs while writing the JSON File)
     */
    @Throws(IOException::class)
    fun exportJSON(dealerSet: Set<Dealer>) {
        val objMap = ObjectMapper()
        objMap.enable(SerializationFeature.INDENT_OUTPUT)

        val outerList: MutableMap<String, List<Map<String, Any>>> = LinkedHashMap()
        val innerList: MutableList<Map<String, Any>> = ArrayList()

        for (d in dealerSet) {
            val vehicleList = d.getVehicleList()
            if (vehicleList.isEmpty()) {
                // Dealer has no vehicles, include only dealer information
                val dealerData: MutableMap<String, Any> = LinkedHashMap()
                dealerData["dealership_id"] = d.dealerID
                dealerData["dealer_name"] = d.dealerName
                dealerData["is_acquisition_enabled"] = d.isAcquisitionEnabled
                innerList.add(dealerData)
            } else {
                for (v in vehicleList) {
                    val vehicleData: MutableMap<String, Any> = LinkedHashMap()

                    vehicleData["dealership_id"] =
                        d.dealerID // HashMap.put("key", value) | assuming getDealerID() exists
                    vehicleData["dealer_name"] = d.dealerName ?: "Unknown"// Add the dealer name here
                    vehicleData["is_acquisition_enabled"] = d.isAcquisitionEnabled ?: "Unknown"
                    vehicleData["vehicle_type"] = v.type ?: "Unknown"
                    vehicleData["vehicle_manufacturer"] = v.manufacturer ?: "Unknown"
                    vehicleData["vehicle_model"] = v.model ?: "Unknown"
                    vehicleData["vehicle_id"] = v.vehicleID ?: "Unknown"// Assuming getId() exists
                    vehicleData["price"] = v.price ?: "Unknown"
                    vehicleData["acquisition_date"] = v.acquisitionDate ?: "Unknown"
                    vehicleData["is_loaned"] = v.isLoaned ?: "Unknown"


                    innerList.add(vehicleData)
                }
            }
        }

        innerList.sortBy { it["dealership_id"] as String }
        outerList["car_inventory"] = innerList

        objMap.writerWithDefaultPrettyPrinter().writeValue(
            File("Dealers_Vehicles.json"),
            outerList
        )
    }

}


