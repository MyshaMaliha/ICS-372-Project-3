package org.example.ics372project3
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
 class VehicleTest{
  @Test
  fun testVehicleInitialization() {
   val vehicle = SUV("v123", "Toyota", "Highlander", 1625097600000, 30000.0, false)


   assertEquals("v123", vehicle.vehicleID)
   assertEquals("Toyota", vehicle.manufacturer)
   assertEquals("Highlander", vehicle.model)
   assertEquals(30000.0, vehicle.price)
   assertEquals(false, vehicle.isLoaned)
  }
  }
