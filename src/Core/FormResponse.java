package Core;

import java.io.IOException;

public class FormResponse implements Response {
    private byte[] contentArr;
    public void handleResponse(RequestParser rp, String root) throws IOException {
        if (contentArr == null)
            generateGETForm(rp).sendResponse(rp.getSocket());
        else
            generatePOSTForm(rp).sendResponse(rp.getSocket());
    }

    public void setContentArray(byte[] content) {
        this.contentArr = content;
    }

    public ResponseNode generatePOSTForm(RequestParser rp){
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html"};

        String content = "<!DOCTYPE html>" +
                         "<html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h2>POST Form</h2>" +
                         "<li>file name: " + FileParser.extractFileName(rp.getContent()) +  "</li>" +
                         "<li>content type: " + FileParser.extractHeaderValue(rp.getContent(), "Content-Type")
                         + "</li>" +
                         "<li>file size: " + FileParser.getFileSize(rp.getContent()) + "</li>" +
                         "</body></html>";

        return new ResponseNode(status, headers, content.getBytes());
    }

    public ResponseNode generateGETForm(RequestParser rp) {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        String content = "<!DOCTYPE html>" +
                         "<html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h2>GET Form</h2>";
        for (String param : rp.getPathParams())
        {
            String[] paramSplit = param.split("=");
            content += "<li>" + paramSplit[0] + ": " + paramSplit[1] + "</li>";
        }

        content += "</body></html>";

        return new ResponseNode(status, headers, content.getBytes());
    }


}
