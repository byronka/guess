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
import com.renomad.guess.ActionEnum;
import com.renomad.guess.CalcData;

public class Guess {

	// Used for storing the last data, so we can undo
	private static CalcData lastData = null;

	// The upper and lower bounds for guessing.
	private static final int MAX_BOUND = 1000;
	private static final int MIN_BOUND = 1;

	public static void main(String[] args) {
		readAndDisplayFile("./resources/banner.txt");
		int currentGuess = ThreadLocalRandom.current().nextInt(1, 20 + 1);

		while (true) {
			ActionEnum token = readInputFromUser();
			boolean startGame = takeActionForTokenBeforeGuessing(token);
			if (startGame)
				break;
		}

		// at this point, we're off to the races!
		// start the guessing with doubling and halving. Later, switch to use
		// midpoints.
		userInputAndTakeAction(currentGuess, currentGuess, null, true);

	}

	public static ActionEnum getInputFromUser(int currentGuess) {
		System.out.printf("is it %d?%n", currentGuess);
		ActionEnum token = readInputFromUser();
		return token;
	}

	/**
	 * here we get some input from the user and take an action.
	 */
	public static void userInputAndTakeAction(int currentGuess, int otherBound, ActionEnum direction,
			boolean isFirstPart) {
		ActionEnum token = getInputFromUser(currentGuess);
		switch (token) {
		case HELP:
			readAndDisplayFile("./resources/instructions_while_playing.txt");
			userInputAndTakeAction(currentGuess, otherBound, direction, isFirstPart);
			break;
		case EMPTY:
			displayShortHelpInGame();
			userInputAndTakeAction(currentGuess, otherBound, direction, isFirstPart);
			break;
		case YES:
			System.out.println("Fantastic!");
		case END:
			System.out.println("Game over");
			System.exit(0);
			break;
		case HIGHER:
		case LOWER:
			CalcData result = null;
			try {
				result = doCalc(token, currentGuess, otherBound, direction, isFirstPart);
			} catch (Exception ex) {
				// no exceptions should ever happen - they are merely an
				// artifact
				// of the development process. They are to help with debugging.
				System.out.println(ex);
			}
			userInputAndTakeAction(result.current, result.otherBound, result.direction, result.isFirstPart);
			break;
		case OOPS:
			if (lastData != null) {
				userInputAndTakeAction(lastData.current, lastData.otherBound, lastData.direction, lastData.isFirstPart);
			}
			System.out.println("cannot oops yet, you haven't done anything!");
		default:
			System.out.println("Invalid input");
			userInputAndTakeAction(currentGuess, otherBound, direction, isFirstPart);
		}
	}

	/**
	 * where the rubber hits the road for the calculation, The algorithm goes
	 * like this: if we are given a max that is 0, it means the user told us to
	 * guess higher, and we're doubling each time.
	 * 
	 * @param isFirstPart
	 *            - our algorithm is this: at the onset, we scale upwards or
	 *            downwards by doubling or halving. Only once we switch
	 *            directions do we start finding midpoints. So, this switches
	 *            the algorithm we use.
	 */
	public static CalcData doCalc(ActionEnum recentChoice, int currentGuess, int otherBound, ActionEnum prevDirection,
			boolean isFirstPart) throws Exception {
		// record state if the user says "oops!"
		lastData = new CalcData(currentGuess, otherBound, prevDirection, isFirstPart);

		// this next line is a little tricky: we're trying to stay true until
		// they switch prevDirection. Once isFirstPart goes false, it will stay
		// false.
		// Simply - if the user starts out heading higher, isFirstPart should
		// stay true until they switch prevDirection and then never leave false.
		// And,
		// vice-versa for starting lower.
		isFirstPart = prevDirection == null ? isFirstPart : recentChoice == prevDirection && isFirstPart;

		// doubling or halving at this point
		if (isFirstPart) {
			if (recentChoice == ActionEnum.HIGHER) {
				int doubleGuess = (currentGuess * 2) > MAX_BOUND ? MAX_BOUND : currentGuess * 2;
				return new CalcData(doubleGuess, currentGuess, recentChoice, isFirstPart);
			} else if (recentChoice == ActionEnum.LOWER) {
				int halveGuess = (currentGuess / 2) <= MIN_BOUND ? 1 : currentGuess / 2;
				return new CalcData(halveGuess, currentGuess, recentChoice, isFirstPart);
			} else {
				throw new Exception("error - only option should be higher or lower");
			}
		} else {
			// midpoints at this point. this is where we start using otherBound
			// distance to the midpoint
			int dist = (int) Math.ceil(Math.abs(((float) currentGuess - otherBound) / 2));
			if (recentChoice == ActionEnum.HIGHER) {
				return new CalcData(currentGuess + dist, currentGuess, recentChoice, false);
			} else if (recentChoice == ActionEnum.LOWER) {
				return new CalcData(currentGuess - dist, currentGuess, recentChoice, false);
			} else {
				throw new Exception("error - only option should be higher or lower");
			}
		}
	}

	/**
	 * Handle the things that don't do calculations
	 * 
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
	 * parses the user input before they start the game.
	 * 
	 * @param a
	 *            token for what action to take
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
		return false; // the default - they have to enter "ready" to move into
						// game mode
	}

	/**
	 * Here's where we'll get the input from the user, and return a token (an
	 * enum) indicating what they intended.
	 */
	public static ActionEnum readInputFromUser() {
		System.out.print(" > ");
		String s = "";

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.US_ASCII));
			s = br.readLine();
			if (s == null) {
				throw new Exception("readLine returned null");
			}
			s = s.toLowerCase().trim();
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
	 * a utility method to read the entire contents of a file and display them
	 * on the screen.
	 *
	 * @param filename
	 *            the name of the file we'll read and display
	 */
	private static void readAndDisplayFile(String filename) {
		List<String> allLines = new ArrayList<String>();
		try {
			allLines = Files.readAllLines(Paths.get(filename), StandardCharsets.US_ASCII);
		} catch (IOException ex) {
			System.out.printf("Error: couldn't read %s%n", filename);
			System.out.println(ex);
		}
		for (String s : allLines) {
			System.out.println(s);
		}
	}

}
