package tests;

import Core.Server;
import Core.SocketHandlerFactory;
import mock.MockHandlerFactory;
import mock.MockRPFactory;
import mock.MockRequestParser;
import mock.MockResponseFactory;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void testConstructor() {
        SocketHandlerFactory hf = new SocketHandlerFactory();
        MockResponseFactory rf = new MockResponseFactory("foo");
        MockRPFactory rpf = new MockRPFactory();
        Server server = new Server(123, hf, rf, rpf);
        assertEquals(123, server.getPort());
        assertEquals(hf, server.getHandlerFactory());
        assertEquals(rf, server.getResponseFactory());
        assertEquals(rpf, server.getRPFactory());
        assertFalse(server.getThreadPool().isTerminated());
    }

    @Test
    public void startAndStop() throws Exception{
        Server server = new Server(123, new SocketHandlerFactory(),
                new MockResponseFactory("foo"), new MockRPFactory());
        server.start();
        Thread.sleep(10);
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    @Test
    public void connection() throws Exception{
        Server server = new Server(124, new MockHandlerFactory(),
                new MockResponseFactory("foo"), new MockRPFactory());
        server.start();
        Thread.sleep(10);

        try (Socket client = new Socket("localhost", 124)) {
            Thread.sleep(10);
            String str = "";
            int b;
            while((b = client.getInputStream().read()) != '\n')
                str += ((char) b);

            assertEquals("Connected!", str);
        }
        server.stop();
    }
}
