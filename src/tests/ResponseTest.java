package tests;

import Core.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        Socket client = new Socket();
        RequestParser rp = new ClientRequestParser(client);
        String root = ".";
        ResponseDelegator response = new ServerResponseDelegator(client, rp, root);

        assertEquals(client, response.getClientSocket());
        assertEquals(rp, response.getRequestParser());
        assertEquals(root, response.getRoot());
    }

    @Test
    public void testGenerateResponse() {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html", "Host: localhost"};
        ResponseNode node = new ResponseNode(status, headers, "".getBytes());

        assertEquals("""
                        HTTP/1.1 200 OK
                        ContentType: text/html
                        Host: localhost
                                                
                        """,
                ResponseNode.convertBytesToString
                        (node.generateResponse()));

        status = "HTTP/1.1 404 Not Found";
        node = new ResponseNode(status, headers, "".getBytes());

        assertEquals("""
                        HTTP/1.1 404 Not Found
                        ContentType: text/html
                        Host: localhost
                                                
                        """,
                ResponseNode.convertBytesToString(
                        node.generateResponse()));
    }

    @Test
    public void testCombineHeadWithContent() {
        byte[] head = {1, 2, 3};
        byte[] content = {4, 5, 6};
        ResponseNode node = new ResponseNode(null, null, content);
        byte[] answer = {1, 2, 3, 4, 5, 6};
        assertEquals(Arrays.toString(answer),
                Arrays.toString(node.combineHeadWithContent
                        (head)));
    }

    @Test
    public void testPOSTFallback() throws IOException, InterruptedException {
        String input = "POST /hello HTTP/1.1\r\n" +
                       "Host: localhost\r\n" +
                       "Content-Length: 43\r\n" +
                       "\r\n" +
                       "number=22&number-to-guess=50&guesses-left=4";

        String[] args = {"-p", "134", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ServerResponseFactory(ap.getRoot()), new ClientRPFactory());
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
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h1>Hello World!</h1></body></html>", str.toString());
        }
    }

    @Test
    public void testRootListing() throws IOException, InterruptedException {
        String input = "GET /listing HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "138", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ServerResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 138)) {
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
                         "<h1>/.</h1><ul>" +
                         "<li><a href=\"/listing/saved-images\">saved-images</a></li>" +
                         "<li><a href=\"/index\">index.html</a></li>" +
                         "<li><a href=\"/\">.DS_Store</a></li>" +
                         "<li><a href=\"/listing/out\">out</a></li>" +
                         "<li><a href=\"/listing/website\">website</a></li>" +
                         "<li><a href=\"/\">.gitignore</a></li>" +
                         "<li><a href=\"/listing/\">.git</a></li>" +
                         "<li><a href=\"/HTTP_Server\">HTTP_Server.iml</a></li>" +
                         "<li><a href=\"/listing/\">.idea</a></li>" +
                         "<li><a href=\"/listing/src\">src</a></li></h3></ul>", str.toString());
        }
    }
}
