package com.renomad.guess;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.renomad.guess.Guess;

public class GuessTests {


  @Test
  public void parametric_test_of_calcs() {
    try {
    CalcData result = Guess.doCalc(2, 2, ActionEnum.HIGHER, true);
    assertEquals(4, result.current);
    assertEquals(2, result.otherBound);
    } catch (Exception ex) {
      fail("exception thrown");
    }
  }

}
