package com.renomad.guess.Guess;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Guess {

  public static void main() {
    System.out.println("hello");

  }

  /**
    * a utility method to read the entire contents
    * of a file and display them on the screen.
    *
    * @param filename the name of the file we'll read and display
    */
  public static void readAndDisplayFile(String filename) {
    List<String> allLines = new ArrayList<String>();
    try {
      allLines = 
        Files.readAllLines(Paths.get(filename), StandardCharsets.US_ASCII);
    } catch (IOException ex) {
      System.out.printf("Error: couldn't read %s\n", filename);
      System.out.println(ex);
    }
    for (String s : allLines) {
      System.out.println(s);
    }
  }
}