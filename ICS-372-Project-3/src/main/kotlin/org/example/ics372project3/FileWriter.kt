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
        val objMap = ObjectMapper() //Used to convert JAVA obj into JSON
        objMap.enable(SerializationFeature.INDENT_OUTPUT) //makes output JSON more readable
        val outerList: MutableMap<String, List<Map<String, Any>>> =
            LinkedHashMap() //outerList:Map-> key="Car Inventory", value= List of vehicles (inner List)
        //Stores the entire inventory under a key ("Car Inventory"), making the JSON structured.
        val innerList: MutableList<Map<String, Any>> =
            ArrayList() //innerList: list of multiple vehicle records(For Each Vehicle-> Vehicle's attributed stored in a Map(key-value pairs)

        for (d in dealerSet) {   //loops through all dealers in dealerSet
            for (v in d.getVehicleList()) {    //loops through all vehicles owned by that dealer
                val vehicleData: MutableMap<String, Any> =
                    LinkedHashMap() //Stores each  attribute of a vehicle as a Map(key-value pairs)
                vehicleData["dealership_id"] = d.dealerID // HashMap.put("key", value) | assuming getDealerID() exists
                vehicleData["dealer_name"] = d.dealerName // Add the dealer name here
                vehicleData["is_acquisition_enabled"] = d.isAcquisitionEnabled
                vehicleData["vehicle_type"] = v.type
                vehicleData["vehicle_manufacturer"] = v.manufacturer
                vehicleData["vehicle_model"] = v.model
                vehicleData["vehicle_id"] = v.vehicleID // Assuming getId() exists
                vehicleData["price"] = v.price
                vehicleData["acquisition_date"] = v.acquisitionDate
                vehicleData["is_loaned"] = v.isLoaned

                innerList.add(vehicleData) //add the  vehicle Obj to innerList (list)
            }
        }
        innerList.sortBy(
            {
             it["dealership_id"] as String
            }
        ) //sort by dealership_id to group similar dealerships together
        outerList["car_inventory"] = innerList
        objMap.writerWithDefaultPrettyPrinter().writeValue(
            File("Dealers_Vehicles.json"),
            outerList
        ) //using: writeValue( File resultFile, Obj value) from Jackson's ObjectMapper class
    }
}

