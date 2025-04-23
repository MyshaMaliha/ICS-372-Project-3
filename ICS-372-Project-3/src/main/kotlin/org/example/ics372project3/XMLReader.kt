package org.example.ics372project3

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.io.IOException
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

/**
 * This class reads and parses dealer inventory data from a XML file
 * It extends abstract File_Reader and implements the parsing method for XML File
 */
class XMLReader
/**
 * Constructs a XMLReader with the specific file path
 * @param filePath (XML File path containing dealer and vehicle data)
 */(private val filePath: String) : FileReader() {
    /**
     * Parses the xml file and loads dealer and vehicle data into the provided dealer set
     * if the xml file is invalid or improperly formatted like missing Dealer ID, Vehicle ID or Vehicle type. it stops processing the file and shows error
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException if errors occurs while reading or parsing the xml file
     */
    @Throws(IOException::class)
    override fun parse(dealerSet: MutableSet<Dealer?>) {
        try {
            val xmlFile = File(filePath)
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(xmlFile)
            doc.documentElement.normalize()

            val dealerNodes = doc.getElementsByTagName("Dealer")

            for (i in 0..<dealerNodes.length) {
                val dealerNode = dealerNodes.item(i)
                if (dealerNode.nodeType == Node.ELEMENT_NODE) {
                    val dealerElement = dealerNode as Element

                    // Ensure dealer has a valid ID
                    val dealerID = dealerElement.getAttribute("id").trim { it <= ' ' }
                    if (dealerID.isEmpty()) {
                        throw IOException("Error: Dealer is missing 'id' attribute. Stopping file processing.")
                    }

                    val dealer = getOrCreateDealer(dealerID, dealerSet)

                    // Extract dealer name if it exists
                    val nameNodes = dealerElement.getElementsByTagName("Name")
                    if (nameNodes.length > 0) {
                        dealer.dealerName = nameNodes.item(0).textContent.trim { it <= ' ' }
                    }

                    val vehicleNodes = dealerElement.getElementsByTagName("Vehicle")
                    for (j in 0..<vehicleNodes.length) {
                        val vehicleNode = vehicleNodes.item(j)
                        if (vehicleNode.nodeType == Node.ELEMENT_NODE) {
                            val vehicleElement = vehicleNode as Element

                            // Ensure vehicle has a type and ID
                            val type = vehicleElement.getAttribute("type").trim { it <= ' ' }
                            val vehicleID = vehicleElement.getAttribute("id").trim { it <= ' ' }
                            if (type.isEmpty() || vehicleID.isEmpty()) {
                                throw IOException("Error: Vehicle missing 'type' or 'id' in Dealer $dealerID. Stopping file processing.")
                            }

                            // Extract optional fields with defaults
                            val manufacturer = getElementText(vehicleElement, "Make", "Unknown")
                            val model = getElementText(vehicleElement, "Model", "Unknown")
                            val price = getElementDouble(vehicleElement, "Price", 0.0)
                            val vehicleIsLoaned = getElementBoolean(vehicleElement, "is_loaned", false)

                            // Create vehicle and add it to the dealer
                            val vehicle = checkType(type, manufacturer, model, vehicleID, 0, price, vehicleIsLoaned)
                            if (vehicle != null) {
                                dealer.addVehicle(vehicle)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            println(e.message) // Print the error message before stopping
            throw e // Rethrow the exception to completely stop the program
        } catch (e: Exception) {
            throw IOException("Error parsing XML file: " + e.message)
        }
    }

    /**
     * Retrieves an existing dealer or creates a new dealer if the dealer does not exist
     * @param dealerID  ID of the Dealer
     * @param dealerSet  The set of the dealers to search or add to
     * @return the existing or  newly created dealer
     */
    private fun getOrCreateDealer(dealerID: String, dealerSet: MutableSet<Dealer?>): Dealer {
        for (d in dealerSet) {
            if (d?.dealerID == dealerID) {
                return d
            }
        }
        val newDealer = Dealer(dealerID)
        dealerSet.add(newDealer)
        return newDealer
    }

    /**
     * Helper method to safely get text content from an XML element, with a default value
     * @param parent   The parent element to search within
     * @param tagName  the tag name to search for
     * @param defaultValue the default value to return if the tag is not found
     * @return   the text content of the tag or the default value if the tag is not found
     */
    private fun getElementText(parent: Element, tagName: String, defaultValue: String): String {
        val nodeList = parent.getElementsByTagName(tagName)
        return if (nodeList.length > 0) nodeList.item(0).textContent.trim { it <= ' ' } else defaultValue
    }

    /**
     * Helper method to safely get a double value from an XML element, with a default value
     * @param parent The parent element to search within
     * @param tagName  The tag name to search for
     * @param defaultValue  The default value to return if the tag is not found or contains invalid data
     * @return The double value of the tag, or the default value if the tag is not found or contains invalid data.
     */
    private fun getElementDouble(parent: Element, tagName: String, defaultValue: Double): Double {
        try {
            val nodeList = parent.getElementsByTagName(tagName)
            return if (nodeList.length > 0) nodeList.item(0).textContent.trim { it <= ' ' }.toDouble() else defaultValue
        } catch (e: NumberFormatException) {
            println("Warning: Invalid number format for $tagName. Using default: $defaultValue")
            return defaultValue
        }
    }

    /**
     * Helper method to safely get a boolean value from an XML element, with a default value
     * @param parent
     * @param tagName
     * @param defaultValue
     * @return  The boolean value of the tag, or the default value if the tag is not found.
     */
    private fun getElementBoolean(parent: Element, tagName: String, defaultValue: Boolean): Boolean {
        val nodeList = parent.getElementsByTagName(tagName)
        return if (nodeList.length > 0) nodeList.item(0).textContent.trim { it <= ' ' }.toBoolean() else defaultValue
    }

    companion object {
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









