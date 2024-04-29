package mock;

import Core.RequestParser;
import Core.ResponseDelegator;
import Core.ResponseFactory;

import java.io.IOException;
import java.net.Socket;

public class MockResponseFactory implements ResponseFactory {

    private final String root;

    public MockResponseFactory(String root) {
        this.root = root;
    }

    public ResponseDelegator newResponse(Socket clientSocket, RequestParser rp,
                                         String root) throws IOException {
        return new MockResponseDelegator(clientSocket, rp, root);
    }

    public String getRoot() {
        return root;
    }
}
