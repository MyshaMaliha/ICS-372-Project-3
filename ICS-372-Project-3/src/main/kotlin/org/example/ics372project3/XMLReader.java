package org.example.ics372project3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * This class reads and parses dealer inventory data from a XML file
 * It extends abstract File_Reader and implements the parsing method for XML File
 */
public class XMLReader extends FileReader {
    private String filePath;


    /**
     * Constructs a XMLReader with the specific file path
     * @param filePath (XML File path containing dealer and vehicle data)
     */
    public XMLReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Parses the xml file and loads dealer and vehicle data into the provided dealer set
     * if the xml file is invalid or improperly formatted like missing Dealer ID, Vehicle ID or Vehicle type. it stops processing the file and shows error
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException if errors occurs while reading or parsing the xml file
     */
    @Override
    public void parse(Set<Dealer> dealerSet) throws IOException {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList dealerNodes = doc.getElementsByTagName("Dealer");

            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Node dealerNode = dealerNodes.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;

                    // Ensure dealer has a valid ID
                    String dealerID = dealerElement.getAttribute("id").trim();
                    if (dealerID.isEmpty()) {
                        throw new IOException("Error: Dealer is missing 'id' attribute. Stopping file processing.");
                    }

                    Dealer dealer = getOrCreateDealer(dealerID, dealerSet);

                    // Extract dealer name if it exists
                    NodeList nameNodes = dealerElement.getElementsByTagName("Name");
                    if (nameNodes.getLength() > 0) {
                        dealer.setDealerName(nameNodes.item(0).getTextContent().trim());
                    }

                    NodeList vehicleNodes = dealerElement.getElementsByTagName("Vehicle");
                    for (int j = 0; j < vehicleNodes.getLength(); j++) {
                        Node vehicleNode = vehicleNodes.item(j);
                        if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element vehicleElement = (Element) vehicleNode;

                            // Ensure vehicle has a type and ID
                            String type = vehicleElement.getAttribute("type").trim();
                            String vehicleID = vehicleElement.getAttribute("id").trim();
                            if (type.isEmpty() || vehicleID.isEmpty()) {
                                throw new IOException("Error: Vehicle missing 'type' or 'id' in Dealer " + dealerID + ". Stopping file processing.");
                            }

                            // Extract optional fields with defaults
                            String manufacturer = getElementText(vehicleElement, "Make", "Unknown");
                            String model = getElementText(vehicleElement, "Model", "Unknown");
                            double price = getElementDouble(vehicleElement, "Price", 0.0);
                            boolean vehicleIsLoaned = getElementBoolean(vehicleElement, "is_loaned", false);

                            // Create vehicle and add it to the dealer
                            Vehicle vehicle = checkType(type, manufacturer, model, vehicleID, 0, price, vehicleIsLoaned);
                            if (vehicle != null) {
                                dealer.addVehicle(vehicle);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Print the error message before stopping
            throw e; // Rethrow the exception to completely stop the program
        } catch (Exception e) {
            throw new IOException("Error parsing XML file: " + e.getMessage());
        }
    }

    /**
     * Retrieves an existing dealer or creates a new dealer if the dealer does not exist
     * @param dealerID  ID of the Dealer
     * @param dealerSet  The set of the dealers to search or add to
     * @return the existing or  newly created dealer
     */
    private Dealer getOrCreateDealer(String dealerID, Set<Dealer> dealerSet) {
        for (Dealer d : dealerSet) {
            if (d.getDealerID().equals(dealerID)) {
                return d;
            }
        }
        Dealer newDealer = new Dealer(dealerID);
        dealerSet.add(newDealer);
        return newDealer;
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
    private static Vehicle checkType(String type, String manufacturer, String model, String id, long acquisitionDate, double price, boolean vehicleIsLoaned) {
        return switch (type.toLowerCase()) {
            case "suv" -> new SUV(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "sedan" -> new Sedan(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "pickup" -> new Pickup(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            case "sports car" -> new SportsCar(id, manufacturer, model, acquisitionDate, price, vehicleIsLoaned);
            default -> {
                System.out.println("Unknown vehicle type: " + type + " (Dealer ID: " + id + ")");
                yield null;
            }
        };
    }

    /**
     * Helper method to safely get text content from an XML element, with a default value
     * @param parent   The parent element to search within
     * @param tagName  the tag name to search for
     * @param defaultValue the default value to return if the tag is not found
     * @return   the text content of the tag or the default value if the tag is not found
     */
    private String getElementText(Element parent, String tagName, String defaultValue) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : defaultValue;
    }

    /**
     * Helper method to safely get a double value from an XML element, with a default value
     * @param parent The parent element to search within
     * @param tagName  The tag name to search for
     * @param defaultValue  The default value to return if the tag is not found or contains invalid data
     * @return The double value of the tag, or the default value if the tag is not found or contains invalid data.
     */
    private double getElementDouble(Element parent, String tagName, double defaultValue) {
        try {
            NodeList nodeList = parent.getElementsByTagName(tagName);
            return (nodeList.getLength() > 0) ? Double.parseDouble(nodeList.item(0).getTextContent().trim()) : defaultValue;
        } catch (NumberFormatException e) {
            System.out.println("Warning: Invalid number format for " + tagName + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Helper method to safely get a boolean value from an XML element, with a default value
     * @param parent
     * @param tagName
     * @param defaultValue
     * @return  The boolean value of the tag, or the default value if the tag is not found.
     */
    private boolean getElementBoolean(Element parent, String tagName, boolean defaultValue) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? Boolean.parseBoolean(nodeList.item(0).getTextContent().trim()) : defaultValue;
    }
}









