
package org.example.ics372project3


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException


class CSVReaderTest {


 @Test
 @Throws(IOException::class)
 fun testParseVehicleCSV() {
  // Arrange
  // Assuming the vehicle.csv file is located in the resources directory or a known location
  val csvFilePath = "C:\\git\\ICS-372-Project-3\\ICS-372-Project-3\\vehicle.csv"
  val csvFile = File(csvFilePath)


  // Ensure the file exists before testing
  assertTrue(csvFile.exists(), "vehicle.csv file should exist at the specified path")


  val dealerSet: MutableSet<Dealer?> = mutableSetOf()
  val reader = CSVReader(csvFilePath)


  // Act
  reader.parse(dealerSet)


  // Assert
  val nonNullDealers = dealerSet.filterNotNull()
  assertTrue(nonNullDealers.isNotEmpty(), "Dealer set should contain at least one dealer")


  // Example check for the first dealer
  val dealer = nonNullDealers.first()
  assertNotNull(dealer.dealerName, "Dealer name should not be null")
  assertEquals(2, dealer.getVehicleList().size)  // Assuming 2 vehicles in the CSV


  // Further checks can be added here as needed
  val vehicles = dealer.getVehicleList()
  assertTrue(vehicles.any { it.manufacturer == "Tesla" }, "Vehicle manufacturer 'Tesla' should exist in the dealer's vehicle list")
  assertTrue(vehicles.any { it.manufacturer == "Ford" }, "Vehicle manufacturer 'Ford' should exist in the dealer's vehicle list")
 }
}
