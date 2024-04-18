package Core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientResponse implements Response {

    public final Socket clientSocket;
    public final RequestParser rp;
    public final String root;
    public final Map<String, Request> getHandlers = new HashMap<>();
    public final Map<String, Request> postHandlers = new HashMap<>();
    public final Request fileRequest = new FileRequest();

    public ClientResponse(Socket clientSocket, RequestParser rp, String root) {
        this.clientSocket = clientSocket;
        this.rp = rp;
        this.root = root;
        initializeGetHandlers();
        initializePostHandlers();
    }

    public void sendGETResponse()
            throws IOException, InterruptedException {
        Request request = getHandlers.get(rp.getPath());
        ResponseNode response;

        if (request == null) {
            response = fileRequest.handleRequest(rp, root);
        } else {
            response = request.handleRequest(rp, root);
        }

        sendResponse(response, clientSocket);
    }

    public void sendPOSTResponse()
            throws IOException, InterruptedException {
        Request request = postHandlers.get(rp.getPath());
        if (request == null)
            sendGETResponse();
        else {
            request.setContentArray(rp.getContent());
            sendResponse(request.handleRequest(rp, root), clientSocket);
        }
    }

    public static byte[] generateResponse(ResponseNode node) {
        StringBuilder str = new StringBuilder();
        str.append(node.getStatus()).append("\n");
        for (String header : node.getHeaders())
            str.append(header).append("\n");
        str.append("\n");

        return combineHeadWithContent(str.toString().getBytes(), node.getContent());
    }

    public void sendResponse(ResponseNode node, Socket clientSocket) throws IOException {
        OutputStream os = clientSocket.getOutputStream();

        os.write(generateResponse(node));
    }

    public static byte[] combineHeadWithContent(byte[] head, byte[] content) {
        byte[] combined = new byte[head.length + content.length];
        int n = 0;
        for (; n < head.length; n++)
            combined[n] = head[n];
        for (int m = 0; m < content.length; n++, m++)
            combined[n] = content[m];

        return combined;
    }

    public static String convertBytesToString(byte[] response) {
        StringBuilder str = new StringBuilder();
        for (byte b : response)
            str.append((char) b);
        return str.toString();
    }

    public void initializeGetHandlers() {
        getHandlers.put("/guess", new GuessingGameRequest());
        getHandlers.put("/hello", new HelloRequest());
        getHandlers.put("/ping", new PingRequest());
        getHandlers.put("/form", new FormRequest());
    }

    public void initializePostHandlers() {
        postHandlers.put("/guess", new GuessingGameRequest());
        postHandlers.put("/form", new FormRequest());
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
