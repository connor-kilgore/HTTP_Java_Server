package mock;

import Core.RequestParser;
import Core.Response;
import Core.ResponseFactory;

import java.io.IOException;
import java.net.Socket;

public class MockResponseFactory implements ResponseFactory {

    private final String root;

    public MockResponseFactory(String root) {
        this.root = root;
    }

    public Response newResponse(Socket clientSocket, RequestParser rp,
                                String root) throws IOException {
        return new MockResponse(clientSocket, rp, root);
    }

    public String getRoot() {
        return root;
    }
}
