package org.example.ics372project3;

import java.io.IOException;
import java.util.Set;

/**
 * Abstract class for reading dealer inventory data from different file formats
 * provides a static method to determine the appropriate reader based on the file extension
 */
public abstract class FileReader {

    /**
     * Reads a file and parses dealer inventory data based on the file type
     * supports JSON and XML file formats for now
     * @param filePath (The path to file to be read)
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException (if error occurs while reading the file)
     */
    public static void readFile(String filePath, Set<Dealer> dealerSet) throws IOException {
        if (filePath.endsWith(".json")) {
            new JSONReader(filePath).parse(dealerSet);  // Calls the JSON parser if file is JSON
        } else if (filePath.endsWith(".xml")) {
            new XMLReader(filePath).parse(dealerSet);  // Calls the XML parser if file is XML
        } else {
            System.out.println("Unsupported file type.");
        }
    }

    /**
     * abstract method to parse dealer inventory from a file
     * implementing classes must define the logic for parsing specific file formats
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException (if error occurs while reading the file)
     */
    public abstract void parse(Set<Dealer> dealerSet) throws IOException;
}



