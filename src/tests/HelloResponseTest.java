package tests;

import Core.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class HelloResponseTest {
    @Test
    public void testSendHello() throws IOException, InterruptedException {
        String input = "GET /hello HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "135", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ServerResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 135)) {
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
                         "</title></head><body><br><br><h1>Hello World!</h1>" +
                         "</body></html>", str.toString());
        }
    }
}
