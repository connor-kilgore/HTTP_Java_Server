package tests;

import Core.*;
import mock.MockResponseFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    @Test
    public void testConstructor() throws IOException {
        Socket client = new Socket();
        ClientRequestParser rp = new ClientRequestParser(client);
        assertEquals(client, rp.getSocket());
        assertEquals("", rp.getRequest());
    }

    @Test
    public void testBuildBody() throws Exception {
        String input = "GET / HTTP/1.1\r\nContent-Length: 7\r\nHost: localhost\r\n\r\ncontent";

        String[] args = {"-p", "127", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new MockResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 127)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < input.length() - 4; i++) {
                str.append((char) is.read());
            }

            assertEquals("GET / HTTP/1.1\n" +
                         "Content-Length: 7\n" +
                         "Host: localhost\n" +
                         "\n" +
                         "content", str.toString());
        }
    }

    @Test
    public void testBuildBodyNoContent() throws Exception {
        String input = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";

        String[] args = {"-p", "128"};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new MockResponseFactory(ap.getRoot()), new ClientRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket socket = new Socket("localhost", 128)) {
            socket.getOutputStream().write(input.getBytes());
            InputStream is = socket.getInputStream();

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < input.length() - 3; i++) {
                str.append((char) is.read());
            }

            assertEquals("GET / HTTP/1.1\n" +
                         "Host: localhost\n\n", str.toString());
        }
    }

    @Test
    public void testSetRequest() throws IOException {
        String request = "GET /guess HTTP/1.1";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals(request, rp.getRequest());
    }

    @Test
    public void testGetRequestType() throws IOException {
        String request = "GET /guess HTTP/1.1";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals("GET", rp.getRequestType());
    }

    @Test
    public void testGetPath() throws IOException {
        String request = "GET /guess HTTP/1.1";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals("/guess", rp.getPath());
    }

    @Test
    public void testGetFile() throws IOException {
        String request = "GET /no-index/test.txt HTTP/1.1\n";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals(new File("website/no-index/test.txt"), rp.getFile("website"));
    }

    @Test
    public void testDirectoryWithIndex() throws IOException {
        String request = "GET /hello HTTP/1.1\n";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals(new File("website/hello/index.html"), rp.getFile("website"));
        request = "GET / HTTP/1.1\n";
        rp.setRequest(request);
        assertEquals(new File("./index.html"), rp.getFile("."));
    }

    @Test
    public void testDirectoryWithoutIndex() throws IOException {
        String request = "GET /no-index HTTP/1.1\n";
        ClientRequestParser rp = new ClientRequestParser(null);
        rp.setRequest(request);
        assertEquals(new File("website/no-index"), rp.getFile("website"));
    }


}
