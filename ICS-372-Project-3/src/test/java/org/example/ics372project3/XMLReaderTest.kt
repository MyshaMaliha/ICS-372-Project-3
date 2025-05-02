package org.example.ics372project3

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import java.io.File
import java.io.IOException


class XMLReaderTest {
 private lateinit var xmlReader: XMLReader
 private val testFilePath = "src/test/resources/vehicle.xml"
 private val testFile = File(testFilePath)


 @BeforeEach
 fun setUp() {
  testFile.parentFile.mkdirs()
  testFile.writeText("""
          <dealers>
              <Dealer id="DLR001">
                  <Name>Test Dealer</Name>
                  <Vehicles>
                      <Vehicle type="SUV" id="VH001">
                          <Make>Toyota</Make>
                          <Model>RAV4</Model>
                          <Price>25000.0</Price>
                          <is_loaned>false</is_loaned>
                      </Vehicle>
                      <Vehicle type="Sedan" id="VH002">
                          <Make>Honda</Make>
                          <Model>Accord</Model>
                          <Price>22000.0</Price>
                          <is_loaned>true</is_loaned>
                      </Vehicle>
                  </Vehicles>
              </Dealer>
          </dealers>
       """.trimIndent())
  xmlReader = XMLReader(testFilePath)
 }


 @AfterEach
 fun tearDown() {
  if (testFile.exists()) {
   testFile.delete()
  }
 }


 @Test
 fun `parse should populate dealer set with correct data`() {
  val dealerSet = mutableSetOf<Dealer?>()
  xmlReader.parse(dealerSet)


  assertEquals(1, dealerSet.size)
  val dealer = dealerSet.first()!!


  assertEquals("DLR001", dealer.dealerID)
  assertEquals("Test Dealer", dealer.dealerName)
  assertEquals(2, dealer.getVehicleList().size)


  val suv = dealer.getVehicleList().find { it.vehicleID == "VH001" }
  assertEquals("Toyota", suv?.manufacturer)
  assertEquals("RAV4", suv?.model)
  assertEquals(25000.0, suv?.price)
  assertEquals("suv", suv?.type)
  assertEquals(false, suv?.isLoaned)


  val sedan = dealer.getVehicleList().find { it.vehicleID == "VH002" }
  assertEquals("Honda", sedan?.manufacturer)
  assertEquals("Accord", sedan?.model)
  assertEquals(22000.0, sedan?.price)
  assertEquals("sedan", sedan?.type)
  assertEquals(true, sedan?.isLoaned)
 }


 @Test
 fun `parse should throw IOException for missing dealer id`() {
  testFile.writeText("""
          <dealers>
              <Dealer>
                  <Name>Invalid Dealer</Name>
              </Dealer>
          </dealers>
       """.trimIndent())


  val dealerSet = mutableSetOf<Dealer?>()
  assertThrows(IOException::class.java) {
   xmlReader.parse(dealerSet)
  }
 }


 @Test
 fun `parse should handle empty vehicle list`() {
  testFile.writeText("""
          <dealers>
              <Dealer id="DLR001">
                  <Name>Empty Vehicles Dealer</Name>
                  <Vehicles></Vehicles>
              </Dealer>
          </dealers>
       """.trimIndent())


  val dealerSet = mutableSetOf<Dealer?>()
  xmlReader.parse(dealerSet)


  assertEquals(1, dealerSet.size)
  assertEquals(0, dealerSet.first()!!.getVehicleList().size)
 }


 @Test
 fun `parse should throw IOException for missing vehicle type`() {
  testFile.writeText("""
          <dealers>
              <Dealer id="DLR001">
                  <Vehicles>
                      <Vehicle id="VH001">
                          <Make>Toyota</Make>
                      </Vehicle>
                  </Vehicles>
              </Dealer>
          </dealers>
       """.trimIndent())


  val dealerSet = mutableSetOf<Dealer?>()
  assertThrows(IOException::class.java) {
   xmlReader.parse(dealerSet)
  }
 }
}
