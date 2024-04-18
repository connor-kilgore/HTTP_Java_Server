package tests;

import Core.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class GuessingGameRequestTest {
    @Test
    public void testStartGuessingGame() throws IOException, InterruptedException {
        GuessingGameRequest ggr = new GuessingGameRequest();
        GuessingGame gg = new GuessingGame();
        ResponseNode node = ggr.updateGuessingGame(gg);

        StringBuilder str = new StringBuilder();
        for (byte b : node.getContent())
            str.append((char) b);

        assertEquals("<!DOCTYPE html><html><head><title> Java HTTP Core.Server" +
                     " </title></head><body><br><br><div><form method=\"POST\" " +
                     "action=\"#\">\t<label for=\"number\">Guess a number between " +
                     "1-100</label><input type=\"number\" id=\"number\" name=\"number\">" +
                     "<input type=\"hidden\" id=\"guesses-left\" name=\"guesses-left\"" +
                     " value=\"7\"><input type=\"hidden\" id=\"number-to-guess\" " +
                     "name=\"number-to-guess\" value=\"" + gg.getAnswer() + "\"><br><input type=\"submit\">" +
                     "</form><h3></h3></div></body></html>", str.toString());
    }

    @Test
    public void testUpdateGuessingGame() throws IOException, InterruptedException {
        String input = "POST /guess HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=4";

        String[] args = {"-p", "132", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 132)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><div><form method=\"POST\" action=\"#\">\t" +
                         "<label for=\"number\">Guess a number between 1-100</label>" +
                         "<input type=\"number\" id=\"number\" name=\"number\">" +
                         "<input type=\"hidden\" id=\"guesses-left\" name=\"guesses-left\" " +
                         "value=\"3\"><input type=\"hidden\" id=\"number-to-guess\" " +
                         "name=\"number-to-guess\" value=\"50\"><br><input type=\"submit\"></form>" +
                         "<h3>Guess Higher!</h3></div></body></html>", str.toString());
        }
    }

    @Test
    public void testGuessingGameWin() throws IOException, InterruptedException {
        String input = "POST /guess HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=50&number-to-guess=50&guesses-left=4";

        String[] args = {"-p", "133", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 133)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server " +
                         "</title></head><body><br><br><div><h3>You Won!</h3>" +
                         "</div></body></html>", str.toString());
        }
    }

    @Test
    public void testGuessingGameLoss() throws IOException, InterruptedException {
        String input = "POST /guess HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=0";

        String[] args = {"-p", "136", "-r", "foo"};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 136)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server " +
                         "</title></head><body><br><br><div><h3>You Failed!</h3>" +
                         "</div></body></html>", str.toString());
        }
    }
}
