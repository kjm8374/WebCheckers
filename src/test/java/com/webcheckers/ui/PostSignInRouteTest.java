
package com.webcheckers.ui;

import com.webcheckers.application.AccountManager;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.UsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Halle Masaryk
 */
@Tag("UI-tier")
public class PostSignInRouteTest {

    private static final String NAME = "Halle Masaryk";
    private static final String NAME2 = "Not Halle Masaryk";
    private static final String ALL_SPACES = "                     ";
    private static final String ALL_CHARACTERS = "%&*^@$$&%@$%@#%";
    private static final String NUMBER_NAME = "My name contains the number 1";

    private PostSignInRoute CuT;
    private PlayerLobby playerLobby;

    private Request request;
    private Response response;
    private TemplateEngine engine;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);
        AccountManager accountManager = mock(AccountManager.class);

        playerLobby = new PlayerLobby();
        //Should I put whens here
        CuT = new PostSignInRoute(playerLobby, accountManager, engine);
    }

    @Test
    public void bad_name_1() {
        when(request.queryParams(eq(PostSignInRoute.NAME_PARAM))).thenReturn(ALL_SPACES);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        try {
            CuT.handle(request, response);
            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATR, GetSignInRoute.SIGNIN_MSG);
            testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_TYPE_ATR, PostSignInRoute.ERROR_TYPE);
            testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_ATR, UsernameValidator.USERNAME_REQUIREMENTS);

            testHelper.assertViewName(PostSignInRoute.VIEW_NAME);

        } catch (HaltException e) {
            System.out.print("Redirected");
        }
    }

    @Test
    public void bad_name_2() {
        when(request.queryParams(eq(PostSignInRoute.NAME_PARAM))).thenReturn(ALL_CHARACTERS);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATR, GetSignInRoute.SIGNIN_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_USER_ATR, null);
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_TYPE_ATR, PostSignInRoute.ERROR_TYPE);
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_ATR, UsernameValidator.USERNAME_REQUIREMENTS);
        //Do I need to put another testHelper here
        testHelper.assertViewName(PostSignInRoute.VIEW_NAME);
        //Is this the right viewName

    }

    @Test
    public void taken_name() {
        //What to do for a taken name
        playerLobby.addUsername(NAME);
        when(request.queryParams(eq(PostSignInRoute.NAME_PARAM))).thenReturn(NAME);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATR, GetSignInRoute.SIGNIN_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_USER_ATR, null);
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_TYPE_ATR, PostSignInRoute.ERROR_TYPE);
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_ATR, PostSignInRoute.UNAVAILABLE_USERNAME);

        testHelper.assertViewName(PostSignInRoute.VIEW_NAME);


    }

    @Test
    public void good_name_1() {
        when(request.queryParams(eq(PostSignInRoute.NAME_PARAM))).thenReturn(NUMBER_NAME);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        });


    }

    @Test
    public void good_name_2() {
        when(request.queryParams(eq(PostSignInRoute.NAME_PARAM))).thenReturn(NAME2);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        });


    }


}
