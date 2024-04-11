package tests;

import Core.GuessingGame;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GuessingGameTest {

    @Test
    public void testSendEndCondition() {
        GuessingGame gg = new GuessingGame();
        assertEquals("<!DOCTYPE html><html><head><title>" +
                " Java HTTP Core.Server </title></head><body><br><br>" +
                "<div><h3>You Won!</h3></div></body></html>",
                gg.getEndCondition("You Won!"));
    }

    @Test
    public void testGetNumber() {
        String[] parameters = {"number=22", "other=5"};
        assertEquals(22, GuessingGame.getNumber(parameters));
    }

    @Test
    public void testGetNumberNoInput() {
        String[] parameters = {"fake-number=22", "other=5"};
        assertEquals(50, GuessingGame.getNumber(parameters));
    }

    @Test
    public void testGetAnswer() {
        String[] parameters = {"number-to-guess=22", "other=5"};
        assertEquals(22, GuessingGame.getAnswer(parameters));
    }

    @Test
    public void testGetAnswerNoInput() {
        String[] parameters = {"answer=22", "other=5"};
        assertEquals(50, GuessingGame.getAnswer(parameters));
    }

    @Test
    public void testGetGuesses() {
        String[] parameters = {"guesses-left=22", "other=5"};
        assertEquals(22, GuessingGame.getGuesses(parameters));
    }

    @Test
    public void testGetGuessesNoInput() {
        String[] parameters = {"answer=22", "other=5"};
        assertEquals(7, GuessingGame.getGuesses(parameters));
    }

    @Test
    public void testGetHintNoNumber(){
        int number = -1;
        int answer = 5;
        assertEquals("", GuessingGame.getHint(number, answer));
    }

    @Test
    public void testGetHintCorrect(){
        int number = 5;
        int answer = 5;
        assertEquals("Correct!", GuessingGame.getHint(number, answer));
    }

    @Test
    public void testGetHintLowerNum(){
        int number = 2;
        int answer = 5;
        assertEquals("Guess Higher!", GuessingGame.getHint(number, answer));
    }

    @Test
    public void testGetHintHigherNum(){
        int number = 8;
        int answer = 5;
        assertEquals("Guess Lower!", GuessingGame.getHint(number, answer));
    }
}
