package Core;

import java.net.Socket;

public class ClientResponseFactory implements ResponseFactory {

    private String root;

    public ClientResponseFactory(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    public Response newResponse(Socket clientSocket, RequestParser rp, String root) {
        return new ClientResponse(clientSocket, rp, root);
    }
}
