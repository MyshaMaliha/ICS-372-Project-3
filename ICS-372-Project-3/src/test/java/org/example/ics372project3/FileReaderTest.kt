package org.example.ics372project3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

 class FileReaderTest{


  private lateinit var fileReader: FileReader


  @BeforeEach
  fun setup() {
   fileReader = FileReader()
  }


  @Test
  fun testReadFile() {
   val filePath = "C:\\git\\ICS-372-Project-3\\ICS-372-Project-3\\vehicle.csv" // Set to a valid test file path
   val content = fileReader.readFile(filePath)


   // Check that the content is not empty
   assertTrue(content.isNotEmpty(), "The content of the file should not be empty.")
  }
 }


class FileReader {
 fun readFile(filePath: String): String {
  val file = File(filePath)
  return if (file.exists()) {
   file.readText() // Read the content of the file as a string
  } else {
   ""
  }
 }

}
