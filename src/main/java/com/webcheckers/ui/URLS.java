package com.webcheckers.ui;

/**
 * This enum contains the URLs used across the application.
 * <p>
 * These should be used when registering a route to handle a URL (done in {@link WebServer}) and when redirecting to
 * a page.
 */
public enum URLS {
  HOME_URL("/"),
  SIGN_IN_URL("/signin"),
  GAME_URL("/game"),
  CHECK_TURN_URL("/checkTurn"),
  RESIGN_GAME_URL("/resignGame"),
  VALIDATE_MOVE_URL("/validateMove"),
  SUBMIT_TURN_URL("/submitTurn"),
  BACK_UP_MOVE_URL("/backupMove"),
  SIGN_OUT_URL("/signout"),
  ACCOUNT_DETAILS_URL("/account-details"),
  REPLAY_GAME_URL("/replay/game"),
  STOP_REPLAY_GAME_URL("/replay/stopWatching"),
  REPLAY_NEXT_MOVE("/replay/nextTurn"),
  REPLAY_LAST_MOVE("/replay/previousTurn");

  private final String text;

  URLS(final String text) {
    this.text = text;
  }


  public String getURL() {
    return text;
  }
}
