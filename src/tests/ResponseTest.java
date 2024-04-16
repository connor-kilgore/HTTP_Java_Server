package tests;

import Core.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        Socket client = new Socket();
        RequestParser rp = new ClientRequestParser(client);
        String root = ".";
        Response response = new ClientResponse(client, rp, root);

        assertEquals(client, response.getClientSocket());
        assertEquals(rp, response.getRequestParser());
        assertEquals(root, response.getRoot());
    }

    @Test
    public void testGenerateResponse() throws IOException {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html", "Host: localhost"};

        assertEquals("""
                        HTTP/1.1 200 OK
                        ContentType: text/html
                        Host: localhost
                                                
                        """,
                ClientResponse.convertBytesToString
                        (ClientResponse.generateResponse
                                (status, headers, "".getBytes())));

        status = "HTTP/1.1 404 Not Found";

        assertEquals("""
                        HTTP/1.1 404 Not Found
                        ContentType: text/html
                        Host: localhost
                                                
                        """,
                ClientResponse.convertBytesToString(
                        ClientResponse.generateResponse
                                (status, headers, "".getBytes())));
    }

    @Test
    public void testCombineHeadWithContent() {
        byte[] head = {1, 2, 3};
        byte[] content = {4, 5, 6};
        byte[] answer = {1, 2, 3, 4, 5, 6};
        assertEquals(Arrays.toString(answer),
                Arrays.toString(ClientResponse.combineHeadWithContent
                        (head, content)));
    }

    @Test
    public void testSendNotFound() throws IOException, InterruptedException {
        String input = "GET /not-real HTTP/1.1\r\nHost: localhost\r\n\r\n\r\n";

        Server server = new Server(129, new SocketHandlerFactory(),
                new ClientResponseFactory("."), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 129)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("""
                    HTTP/1.1 404 Not Found
                    ContentType: text/html

                    <b>Error 404 (File Not Found)</b>
                    """, str.toString());
        }
    }

    @Test
    public void testSendHomePage() throws IOException, InterruptedException {
        String input = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";

        Server server = new Server(130, new SocketHandlerFactory(),
                new ClientResponseFactory("."), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 130)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();
            File content = new File("index.html");

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                    "ContentType: text/html\n" +
                    "\n" +
                    Files.readString(content.toPath()), str.toString());
        }
    }

    @Test
    public void testSendFile() throws IOException, InterruptedException {
        String input = "GET /website/no-index/test.txt HTTP/1.1\r\n" +
                "Host: localhost\r\n\r\n";

        Server server = new Server(132, new SocketHandlerFactory(),
                new ClientResponseFactory("."), new ClientRPFactory());
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

            assertEquals("""
                    HTTP/1.1 200 OK
                    ContentType: text/plain

                    I am a test file.""", str.toString());
        }
    }

    @Test
    public void testUpdateGuessingGame() throws IOException, InterruptedException {
        String input = "POST /guess HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=4";

        Server server = new Server(134, new SocketHandlerFactory(),
                new ClientResponseFactory("foo"), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 134)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                    "ContentType: text/html\n" +
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

        Server server = new Server(136, new SocketHandlerFactory(),
                new ClientResponseFactory("foo"), new ClientRPFactory());
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

            assertEquals("<!DOCTYPE html><html><head><title> " +
                    "Java HTTP Core.Server </title></head><body><br><br>" +
                    "<div><h3>You Won!</h3></div></body></html>", str.toString());
        }
    }

    @Test
    public void testGuessingGameLoss() throws IOException, InterruptedException {
        String input = "POST /guess HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=0";

        Server server = new Server(136, new SocketHandlerFactory(),
                new ClientResponseFactory("foo"), new ClientRPFactory());
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

            assertEquals("<!DOCTYPE html><html><head><title> " +
                    "Java HTTP Core.Server </title></head><body><br><br>" +
                    "<div><h3>You Failed!</h3></div></body></html>", str.toString());
        }
    }

    @Test
    public void testPOSTFallback() throws IOException, InterruptedException {
        String input = "POST /hello HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=4";

        Server server = new Server(135, new SocketHandlerFactory(),
                new ClientResponseFactory("foo"), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 135)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();
            File content = new File("website/hello/index.html");

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                    "ContentType: text/html\n" +
                    "\n" +
                    Files.readString(content.toPath()), str.toString());
        }
    }

    @Test
    public void testSendGuess() throws IOException, InterruptedException {
        GuessingGame gg = new GuessingGame();

        assertEquals("HTTP/1.1 200 OK\n" +
                        "ContentType: text/html\n" +
                        "\n" +
                        "<!DOCTYPE html><html><head><title> " +
                        "Java HTTP Core.Server </title></head><body>" +
                        "<br><br><div><form method=\"POST\" action=\"#\">\t" +
                        "<label for=\"number\">Guess a number between 1-100</label>" +
                        "<input type=\"number\" id=\"number\" name=\"number\">" +
                        "<input type=\"hidden\" id=\"guesses-left\" " +
                        "name=\"guesses-left\" value=\"7\"><input type=\"hidden\" " +
                        "id=\"number-to-guess\" name=\"number-to-guess\" value=\"" +
                        gg.getAnswer() + "\"><br>" + "<input type=\"submit\"></form><h3></h3></div></body></html>",
                ClientResponse.convertBytesToString(ClientResponse.generateGuess(gg)));
    }

    @Test
    public void testSendHello() throws IOException, InterruptedException {
        String input = "GET /hello HTTP/1.1\r\nHost: localhost\r\n\r\n";

        Server server = new Server(133, new SocketHandlerFactory(),
                new ClientResponseFactory("foo"), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 133)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();
            File content = new File("website/hello/index.html");

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                    "ContentType: text/html\n" +
                    "\n" +
                    Files.readString(content.toPath()), str.toString());
        }
    }

    @Test
    public void testSendDirectory() throws IOException, InterruptedException {
        String input = "GET /website HTTP/1.1\r\nHost: localhost\r\n\r\n";

        Server server = new Server(131, new SocketHandlerFactory(),
                new ClientResponseFactory("."), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 131)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            Thread.sleep(10);

            StringBuilder str = new StringBuilder();
            byte[] bytes = is.readNBytes(is.available());
            for (byte b : bytes)
                str.append((char) b);

            assertEquals("HTTP/1.1 200 OK\n" +
                    "ContentType: text/html\n" +
                    "\n" +
                    "<h1>/website</h1><ul><h3><li><a href=\"././\">.</a></li>" +
                    "<li><a href=\"./../\">..</a></li>" +
                    "<li><a href=\"./no-index/\">no-index</a></li>" +
                    "<li><a href=\"./hello/\">hello</a></li></h3></ul>\n", str.toString());
        }
    }

    @Test
    public void testSendPing() throws InterruptedException, IOException {
        long time = System.currentTimeMillis();
        assertEquals("HTTP/1.1 200 OK\n" +
                "ContentType: text/html\n" +
                "\n" +
                "<h3>" + time + "</h3>", ClientResponse.generatePingResponse(time));
    }

    @Test
    public void testListFilesInHTML() {
        File[] files = {new File("my_file.txt"),
                new File("test.txt"), new File("photo.png")};
        assertEquals("<h1>/my_dir</h1><ul><h3><li><a href=\"././\">.</a></li>" +
                        "<li><a href=\"./../\">..</a></li>" +
                        "<li><a href=\"./my_file.txt/\">my_file.txt</a></li>" +
                        "<li><a href=\"./test.txt/\">test.txt</a></li>" +
                        "<li><a href=\"./photo.png/\">photo.png</a></li></h3></ul>",
                ClientResponse.listFilesInHTML(files, "my_dir"));
    }
}
