package Core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class GuessingGame {

    int answer;
    int guesses;
    int number;
    String hint;

    public GuessingGame(String[] parameters) {
        this.number = getParameter(parameters, "number", -1);
        this.answer = getParameter(parameters, "number-to-guess",
                (int) ((Math.random() * 100) + 1));
        this.guesses = getParameter(parameters, "guesses-left", 8) - 1;
    }

    public GuessingGame(int number, int answer, int guesses) {
        this.number = number;
        this.answer = answer;
        this.guesses = guesses - 1;
    }

    public GuessingGame() {
        number = -1;
        answer = (int) ((Math.random() * 100) + 1);
        guesses = 7;
    }

    public int getNumber() {
        return number;
    }

    public int getAnswer() {
        return answer;
    }

    public int getGuesses() {
        return guesses;
    }

    public String getEndCondition() {
        if (number == answer)
            return "You Won!";
        else if (guesses <= 0)
            return "You Failed!";
        else
            return "";
    }

    public static int getParameter(String[] parameters, String search, int defaultVal) {
        int num = defaultVal;
        for (String parameter : parameters)
            if (parameter.split("=")[0].equals(search))
                num = Integer.parseInt(parameter.split("=")[1]);

        return num;
    }

    public String getHint() {
        if (number == -1)
            return "";
        else if (number == answer)
            return "Correct!";
        else if (number > answer)
            return "Guess Lower!";
        else
            return "Guess Higher!";
    }

}
