package com.renomad.guess;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Guess {


  public static void main(String[] args) {
		readAndDisplayFile("./resources/banner.txt");
    boolean isGuessing = false;
    int currentGuess = ThreadLocalRandom.current().nextInt(1, 20 + 1);
    while (true) {
      if (isGuessing) {
        System.out.printf("is it %d?\n", currentGuess);
      }
      ActionEnum token = readInputFromUser();
      isGuessing = takeActionForToken(token, isGuessing);
    }
  }


  public static boolean takeActionForToken(ActionEnum token, boolean isGuessing) {
    switch (token) {
      case READY:
        return true;
      case HELP:
        readAndDisplayFile("./resources/instructions.txt");
        break;
      case EMPTY:
        displayShortHelp(isGuessing);
        break;
      case HIGHER:
        break;
      case LOWER:
        break;
      case YES:
        break;
      case END:
        System.out.println("Game over");
        System.exit(0);
        break;
      default:

    }
    return isGuessing;
  }


  public enum ActionEnum {
    EMPTY,  // the user entered nothing.
    READY,
    YES,    
    END,
    HELP,
    HIGHER,
    LOWER,
    BAD_INPUT, // the user entered something unparseable
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
        case "":
          return ActionEnum.EMPTY;
        case "higher":
        case "h":
        case "hi":
          return ActionEnum.HIGHER;
        case "lower":
        case "l":
        case "low":
          return ActionEnum.HIGHER;
        case "end":
        case "e":
          return ActionEnum.END;
        case "i":
        case "instructions":
        case "help":
        case "?":
          return ActionEnum.HELP;
        case "r":
        case "ready":
          return ActionEnum.READY;
      }
      return ActionEnum.BAD_INPUT;
  }


  public static void displayShortHelp(boolean isGuessing) {
    if (isGuessing) {
      System.out.println("(higher, lower, yes, end, ? for help)");
    } else {
      System.out.println("(end, ready, ? for help)");
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
