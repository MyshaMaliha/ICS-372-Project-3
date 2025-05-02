package org.example.ics372project3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Stub for Vehicle class used in testing


class DealerTest {

    private lateinit var dealer: Dealer
    private lateinit var vehicle1: Vehicle
    private lateinit var vehicle2: Vehicle

    @BeforeEach
    fun setUp() {
        dealer = Dealer("D123")
        dealer.dealerName = "Test Dealer"
        vehicle1 = SUV("V001", "toyota","model2",45242, 5635.8, false)
        vehicle2 = Sedan("V002", "Ford","model12",452322, 6635.8, false)
    }

    @Test
    fun testInitialAcquisitionEnabled() {
        assertTrue(dealer.isAcquisitionEnabled)
    }

    @Test
    fun testEnableDisableAcquisition() {
        dealer.disableAcquisition()
        assertFalse(dealer.isAcquisitionEnabled)

        dealer.enableAcquisition()
        assertTrue(dealer.isAcquisitionEnabled)
    }

    @Test
    fun testAddVehicleWhenEnabled() {
        val result = dealer.addVehicle(vehicle1)
        assertTrue(result)
        assertEquals(1, dealer.getVehicleList().size)
        assertEquals(vehicle1, dealer.getVehicleList()[0])
    }

    @Test
    fun testAddVehicleWhenDisabled() {
        dealer.disableAcquisition()
        val result = dealer.addVehicle(vehicle1)
        assertFalse(result)
        assertEquals(0, dealer.getVehicleList().size)
    }

    @Test
    fun testRemoveVehicle() {
        dealer.addVehicle(vehicle1)
        dealer.addVehicle(vehicle2)
        assertEquals(2, dealer.getVehicleList().size)

        dealer.removeVehicle(vehicle1)
        assertEquals(1, dealer.getVehicleList().size)
        assertEquals(vehicle2, dealer.getVehicleList()[0])
    }

    @Test
    fun testDealerNameField() {
        assertEquals("Test Dealer", dealer.dealerName)
        dealer.dealerName = "New Name"
        assertEquals("New Name", dealer.dealerName)
    }
}
