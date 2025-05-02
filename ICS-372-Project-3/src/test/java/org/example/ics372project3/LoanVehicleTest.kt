package org.example.ics372project3
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


 class LoanVehicleTest{
  @Test
  fun testLoanVehicle() {
   val dealer = Dealer("123").apply {
    addVehicle(SUV("v123", "Toyota", "Highlander", 1625097600000, 30000.0, false))
   }
   val loanVehicle = LoanVehicle()


   // Loan a vehicle
   val result = loanVehicle.loanVehicle(dealer, "v123")
   assertTrue(result, "Vehicle should be loaned successfully")


   // Ensure the vehicle is marked as loaned
   val vehicle = dealer.getVehicleList().firstOrNull { it.vehicleID == "v123" }
   assertTrue(vehicle?.isLoaned == true, "The vehicle should be marked as loaned")
  }


  @Test
  fun testReturnVehicle() {
   val dealer = Dealer("123").apply {
    addVehicle(SUV("v123", "Toyota", "Highlander", 1625097600000, 30000.0, true))
   }
   val loanVehicle = LoanVehicle()


   // Return the loaned vehicle
   val result = loanVehicle.returnVehicle(dealer, "v123")
   assertTrue(result, "Vehicle should be returned successfully")


   // Ensure the vehicle is marked as not loaned
   val vehicle = dealer.getVehicleList().firstOrNull { it.vehicleID == "v123" }
   assertFalse(vehicle?.isLoaned == true, "The vehicle should no longer be marked as loaned")
  }

 }