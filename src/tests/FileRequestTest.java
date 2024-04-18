package tests;

import Core.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class FileRequestTest {

    @Test
    public void testSendNotFound() throws IOException, InterruptedException {
        String input = "GET /not-real HTTP/1.1\r\nHost: localhost\r\n\r\n\r\n";

        String[] args = {"-p", "129", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
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

            assertEquals("HTTP/1.1 404 Not Found\n" +
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server" +
                         " </title></head><body><br><br><h1>Error 404 (File Not Found)" +
                         "</h1></body></html>", str.toString());
        }
    }

    @Test
    public void testSendHomePage() throws IOException, InterruptedException {
        String input = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "130", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
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
                         "Content-Type: text/html\n" +
                         "Server: Example Server\n" +
                         "\n" +
                         Files.readString(content.toPath()), str.toString());
        }
    }

    @Test
    public void testSendFile() throws IOException, InterruptedException {
        String input = "GET /website/no-index/test.txt HTTP/1.1\r\n" +
                       "Host: localhost\r\n\r\n";

        String[] args = {"-p", "131", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
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

            assertEquals("""
                    HTTP/1.1 200 OK
                    Content-Type: text/plain
                    Server: Example Server

                    I am a test file.""", str.toString());
        }
    }

    @Test
    public void testSendDirectory() throws IOException, InterruptedException {
        String input = "GET /website HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "137"};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 137)) {
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
                         "<h1>/website</h1><ul>" +
                         "<li><a href=\"/listing/website/no-index\">no-index</a></li>" +
                         "<li><a href=\"/listing/website/hello\">hello</a></li></h3></ul>",
                    str.toString());
        }
    }
}
