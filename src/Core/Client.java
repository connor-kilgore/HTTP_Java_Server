package Core;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    Socket clientSocket;
    String root;

    public Client(Socket clientSocket, String root) {
        this.clientSocket = clientSocket;
        this.root = root;
    }

    public void handleClient() throws IOException, InterruptedException {
        BufferedReader clientReader =
                new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));

        String request = RequestParser.buildRequest(clientReader);
        String requestType = RequestParser.getRequestType(request);

        if (requestType.equals("GET"))
            Response.sendGETResponse(clientSocket, request, root);
        else
        {
            Response.sendPOSTResponse(clientSocket, request, root);
        }
        clientSocket.close();
    }


    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
