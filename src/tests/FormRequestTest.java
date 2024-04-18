package tests;

import Core.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class FormRequestTest {

    @Test
    public void testForm() throws IOException, InterruptedException {
        String input = "GET /form?foo=1&bar=2 HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "138"};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new ClientResponseFactory(ap.getRoot()), new ClientRPFactory());
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
                         "<!DOCTYPE html><html><head><title> Java HTTP Core.Server </title>" +
                         "</head><body><br><br><h2>GET Form</h2>" +
                         "<li>foo: 1</li>" +
                         "<li>bar: 2</li></body></html>",
                    str.toString());
        }
    }
}
