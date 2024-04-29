package Core;

import java.io.IOException;
import java.net.Socket;

public interface ResponseDelegator {

    public void sendGETResponse() throws IOException, InterruptedException;
    public void sendPOSTResponse() throws IOException, InterruptedException;
    public Socket getClientSocket();
    public RequestParser getRequestParser();
    public String getRoot();
}
