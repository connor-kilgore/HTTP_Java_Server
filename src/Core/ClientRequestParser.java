package Core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientRequestParser implements RequestParser {

    Socket clientSocket;
    public String request;
    byte[] content;

    public ClientRequestParser(Socket clientSocket){
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

    public void buildContent(int contentLen) throws IOException {
        content = new byte[contentLen];
        InputStream is = clientSocket.getInputStream();
        int i = 0;
        while (contentLen > 0) {
            content[i] = (byte)is.read();
            i++;
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
        buildContent(contentLen);
        request = str.toString(); // TODO
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public String getPath() {
        return request.split(" ")[1].split("\\?")[0];
    }
    public String[] getPathParams() {
        String requestPath = request.split(" ")[1];
        int i;
        if ((i = requestPath.indexOf('?')) != -1 && i != requestPath.length() - 1) {
            return requestPath.split("\\?")[1].split("&");
        }
        else
            return new String[] {};
    }

    public String getRequestType() {
        return request.split(" ")[0];
    }

    public byte[] getContent() {
        return content;
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
