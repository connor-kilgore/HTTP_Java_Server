package mock;

import Core.RequestParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MockRequestParser implements RequestParser {

    Socket clientSocket;
    public String request;
    public MockRequestParser (Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.request = "";
    }

    public String getRequestType() {
        return request.split(" ")[0];
    }

    public void buildBody() throws IOException {
        String str = "";
        int b;
        InputStream is = clientSocket.getInputStream();
        while((b = is.read()) != '\n')
            str += (char) b;

        str += '\n';

        request = str;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public String getPath() {
        return null;
    }

    public String[] getParameters() {
        return new String[0];
    }

    public String getRequest() {
        return request;
    }

    public File getFile(String root) {
        return null;
    }
}
