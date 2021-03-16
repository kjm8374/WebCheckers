package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostValidateMoveRouteTest {
    private Gson gson = new Gson();

    /**
     * Tests the constructor
     */
    @Test
    public void ConstructorTest() {
        assertDoesNotThrow(PostReplayNextRoute::new);
    }

    /**
     * Tests if there is no valid next move to make
     */
    @Test
    public void ValidMoveTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);


        Move move = new Move(new Position(1, 3), new Position(0, 2));


        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("21");

        PlayerLobby lobby = mock(PlayerLobby.class);

        GameLobby game = mock(GameLobby.class);

        when(game.isGameDone()).thenReturn(false);
        when(request.queryParams("actionData")).thenReturn(gson.toJson(move));

        when(game.getActiveBoard()).thenReturn(mock(BoardView.class));
        MoveValidator validator = mock(MoveValidator.class);

        JsonObject json = new JsonObject();
        json.addProperty("type", "INFO");
        json.addProperty("text", "true");

        when(validator.ValidateMove(move, game.getActiveColor(), game.getActiveBoard())).thenReturn(json);

        when(game.getValidator()).thenReturn(validator);

        when(lobby.getGame(21)).thenReturn(game);


        assertEquals(json, new PostValidateMoveRoute(lobby).handle(request, response));
    }

    /**
     * Tests if there is no valid next move to make
     */
    @Test
    public void InvalidMoveTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);


        Move move = new Move(new Position(1, 3), new Position(0, 2));


        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("21");

        PlayerLobby lobby = mock(PlayerLobby.class);

        GameLobby game = mock(GameLobby.class);

        when(game.isGameDone()).thenReturn(false);
        when(request.queryParams("actionData")).thenReturn(gson.toJson(move));

        when(game.getActiveBoard()).thenReturn(mock(BoardView.class));
        MoveValidator validator = mock(MoveValidator.class);

        JsonObject json = new JsonObject();
        json.addProperty("type", "INFO");
        json.addProperty("text", "false");

        when(validator.ValidateMove(move, game.getActiveColor(), game.getActiveBoard())).thenReturn(json);

        when(game.getValidator()).thenReturn(validator);

        when(lobby.getGame(21)).thenReturn(game);


        assertEquals(json, new PostValidateMoveRoute(lobby).handle(request, response));
    }

    /**
     * Tests functionality relating to a game being over
     */
    @Test
    public void GameOverTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);


        Move move = new Move(new Position(1, 3), new Position(0, 2));


        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("21");

        PlayerLobby lobby = mock(PlayerLobby.class);

        GameLobby game = mock(GameLobby.class);

        when(game.isGameDone()).thenReturn(true);
        when(request.queryParams("actionData")).thenReturn(gson.toJson(move));

        when(game.getActiveBoard()).thenReturn(mock(BoardView.class));
        MoveValidator validator = mock(MoveValidator.class);

        JsonObject json = new JsonObject();
        json.addProperty("type", "ERROR");
        json.addProperty("text", " You can't make a move because your opponent already resigned. Click my home to enter a game.");

        when(validator.ValidateMove(move, game.getActiveColor(), game.getActiveBoard())).thenReturn(json);

        when(game.getValidator()).thenReturn(validator);

        when(lobby.getGame(21)).thenReturn(game);


        assertEquals(json, new PostValidateMoveRoute(lobby).handle(request, response));
    }
}
