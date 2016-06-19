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
    int currentGuess = ThreadLocalRandom.current().nextInt(1, 20 + 1);

    while (true) {
      ActionEnum token = readInputFromUser();
      boolean startGame = takeActionForTokenBeforeGuessing(token);
      if (startGame) break;
    }

    // at this point, we're off to the races!
    GuessLoop(currentGuess, 0, 0);

  }


  public static void GuessLoop(int currentGuess, int min, int max) {
    System.out.printf("is it %d?\n", currentGuess);
    ActionEnum token = readInputFromUser();
      switch (token) {
        case HELP:
          readAndDisplayFile("./resources/instructions.txt");
          break;
        case EMPTY:
          displayShortHelpInGame();
          break;
        case HIGHER:
          GuessLoop(currentGuess * 2, min, currentGuess * 2);
          break;
        case LOWER:
          GuessLoop(Math.abs(min-max)/2, min, Math.abs(min-max)/2);
          break;
        case OOPS:
          //TODO
          GuessLoop(currentGuess, min, max);
          break;
        case YES:
          System.out.println("Fantastic!");
        case END:
          System.out.println("Game over");
          System.exit(0);
          break;
        default:
          System.out.println("invalid input");
      }
      GuessLoop(currentGuess, min, max);
  }

  public static Blah CrunchBrunch(Blah bah) {

  }

  /**
    * The data needed for the loop
    */
  private class CalcData {
    public Blah(int current, int min, int max) {
      this.current = current;
      this.min = min;
      this.max = max;
    }

    public final int current;
    public final int min;
    public final int max;
  }



  /**
    * parses the user input before they start the game. 
    * @param a token for what action to take
    * @return whether to start the game
    */
  public static boolean takeActionForTokenBeforeGuessing(ActionEnum token) {
    switch (token) {
      case READY:
        return true;
      case HELP:
        readAndDisplayFile("./resources/instructions.txt");
        break;
      case EMPTY:
        displayShortHelpOutOfGame();
        break;
      case END:
        System.out.println("Game over");
        System.exit(0);
        break;
      default:
        return false;
    }
    return false;  // the default - they have to enter "ready" to move into game mode
  }


  /**
    * The various user actions available
    */
  public enum ActionEnum {
    EMPTY,  // the user entered nothing.
    READY,
    YES,    
    END,
    HELP,
    HIGHER,
    LOWER,
    BAD_INPUT, // the user entered something unparseable
    OOPS  // if ther user wants to undo their previous entry
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
    return analyzeInput(s);
  }


  /**
    * simply analyzes the string for which token it relates to
    */
  public static ActionEnum analyzeInput(String s) {
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


  public static void displayShortHelpOutOfGame() {
    System.out.println("(end, ready, ? for help)");
  }

  public static void displayShortHelpInGame() {
    System.out.println("(higher, lower, yes, end, oops, ? for help)");
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
