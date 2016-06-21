package com.renomad.guess;

/**
  * The data needed for the loop
  */
public class CalcData {
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
