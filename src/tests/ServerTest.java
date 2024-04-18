package tests;

import Core.ArgumentParser;
import Core.Server;
import Core.SocketHandlerFactory;
import mock.MockHandlerFactory;
import mock.MockRPFactory;
import mock.MockResponseFactory;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void testConstructor() {
        SocketHandlerFactory hf = new SocketHandlerFactory();
        MockResponseFactory rf = new MockResponseFactory("foo");
        String[] args = {"-p", "90", "-r", ".", "-x"};
        ArgumentParser ap = new ArgumentParser(args);
        MockRPFactory rpf = new MockRPFactory();
        Server server = new Server(ap, hf, rf, rpf);
        assertEquals(ap, server.getArgumentParser());
        assertEquals(hf, server.getHandlerFactory());
        assertEquals(rf, server.getResponseFactory());
        assertEquals(rpf, server.getRPFactory());
        assertFalse(server.getThreadPool().isTerminated());
    }

    @Test
    public void startAndStop() throws Exception{
        String[] args = {"-p", "123", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new SocketHandlerFactory(),
                new MockResponseFactory(ap.getRoot()), new MockRPFactory());
        server.start();
        Thread.sleep(10);
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    @Test
    public void connection() throws Exception{
        String[] args = {"-p", "124", "-r", "."};
        ArgumentParser ap = new ArgumentParser(args);
        Server server = new Server(ap, new MockHandlerFactory(),
                new MockResponseFactory(ap.getRoot()), new MockRPFactory());
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
