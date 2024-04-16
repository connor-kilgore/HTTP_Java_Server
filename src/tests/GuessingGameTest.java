package tests;

import Core.GuessingGame;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class GuessingGameTest {

    @Test
    public void testParametersConstructor() {
        GuessingGame gg = new GuessingGame(new String[]
                {"number=22",
                "number-to-guess=80",
                "guesses-left=5"});

        assertEquals(22, gg.getNumber());
        assertEquals(80, gg.getAnswer());
        assertEquals(4, gg.getGuesses());
    }

    @Test
    public void testParametersDefaultValuesConstructor() {
        GuessingGame gg = new GuessingGame(new String[]
                {"fake-number=22",
                "answer=80",
                "guesses-i-have-left=5"});

        assertEquals(50, gg.getNumber());
        assertEquals(50, gg.getAnswer());
        assertEquals(7, gg.getGuesses());
    }

    @Test
    public void testInitializeConstructor() {
        GuessingGame gg = new GuessingGame(90,
                20,
                3);

        assertEquals(90, gg.getNumber());
        assertEquals(20, gg.getAnswer());
        assertEquals(2, gg.getGuesses());
    }

    @Test
    public void testDefaultConstructor() {
        GuessingGame gg = new GuessingGame();

        assertEquals(-1, gg.getNumber());
        assertEquals(7, gg.getGuesses());
        assertNotEquals(0, gg.getAnswer());
    }

    @Test
    public void testGetHintNoNumber(){
        GuessingGame gg = new GuessingGame(-1, 5, 7);
        assertEquals("", gg.getHint());
    }

    @Test
    public void testGetHintCorrect(){
        GuessingGame gg = new GuessingGame(5, 5, 6);
        assertEquals("Correct!", gg.getHint());
    }

    @Test
    public void testGetHintLowerNum(){
        GuessingGame gg = new GuessingGame(2, 5, 6);
        assertEquals("Guess Higher!", gg.getHint());
    }

    @Test
    public void testGetHintHigherNum(){
        GuessingGame gg = new GuessingGame(8, 5, 6);
        assertEquals("Guess Lower!", gg.getHint());
    }

    @Test
    public void testGetEndConditionNoEnd(){
        GuessingGame gg = new GuessingGame(8, 5, 6);
        assertEquals("", gg.getEndCondition());
    }

    @Test
    public void testGetEndConditionWon(){
        GuessingGame gg = new GuessingGame(5, 5, 6);
        assertEquals("You Won!", gg.getEndCondition());
    }

    @Test
    public void testGetEndConditionLoss(){
        GuessingGame gg = new GuessingGame(8, 5, 0);
        assertEquals("You Failed!", gg.getEndCondition());
    }
}
