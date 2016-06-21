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

}
