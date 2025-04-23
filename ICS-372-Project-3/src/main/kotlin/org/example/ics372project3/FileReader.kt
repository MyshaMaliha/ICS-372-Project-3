package org.example.ics372project3

import java.io.IOException

/**
 * Abstract class for reading dealer inventory data from different file formats
 * provides a static method to determine the appropriate reader based on the file extension
 */
abstract class FileReader {
    /**
     * abstract method to parse dealer inventory from a file
     * implementing classes must define the logic for parsing specific file formats
     * @param dealerSet (the set of dealers where parsed data will be stored)
     * @throws IOException (if error occurs while reading the file)
     */
    @Throws(IOException::class)
    abstract fun parse(dealerSet: MutableSet<Dealer?>)

    companion object {
        /**
         * Reads a file and parses dealer inventory data based on the file type
         * supports JSON and XML file formats for now
         * @param filePath (The path to file to be read)
         * @param dealerSet (the set of dealers where parsed data will be stored)
         * @throws IOException (if error occurs while reading the file)
         */
        @Throws(IOException::class)
        @JvmStatic
        fun readFile(filePath: String, dealerSet: MutableSet<Dealer?>) {
            if (filePath.endsWith(".json")) {
                JSONReader(filePath).parse(dealerSet) // Calls the JSON parser if file is JSON
            } else if (filePath.endsWith(".xml")) {
                XMLReader(filePath).parse(dealerSet) // Calls the XML parser if file is XML
            } else {
                println("Unsupported file type.")
            }
        }

    }
}



