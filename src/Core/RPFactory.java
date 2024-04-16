package Core;

import java.io.IOException;
import java.net.Socket;

public interface RPFactory {
    public RequestParser newRP (Socket clientSocket) throws IOException;
}
