package com.webcheckers.ui;

/**
 * This enum contains the names of session attributes. It also contains the names of URL parameters (specifically
 * "gameID".)
 * <p>
 * These should be used when storing data into and retrieving data from the browser session. In the case of URL
 * parameters, they should be used when reading URL parameters from the URL or when redirecting to a destination with
 * a URL parameter.
 */
public enum ATTRIBUTES {

    MESSAGE_ATR("message"),
    MESSAGE_TYPE_ATR("messageType"),
    MESSAGE_TYPE_ERROR_ATR("error"),
    MESSAGE_TYPE_INFO_ATR("info"),
    USERS_LIST_ATR("usersList"),
    CURRENT_USER_ATR("currentUser"),
    TITLE_ATR("title"),
    INFO_ATR("InfoError"),
    GAME_ID_ATR("gameID"),
    RED_PLAYER_ID_ATR("redPlayer"),
    WHITE_PLAYER_ID_ATR("whitePlayer"),
    ACTIVE_COLOR_ATR("activeColor"),
    VIEW_MODE_ATR("viewMode"),
    GAME_BOARD_ATR("board");

    private final String text;

    ATTRIBUTES(final String text) {
        this.text = text;
    }


    public String getAtr() {
        return text;
    }
}
