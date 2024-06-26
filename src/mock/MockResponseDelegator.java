package mock;

import Core.RequestParser;
import Core.ResponseDelegator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MockResponseDelegator implements ResponseDelegator {
    public final Socket clientSocket;
    public final RequestParser rp;
    public final String root;
    OutputStream clientOutput;
    public MockResponseDelegator(Socket clientSocket, RequestParser rp, String root)
            throws IOException {
        this.clientSocket = clientSocket;
        this.rp = rp;
        this.root = root;
        clientOutput = this.clientSocket.getOutputStream();
    }
    public void sendGETResponse() throws IOException {
        clientOutput.write(rp.getRequest().getBytes());
        clientOutput.write(rp.getContent());
    }

    public void sendPOSTResponse() throws IOException {
        clientOutput.write(rp.getRequest().getBytes());
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
