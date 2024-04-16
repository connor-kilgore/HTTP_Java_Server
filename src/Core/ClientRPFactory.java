package Core;

import java.io.IOException;
import java.net.Socket;

public class ClientRPFactory implements RPFactory{
    public RequestParser newRP(Socket clientSocket) throws IOException {
        return new ClientRequestParser(clientSocket);
    }
}
