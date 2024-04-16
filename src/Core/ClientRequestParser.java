package Core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientRequestParser implements RequestParser {

    Socket clientSocket;
    public String request;

    public ClientRequestParser(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.request = "";
    }

    public int buildHead(StringBuilder str) throws IOException {
        StringBuilder temp = new StringBuilder();
        int contentLen = 0;
        int b;
        while (!temp.toString().endsWith("\r\n\r\n")) {
            b = clientSocket.getInputStream().read();
            temp.append((char) b);
        }

        for (String h : temp.toString().split("\r\n")) {
            if (h.startsWith("Content-Length: "))
                contentLen = Integer.parseInt(h.substring("Content-Length: ".length()));
            str.append(h).append("\n");
        }
        str.append("\n");

        return contentLen;
    }

    public void buildContent(StringBuilder str, int contentLen) throws IOException {
        InputStream is = clientSocket.getInputStream();
        while (contentLen > 0) {
            str.append((char) is.read());
            contentLen--;
        }
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest(){
        return request;
    }

    public void buildBody() throws IOException {
        StringBuilder str = new StringBuilder();
        int contentLen = buildHead(str);
        buildContent(str, contentLen);
        request = str.toString(); // TODO
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public String getPath() {
        return request.split(" ")[1].split("\\?")[0];
    }

    public String getRequestType() {
        return request.split(" ")[0];
    }

    public String[] getParameters() {
        return request.split("\n\n")[1].split("\n")[0].split("&");
    }

    public File getFile(String root) {
        File newFile = new File(root + "/" + getPath());
        File indexFile = new File(root + "/" + getPath() + "/index.html");

        if (newFile.isDirectory() && indexFile.exists())
            return indexFile;
        else
            return newFile;
    }

}
