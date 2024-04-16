package mock;

import Core.Handler;

import java.net.Socket;

public class MockHandler implements Handler {

    private final Socket clientSocket;

    public MockHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleClient() throws Exception {
        clientSocket.getOutputStream().write("Connected!\n".getBytes());
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
