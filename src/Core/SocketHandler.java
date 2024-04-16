package Core;

import java.net.Socket;

public class SocketHandler implements Handler {

    private final Socket clientSocket;
    private final ResponseFactory responseFactory;
    private final RequestParser rp;

    public SocketHandler(ResponseFactory responseFactory, RequestParser rp) {
        this.responseFactory = responseFactory;
        this.rp = rp;
        this.clientSocket = rp.getSocket();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ResponseFactory getResponseFactory() {
        return responseFactory;
    }

    public void handleClient() throws Exception {
        rp.buildBody();
        String requestType = rp.getRequestType();
        Response response = responseFactory.newResponse(
                clientSocket, rp, responseFactory.getRoot());

        if (requestType.equals("GET"))
            response.sendGETResponse();
        else
            response.sendPOSTResponse();

    }


    @Override
    public void run() {
        try {
            handleClient();
            clientSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
