package org.example.ics372project3
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

 class InventoryTransferTest{
  private lateinit var dealer1: Dealer
  private lateinit var dealer2: Dealer
  private lateinit var vehicle: SUV
  private lateinit var dealerSet: Set<Dealer>


  @BeforeEach
  fun setup() {
   // Setup dealers and vehicles
    dealer1 = Dealer("123").apply {
    dealerName = "Dealer One"
    addVehicle(SUV("v123", "Toyota", "Highlander", 1625097600000, 30000.0, false))
   }


   dealer2 = Dealer("456").apply {
    dealerName = "Dealer Two"
   }


   // Add dealers to a set
   dealerSet = setOf(dealer1, dealer2)
  }


  @Test
  fun testTransferInventory() {
   val inventoryTransfer = InventoryTransfer()


   // Try to transfer a vehicle from dealer1 to dealer2
   val result = inventoryTransfer.transferVehicle(dealerSet, "123", "456", "v123")


   // Verify the transfer was successful
   assertTrue(result, "The vehicle should be transferred successfully.")


   // Verify dealer2 now has the vehicle
   assertTrue(dealer2.getVehicleList().isNotEmpty(), "Dealer Two should have received the vehicle.")
   assertEquals(1, dealer2.getVehicleList().size, "Dealer Two should have exactly one vehicle.")
   assertEquals("v123", dealer2.getVehicleList()[0].vehicleID, "The vehicle ID should match.")


   // Verify dealer1 no longer has the vehicle
   assertTrue(dealer1.getVehicleList().isEmpty(), "Dealer One should no longer have the vehicle.")
  }


  @Test
  fun testTransferInvalidVehicle() {
   val inventoryTransfer = InventoryTransfer()


   // Try to transfer a non-existing vehicle from dealer1 to dealer2
   val result = inventoryTransfer.transferVehicle(dealerSet, "123", "456", "v999")


   // Verify the transfer fails
   assertFalse(result, "The transfer should fail as the vehicle does not exist.")
  }


  @Test
  fun testTransferToNonAcquiringDealer() {
   // Disable acquisition for dealer2
   dealer2.disableAcquisition()


   val inventoryTransfer = InventoryTransfer()


   // Try to transfer a vehicle to a dealer that is not accepting acquisitions
   val result = inventoryTransfer.transferVehicle(dealerSet, "123", "456", "v123")


   // Verify the transfer fails
   assertFalse(result, "The transfer should fail as Dealer Two is not accepting acquisitions.")
  }


  @Test
  fun testTransferToSameDealer() {
   val inventoryTransfer = InventoryTransfer()


   // Try to transfer a vehicle to the same dealer
   val result = inventoryTransfer.transferVehicle(dealerSet, "123", "123", "v123")


   // Verify the transfer fails
   assertFalse(result, "The transfer should fail as the vehicle cannot be transferred to the same dealer.")
  }

 }