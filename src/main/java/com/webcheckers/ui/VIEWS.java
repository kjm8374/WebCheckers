package com.webcheckers.ui;

/**
 * This enum contains the filenames of .ftl templates used to render pages.
 * <p>
 * These should be used when calling {@code templateEngine.render} to render a page from a .ftl file.
 */
public enum VIEWS {

    GAME_VIEW("game.ftl"),
    HOME_VIEW("home.ftl"),
    SIGNIN_VIEW("signin.ftl"),
    ACCOUNT_VIEW("account-details.ftl");

    private final String text;

    VIEWS(final String text) {
        this.text = text;
    }


    public String getView() {
        return text;
    }
}
