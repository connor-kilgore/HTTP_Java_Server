package tests;

import Core.*;
import mock.MockRPFactory;
import mock.MockResponseFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class SocketHandlerTest {

    @Test
    public void testConstructor() throws IOException {
        Socket socket = new Socket("localhost", 124);
        ResponseFactory responseFactory = new ClientResponseFactory(".");
        RequestParser rp = new ClientRequestParser(socket);
        SocketHandler client = new SocketHandler(responseFactory, rp);
        assertEquals(socket, client.getClientSocket());
        assertEquals(responseFactory, client.getResponseFactory());
    }

    @Test
    public void testGetRequest() throws Exception {

        Server server = new Server(125, new SocketHandlerFactory(),
                new MockResponseFactory("foo"), new MockRPFactory());
        server.start();

        String request = "GET / HTTP/1.1\n";

        Thread.sleep(10);
        try (Socket socket = new Socket("localhost", 125)) {
            socket.getOutputStream().write(request.getBytes());

            String str = "";
            int b;
            while((b = socket.getInputStream().read()) != '\n')
                str += ((char) b);

            assertEquals("GET / HTTP/1.1", str);
        }
    }

    @Test
    public void testPostRequest() throws Exception {

        Server server = new Server(126, new SocketHandlerFactory(),
                new MockResponseFactory("foo"), new MockRPFactory());
        server.start();

        String request = "POST / HTTP/1.1\n";

        Thread.sleep(10);
        try (Socket socket = new Socket("localhost", 126)) {
            socket.getOutputStream().write(request.getBytes());
            Thread.sleep(10);
            String str = "";
            int b;
            while((b = socket.getInputStream().read()) != '\n')
                str += ((char) b);

            assertEquals("POST / HTTP/1.1", str);
        }
    }

}
