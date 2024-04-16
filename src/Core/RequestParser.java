package Core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public interface RequestParser {
    public String getRequestType();

    public void buildBody() throws IOException;

    public Socket getSocket();

    public String getPath();

    public String[] getParameters();
    public String getRequest();
    public File getFile(String root);
}
