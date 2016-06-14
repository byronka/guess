package com.renomad.guess;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Guess {

  public static void main(String[] args) {
    System.out.println("hello");
		readAndDisplayFile("./resources/banner.txt");
		System.out.println("Would you like instructions? (yes or no)");
		System.out.print(" > ");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String s = br.readLine().toLowerCase();
			if (s.equals("y") || s.equals("yes")) {
        readAndDisplayFile("./resources/instructions.txt");
			}
		} catch (IOException ex) {
		  System.out.println("error reading line");
		}
	

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

  public static void parseInputAndTakeAction(String input) {
   // to be implemented
  }

  public static void guessHigher(int min, int max) {
   // to be implemented
  }

  public static void guessLower(int min, int max) {
   // to be implemented
  }

}
