package org.example.ics372project3

import java.io.File
import java.io.IOException
import java.util.*


class CSVReader(private val filePath: String) : FileReader() {
    @Throws(IOException::class)
    override fun parse(dealerSet: MutableSet<Dealer?>) {
        try {
            val file = File(filePath)
            val s = Scanner(file)
            while (s.hasNextLine()) {
                // get all the fields separated by commas
                val line = s.nextLine()
                val parts = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val dealershipId = parts[0]
                val dealerName = parts[1]
                val is_acquisition_enabled = bool(parts[2])
                val vehicleType = parts[3]
                val vehicle_manufacturer = parts[4]
                val vehicle_model = parts[5]
                val vehicle_id = parts[6]
                val price = parts[7].toDouble()
                val acquisition_date = parts[8].toLong()
                val is_loaned = bool(parts[9])

                // check the type of vehicle, then create vehicle object
                val vehicle = checkType(
                    vehicleType,
                    vehicle_manufacturer,
                    vehicle_model,
                    vehicle_id,
                    acquisition_date,
                    price,
                    is_loaned
                )

                val dealer = getOrCreateDealer(dealershipId, dealerSet, dealerName)
                if (vehicle != null) {
                    dealer.addVehicle(vehicle)
                }
            }
        } catch (e: IOException) {
            throw IOException(e)
        }
    }

    /**
     * Retrieves an existing dealer or creates a new dealer if the dealer does not exist
     * @param dealerID  ID of the Dealer
     * @param dealerSet  The set of the dealers to search or add to
     * @param dealerName the name of the dealer
     * @return the existing or  newly created dealer
     */
    private fun getOrCreateDealer(dealerID: String, dealerSet: MutableSet<Dealer?>, dealerName: String): Dealer {
        for (d in dealerSet) {
            if (d?.dealerID == dealerID) {
                return d
            }
        }
        val newDealer = Dealer(dealerID)
        newDealer.dealerName = dealerName
        dealerSet.add(newDealer)
        return newDealer
    }


    companion object {
        private fun bool(`val`: String): Boolean {
            if (`val`.equals("true", ignoreCase = true)) {
                return true
            }
            return false
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
        private fun checkType(
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
                    println("Unknown vehicle type: $type (Dealer ID: $id)")
                    null
                }
            }
        }
    }
}


