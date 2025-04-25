package org.example.ics372project3

abstract class Vehicle(// Getter methods to retrieve the values of instance variables/attributes
    var vehicleID: String? = null,
    var manufacturer: String? = null,
    var model: String? = null,
    var acquisitionDate: Long? = null,
    var price: Double? = null,
    var type: String? = null, //adding new filed to track loaned vehicle
    var isLoaned: Boolean? = false
)

internal class SUV  //Constructor
    (vehicleID: String, manufacturer: String, model: String, acquisitionDate: Long, price: Double, isLoaned: Boolean) :
    Vehicle(vehicleID, manufacturer, model, acquisitionDate, price, "suv", isLoaned)

internal class Sedan  //Constructor
    (vehicleID: String, manufacturer: String, model: String, acquisitionDate: Long, price: Double, isLoaned: Boolean) :
    Vehicle(vehicleID, manufacturer, model, acquisitionDate, price, "sedan", isLoaned)

internal class Pickup  //Constructor
    (vehicleID: String, manufacturer: String, model: String, acquisitionDate: Long, price: Double, isLoaned: Boolean) :
    Vehicle(vehicleID, manufacturer, model, acquisitionDate, price, "pickup", isLoaned)

internal class SportsCar  //Constructor
    (vehicleID: String, manufacturer: String, model: String, acquisitionDate: Long, price: Double, isLoaned: Boolean) :
    Vehicle(vehicleID, manufacturer, model, acquisitionDate, price, "sports car", isLoaned)


