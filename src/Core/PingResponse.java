package Core;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PingResponse implements Response {
    public void handleResponse(RequestParser rp, String root) throws IOException {
        int delay = 0;
        if (!rp.getPath().replaceFirst("/ping", "").isEmpty())
            delay = Integer.parseInt(rp.getPath().split("/")[2]);

        try {
            generatePingStart().sendResponse(rp.getSocket());
            Thread.sleep(delay * 1000);
            generatePingEnd().sendResponse(rp.getSocket());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseNode generatePingEnd(){
        LocalDateTime endTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String content = "<li>end time: " + endTime.format(formatter) + "</li>" +
                  "<li>end time in ms: " + endTime + "</li>" +
                  "</body></html>";

        return new ResponseNode("", new String[]{}, content.getBytes());
    }
    public ResponseNode generatePingStart() {
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        String content = "<!DOCTYPE html>" +
                         "<html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h2>Ping</h2>" +
                         "<li>start time: " + startTime.format(formatter) + "</li>" +
                         "<li>start time in ms: " + startTime + "</li>";


        return new ResponseNode(status, headers, content.getBytes());
    }

    public void setContentArray(byte[] content) {
    }
}
