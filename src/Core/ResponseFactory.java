package Core;

import java.io.IOException;
import java.net.Socket;

public interface ResponseFactory {
    public ResponseDelegator newResponse(Socket clientSocket, RequestParser rp, String root)
            throws IOException;
    public String getRoot();
}
