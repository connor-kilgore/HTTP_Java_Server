package tests;

import Core.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuessingGameResponseTest {
    @Test
    public void testStartGuessingGame() {
        GuessingGame gg = new GuessingGame();
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<input type=\"hidden\" id=\"guesses-left\"" +
                          " name=\"guesses-left\" value=\"7\">"));
    }

    @Test
    public void testUpdateGuessingGame() {
        GuessingGame gg = new GuessingGame(25, 50, 5);
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<input type=\"hidden\" id=\"guesses-left\" name=\"guesses-left\" value=\"4\">"));
    }

    @Test
    public void testLowerGuess() {
        GuessingGame gg = new GuessingGame(25, 50, 5);
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h3>Guess Higher!</h3>"));
    }

    @Test
    public void testHigherGuess() {
        GuessingGame gg = new GuessingGame(75, 50, 5);
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h3>Guess Lower!</h3>"));
    }

    @Test
    public void testWin() {
        GuessingGame gg = new GuessingGame(50, 50, 5);
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h3>You Won!</h3>"));
    }

    @Test
    public void testLoss() {
        GuessingGame gg = new GuessingGame(0, 50, 0);
        ResponseNode node = new GuessingGameResponse().updateGuessingGame(gg);

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h3>You Failed!</h3>"));
    }
}
