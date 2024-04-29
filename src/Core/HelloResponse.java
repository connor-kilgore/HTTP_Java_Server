package Core;

import java.io.IOException;

public class HelloResponse implements Response {
    public void handleResponse(RequestParser rp, String root) throws IOException {
        generateHello().sendResponse(rp.getSocket());
    }

    public void setContentArray(byte[] content) {
    }

    public ResponseNode generateHello() {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        String content = "<!DOCTYPE html>" +
                         "<html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h1>Hello World!</h1></body></html>";

        return new ResponseNode(status, headers, content.getBytes());
    }
}
