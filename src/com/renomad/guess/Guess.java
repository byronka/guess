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


  //Used for storing the last data, so we can undo
  private static CalcData lastData = null;

  public static void main(String[] args) {
		readAndDisplayFile("./resources/banner.txt");
    int currentGuess = ThreadLocalRandom.current().nextInt(1, 20 + 1);

    while (true) {
      ActionEnum token = readInputFromUser();
      boolean startGame = takeActionForTokenBeforeGuessing(token);
      if (startGame) break;
    }

    // at this point, we're off to the races!
    // start the guessing with doubling and halving.  Later, switch to use midpoints.
    guessLoop(currentGuess, currentGuess, null, true );

  }


  /**
    * we want to loop, suggesting a guess, and then taking the user's action.
    * we also want to separate out the calculation from the user-input portions.
    * so we broke this into two pieces - user interface - guessLoop, and calcs - doGuess
    *
    * also, our algorithm depends on setting an initial direction and then noticing 
    * when that direction changes.  And, we don't want to use globals, because 
    * too big scope == the suck.
    */
  public static void guessLoop(int currentGuess, int otherBound, ActionEnum direction, boolean firstPart) {
    System.out.printf("is it %d?%n", currentGuess);
    ActionEnum token = readInputFromUser();
    if (handleNonCalcs(token)) {
      guessLoop(currentGuess, otherBound, direction, firstPart);
    } else if (token == ActionEnum.OOPS) {
      guessLoop(lastData.current, lastData.otherBound, lastData.direction, lastData.firstPart);
    } else { // this is where we handle HIGHER or LOWER
      lastData = new CalcData(currentGuess, otherBound, direction, firstPart);
      //this next line is a little tricky: we're trying to stay true until
      // they switch direction.  Once firstPart goes false, it will stay false.
      // Simply - if the user starts out heading higher, firstPart should
      // stay true until they switch direction and then never leave false.  And,
      // vice-versa for starting lower.
      firstPart = direction != null ? token == direction && firstPart : firstPart;
      direction = token;
      doGuess(currentGuess, otherBound, direction, firstPart);
    }
  }


  /**
    * where the rubber hits the road for the calculation, 
    * The algorithm goes like this:
    * if we are given a max that is 0, it means the user told us
    * to guess higher, and we're doubling each time.
    * 
    * @param firstPart - our algorithm is this: at the onset, we
    *     scale upwards or downwards by doubling or halving.  Only once
    *     we switch directions do we start finding midpoints.  So, this
    *     switches the algorithm we use.
    */
  public static void doGuess(int currentGuess, int otherBound, ActionEnum direction, boolean firstPart) {
    // doubling or halving at this point
    if (firstPart) {
      if (direction == ActionEnum.HIGHER) {
        guessLoop(currentGuess * 2, currentGuess, direction, firstPart);
      } else if (direction == ActionEnum.LOWER) {
        guessLoop(currentGuess / 2, currentGuess, direction, firstPart);
      } else {
        System.out.println("error - only option should be higher or lower");
      }
    } else {
        // midpoints at this point.  this is where we start using otherBound
      if (direction == ActionEnum.HIGHER) {
        guessLoop(currentGuess + Math.abs(currentGuess - otherBound)/2, currentGuess, direction, false);
      } else if (direction == ActionEnum.LOWER) {
        guessLoop(currentGuess - Math.abs(currentGuess - otherBound)/2, currentGuess, direction, false);
      } else {
        System.out.println("error - only option should be higher or lower");
      }
    }
  }


  /**
    * Handle the things that don't do calculations
    * @return if true, loop without performing calcs
    */
  public static boolean handleNonCalcs(ActionEnum token) {
      switch (token) {
        case HELP:
          readAndDisplayFile("./resources/instructions.txt");
          break;
        case EMPTY:
          displayShortHelpInGame();
          break;
        case YES:
          System.out.println("Fantastic!");
        case END:
          System.out.println("Game over");
          System.exit(0);
          break;
        default:
          return false;
      }
      return true;

  }


  /**
    * The data needed for the loop
    */
  public static class CalcData {
    public CalcData(int current, int otherBound, ActionEnum direction, boolean firstPart) {
      this.current = current;
      this.otherBound = otherBound;
      this.direction = direction;
      this.firstPart = firstPart;
    }

    public final int current;
    public final int otherBound;
    public final ActionEnum direction;
    public final boolean firstPart;
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
        new BufferedReader(new InputStreamReader(System.in, StandardCharsets.US_ASCII) );
      s = br.readLine();
      if (s == null) {
        throw new Exception("readLine returned null");   
      }
      s.toLowerCase().trim(); 
    } catch (IOException ex) {
      System.out.println("error reading line");
    } catch (Exception ex) {
      System.out.println(ex);
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
        return ActionEnum.LOWER;
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
      case "o":
      case "oops":
      case "oop":
        return ActionEnum.OOPS;
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
      System.out.printf("Error: couldn't read %s%n", filename);
      System.out.println(ex);
    }
    for (String s : allLines) {
      System.out.println(s);
    }
  }

}
