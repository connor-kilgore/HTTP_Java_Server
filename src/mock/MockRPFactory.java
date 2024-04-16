package mock;

import Core.RPFactory;
import Core.RequestParser;

import java.io.IOException;
import java.net.Socket;

public class MockRPFactory implements RPFactory {
    public RequestParser newRP(Socket clientSocket) throws IOException {
        return new MockRequestParser(clientSocket);
    }
}
