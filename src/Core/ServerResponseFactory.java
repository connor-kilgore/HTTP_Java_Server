package Core;

import java.net.Socket;

public class ServerResponseFactory implements ResponseFactory {

    private String root;

    public ServerResponseFactory(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    public ResponseDelegator newResponse(Socket clientSocket, RequestParser rp, String root) {
        return new ServerResponseDelegator(clientSocket, rp, root);
    }
}
