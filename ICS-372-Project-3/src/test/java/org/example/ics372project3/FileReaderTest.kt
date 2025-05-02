package org.example.ics372project3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException

class FileReaderStaticMethodTest {

 private lateinit var dealerSet: MutableSet<Dealer?>

 @BeforeEach
 fun setUp() {
  dealerSet = mutableSetOf()
 }

 @Test
 fun `readFile should call JSONReader parse`() {
  val testFilePath = "C:\\git\\ICS-372-Project-3\\ICS-372-Project-3\\vehicle.json" // Provide a real JSON test file here

  try {
   FileReader.readFile(testFilePath, dealerSet)
   assertTrue(dealerSet.isNotEmpty(), "Dealer set should be populated by JSONReader.")
  } catch (e: IOException) {
   fail("IOException thrown during JSONReader parsing: ${e.message}")
  }
 }

 @Test
 fun `readFile should call XMLReader parse`() {
  val testFilePath = "C:\\git\\ICS-372-Project-3\\ICS-372-Project-3\\vehicle.xml" // Provide a real XML test file here

  try {
   FileReader.readFile(testFilePath, dealerSet)
   assertTrue(dealerSet.isNotEmpty(), "Dealer set should be populated by XMLReader.")
  } catch (e: IOException) {
   fail("IOException thrown during XMLReader parsing: ${e.message}")
  }
 }

 @Test
 fun `readFile should call CSVReader parse`() {
  val testFilePath = "C:\\git\\ICS-372-Project-3\\ICS-372-Project-3\\vehicle.csv" // Provide a real CSV test file here

  try {
   FileReader.readFile(testFilePath, dealerSet)
   assertTrue(dealerSet.isNotEmpty(), "Dealer set should be populated by CSVReader.")
  } catch (e: IOException) {
   fail("IOException thrown during CSVReader parsing: ${e.message}")
  }
 }

 @Test
 fun `readFile should print unsupported file type`() {
  val testFilePath = "src/test/resources/test_dealers.txt"
  // You can redirect stdout temporarily to capture printed output if needed

  FileReader.readFile(testFilePath, dealerSet)

  assertTrue(dealerSet.isEmpty(), "Dealer set should remain empty for unsupported file types.")
 }
}
