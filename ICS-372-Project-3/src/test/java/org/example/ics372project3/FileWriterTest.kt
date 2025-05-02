package org.example.ics372project3

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.io.IOException

class FileWriterTest{
 private val outputFile: File = File("Dealers_Vehicles.json")


 @BeforeEach
 fun setup() {
  // Ensure the file is deleted before each test
  if (outputFile.exists()) {
   outputFile.delete()
  }
 }


 @Test
 fun testExportJSON() {
  // Set up a mock dealer set with vehicles
  val dealerSet = setOf(
   Dealer("1").apply {
    dealerName = "Dealer One"
    addVehicle(SUV("1", "Toyota", "Highlander", 1634254932000, 40000.0, false))
    addVehicle(Sedan("2", "Honda", "Civic", 1634254932000, 25000.0, true))
   },
   Dealer("2").apply {
    dealerName = "Dealer Two"
    addVehicle(Pickup("3", "Ford", "F-150", 1634254932000, 35000.0, false))
   }
  )


  // Call the export method to generate the JSON file
  try {
   FileWriter.exportJSON(dealerSet)
  } catch (e: IOException) {
   fail("IOException thrown: ${e.message}")
  }


  // Verify that the file exists after the method is called
  assertTrue(outputFile.exists(), "File should be created after export")


  // Verify that the content of the file is not empty
  val content = outputFile.readText()
  assertTrue(content.isNotEmpty(), "The content of the JSON file should not be empty.")


  //  Check if the content is in valid JSON format (basic validation)
  assertTrue(content.startsWith("{") && content.endsWith("}"), "The content should start and end with braces (valid JSON).")
 }

}