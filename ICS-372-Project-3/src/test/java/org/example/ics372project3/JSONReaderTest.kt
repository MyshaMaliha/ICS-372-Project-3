package org.example.ics372project3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter

 class JSONReaderTest{
  @Test
  fun testParseValidJSONWithVehicles() {
   val tempFile = File.createTempFile("test_inventory", ".json")
   tempFile.writeText(
    """
          {
            "car_inventory": [
              {
                "dealership_id": "D001",
                "dealer_name": "Best Cars",
                "is_acquisition_enabled": true,
                "vehicle_type": "SUV",
                "vehicle_manufacturer": "Toyota",
                "vehicle_model": "RAV4",
                "vehicle_id": "V1234",
                "price": 25000.0,
                "acquisition_date": 1640995200000,
                "is_loaned": false
              },
              {
                "dealership_id": "D001",
                "dealer_name": "Best Cars",
                "is_acquisition_enabled": true,
                "vehicle_type": "Sedan",
                "vehicle_manufacturer": "Honda",
                "vehicle_model": "Civic",
                "vehicle_id": "V5678",
                "price": 20000.0,
                "acquisition_date": 1640995200000,
                "is_loaned": true
              }
            ]
          }
          """.trimIndent()
   )




   val dealerSet = mutableSetOf<Dealer?>()
   JSONReader(tempFile.absolutePath).parse(dealerSet)




   assertEquals(1, dealerSet.size)
   val dealer = dealerSet.first()!!
   assertEquals("D001", dealer.dealerID)
   assertEquals("Best Cars", dealer.dealerName)
   assertTrue(dealer.isAcquisitionEnabled)
   assertEquals(2, dealer.getVehicleList().size)
  }




  @Test
  fun testParseJSONWithDealerOnlyEntry() {
   val tempFile = File.createTempFile("dealer_only", ".json")
   tempFile.writeText(
    """
          {
            "car_inventory": [
              {
                "dealership_id": "D002",
                "dealer_name": "Only Dealer",
                "is_acquisition_enabled": false
              }
            ]
          }
          """.trimIndent()
   )




   val dealerSet = mutableSetOf<Dealer?>()
   JSONReader(tempFile.absolutePath).parse(dealerSet)




   assertEquals(1, dealerSet.size)
   val dealer = dealerSet.first()!!
   assertEquals("D002", dealer.dealerID)
   assertEquals("Only Dealer", dealer.dealerName)
   assertFalse(dealer.isAcquisitionEnabled)
   assertTrue(dealer.getVehicleList().isEmpty())
  }




  @Test
  fun testParseJSONWithUnknownVehicleType() {
   val tempFile = File.createTempFile("unknown_type", ".json")
   tempFile.writeText(
    """
          {
            "car_inventory": [
              {
                "dealership_id": "D003",
                "dealer_name": "Strange Dealer",
                "is_acquisition_enabled": true,
                "vehicle_type": "Plane",
                "vehicle_manufacturer": "Boeing",
                "vehicle_model": "747",
                "vehicle_id": "V9999",
                "price": 999999.0,
                "acquisition_date": 1640995200000,
                "is_loaned": false
              }
            ]
          }
          """.trimIndent()
   )




   val dealerSet = mutableSetOf<Dealer?>()
   JSONReader(tempFile.absolutePath).parse(dealerSet)




   assertEquals(1, dealerSet.size)
   val dealer = dealerSet.first()!!
   assertEquals("D003", dealer.dealerID)
   assertTrue(dealer.getVehicleList().isEmpty()) // "Plane" should be ignored
  }

 }