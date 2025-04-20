package org.example.ics372project3

abstract class Vehicle(// Getter methods to retrieve the values of instance variables/attributes
    var vehicleID: String,
    var manufacturer: String,
    var model: String,
    var acquisitionDate: Long,
    var price: Double,
    var type: String, //adding new filed to track loaned vehicle
    var isLoaned: Boolean
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


