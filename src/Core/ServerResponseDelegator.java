package Core;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerResponseDelegator implements ResponseDelegator {

    public final Socket clientSocket;
    public final RequestParser rp;
    public final String root;
    public final Map<String, Response> getHandlers = new HashMap<>();
    public final Map<String, Response> postHandlers = new HashMap<>();
    public final Response fileResponse = new FileResponse();

    public ServerResponseDelegator(Socket clientSocket, RequestParser rp, String root) {
        this.clientSocket = clientSocket;
        this.rp = rp;
        this.root = root;
        initializeGetHandlers();
        initializePostHandlers();
    }

    public void sendGETResponse()
            throws IOException, InterruptedException {
        Response response = getHandlers.get(rp.getPath());
        if (response == null) {
            fileResponse.handleResponse(rp, root);
        } else {
           response.handleResponse(rp, root);
        }
    }

    public void sendPOSTResponse()
            throws IOException, InterruptedException {
        Response response = postHandlers.get(rp.getPath());
        if (response == null)
            sendGETResponse();
        else {
            response.setContentArray(rp.getContent());
            response.handleResponse(rp, root);
        }
    }

    public void initializeGetHandlers() {
        getHandlers.put("/guess", new GuessingGameResponse());
        getHandlers.put("/hello", new HelloResponse());
        getHandlers.put("/ping", new PingResponse());
        getHandlers.put("/form", new FormResponse());
    }

    public void initializePostHandlers() {
        postHandlers.put("/guess", new GuessingGameResponse());
        postHandlers.put("/form", new FormResponse());
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public RequestParser getRequestParser() {
        return rp;
    }

    public String getRoot() {
        return root;
    }
}
