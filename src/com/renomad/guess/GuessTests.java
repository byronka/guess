package com.renomad.guess;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.renomad.guess.Guess;

public class GuessTests {


  // what happens when the computer starts out low, and the user
  // starts out requesting higher each time?
  @Test
  public void test_doubling() {
    try {
      CalcData result = Guess.doCalc(2, 2, ActionEnum.HIGHER, true);
      assertEquals(4, result.current);
      assertEquals(2, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.HIGHER, true);
      assertEquals(8, result.current);
      assertEquals(4, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.HIGHER, true);
      assertEquals(16, result.current);
      assertEquals(8, result.otherBound);
    } catch (Exception ex) {
      fail("exception thrown");
    }
  }


  // what happens when we start out with a higher number, like 18, 
  // and start out halving each time?
  @Test
  public void test_halving() {
    try {
      CalcData result = Guess.doCalc(18, 18, ActionEnum.LOWER, true);
      assertEquals(9, result.current);
      assertEquals(18, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.LOWER, true);
      assertEquals(4, result.current);
      assertEquals(9, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.LOWER, true);
      assertEquals(2, result.current);
      assertEquals(4, result.otherBound);
    } catch (Exception ex) {
      fail("exception thrown");
    }
  }


  // what happens when the user decides the computer has
  // gone over their choice (let's say it's 51), and tells it to switch
  // to the other direction? it goes from calculating
  // doubles / halves to calculating midpoints.
  @Test
  public void test_switching_to_midpoints() {
    try {
      CalcData result = Guess.doCalc(30, 15, ActionEnum.HIGHER, true);
      assertEquals(60, result.current);
      assertEquals(30, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.LOWER, false);
      assertEquals(45, result.current);
      assertEquals(60, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.HIGHER, false);
      assertEquals(53, result.current);
      assertEquals(45, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.LOWER, false);
      assertEquals(49, result.current);
      assertEquals(53, result.otherBound);
      result = Guess.doCalc(result.current, result.otherBound, ActionEnum.HIGHER, false);
      assertEquals(51, result.current);
      assertEquals(49, result.otherBound);
    } catch (Exception ex) {
      fail("exception thrown");
    }
  }

  // Here, we'll test every possible permutation in the game. The game
  // can start with any number, randomly, between 0 and 20 exclusive, and
  // the user can guess any number between 1 and 1000, inclusive.  That means
  // there are 19 * 1000 possible permutations.  We'll test that all of them
  // are reachable.
  @Test
  public void test_every_possibility_1_to_1000() {
    // TODO
  }


  // what happens if the user says "higher" when we're
  // guessing 1000?
  @Test
  public void test_higher_at_upper_bound() {
    // TODO
  }

  
  // what happens if the user says "lower" when we're
  // guessing 1?
  @Test
  public void test_lower_at_lower_bound() {
    // TODO
  }

  // what happens if the user made a mistake and figured
  // it out too late, and now we're doing midpoints and
  // cannot escape to where they wanted to go?  Yes, it's
  // an edge case and sort of user error, because one
  // of the times they said "higher" or "lower" they were 
  // wrong, but they shouldn't be prevented from getting
  // there.  Perhaps once the system sees them guessing continuously
  // past a bound, it should switch back to doubling/halving.
  @Test
  public void test_switching_back_to_firstpart() {
    // TODO
  }


  // other tests:
  //   what happens if the user presses ctrl-c or ctrl-d while playing?
}
