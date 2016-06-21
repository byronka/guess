  package com.renomad.guess;

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
