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
		readAndDisplayFile("./resources/banner.txt");
    while (true) {
      ActionEnum token = readInputFromUser();
      takeActionForToken(token);
    }
	

  }

  public void takeActionForToken(ActionEnum token) {
    switch (token) {
      case HELP:
        readAndDisplayFile("./resources/instructions.txt");
        break;
      case EMPTY:
        displayShortHelp();
        break;
      case HIGHER:
        break;
      case LOWER:
        break;
      case END:
        break;
    }
  }


  public enum ActionEnum {
    EMPTY, YES, END, HELP, HIGHER, LOWER 
  } 

  /**
    * Here's where we'll get the input from
    * the user, and return a token (an enum) indicating
    * what they intended.
    */
  public static ActionEnum readInputFromUser() {
      System.out.print(" > ");
      String s = "";

      try {
        BufferedReader br = 
          new BufferedReader(new InputStreamReader(System.in));
        s = br.readLine().toLowerCase().trim();
      } catch (IOException ex) {
        System.out.println("error reading line");
      }

      switch (s) {
        case "y":
        case "yes":
          return ActionEnum.YES;
          break;
        case "":
          return ActionEnum.EMPTY;
          break;
        case "higher:
        case "h":
        case "hi":
          return ActionEnum.HIGHER;
          break;
        case "lower:
        case "l":
        case "low":
          return ActionEnum.HIGHER;
          break;
      }
  }


  public static void displayShortHelp() {
    System.out.println("(higher, lower, yes, end)");
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
