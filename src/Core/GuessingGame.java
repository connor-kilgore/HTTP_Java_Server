package Core;

import java.io.IOException;
import java.io.OutputStream;

public class GuessingGame {

    int answer;
    int guesses;
    int number;

    public GuessingGame(String[] parameters) {
        this.number = getNumber(parameters);
        this.answer = getAnswer(parameters);
        this.guesses = getGuesses(parameters);
    }

    public GuessingGame()
    {
        number = -1;
        answer = (int) ((Math.random() * 100) + 1);
        guesses = 7;
    }


    public void sendCustomHTML(String htmlContent, OutputStream clientOutput) throws IOException {
        clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
        clientOutput.write(("ContentType: text/html\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(htmlContent.getBytes());
        clientOutput.write("\r\n".getBytes());
    }

    public void sendGuessHTML(OutputStream clientOutput) throws IOException {
        String hint = getHint(number, answer);
        String html = "<!DOCTYPE html>" +
                "<html><head><title> Java HTTP Core.Server </title></head>" +
                "<body><br><br><div><form method=\"POST\" action=\"#\">" +
                "\t<label for=\"number\">Guess a number between 1-100</label>" +
                "<input type=\"number\" id=\"number\" name=\"number\">" +
                "<input type=\"hidden\" id=\"guesses-left\" name=\"guesses-left\" value=\"" + (guesses - 1) + "\">" +
                "<input type=\"hidden\" id=\"number-to-guess\"" +
                " name=\"number-to-guess\" value=\"" + answer + "\">" +
                "<br><input type=\"submit\"></form>" +
                "<h3>" + hint + "</h3></div></body></html>";

        sendCustomHTML(html, clientOutput);
    }

    public String getEndCondition(String endCond) {
        return "<!DOCTYPE html>" +
                "<html><head><title> Java HTTP Core.Server </title></head>" +
                "<body><br><br><div>" +
                "<h3>" + endCond + "</h3></div></body></html>";
    }

    public void updateGame(OutputStream clientOutput) throws IOException {
        if (number == answer)
            sendCustomHTML(getEndCondition("You Won!"), clientOutput);
        else if (guesses == 0)
            sendCustomHTML(getEndCondition("You Failed!"), clientOutput);
        else
            sendGuessHTML(clientOutput);
    }

    public static int getNumber(String[] parameters) {
        int num = 50;
        for (String parameter : parameters)
            if (parameter.split("=")[0].equals("number"))
                num = Integer.parseInt(parameter.split("=")[1]);

        return num;
    }

    public static int getAnswer(String[] parameters) {
        int num = 50;
        for (String parameter : parameters)
            if (parameter.split("=")[0].equals("number-to-guess"))
                num = Integer.parseInt(parameter.split("=")[1]);

        return num;
    }

    public static int getGuesses(String[] parameters) {
        int num = 7;
        for (String parameter : parameters)
            if (parameter.split("=")[0].equals("guesses-left"))
                num = Integer.parseInt(parameter.split("=")[1]);

        return num;
    }

    public static String getHint(int number, int numberToGuess) {
        if (number == -1)
            return "";
        else if (number == numberToGuess)
            return "Correct!";
        else if (number > numberToGuess)
            return "Guess Lower!";
        else
            return "Guess Higher!";
    }

}
