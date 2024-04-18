package tests;

import Core.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PingRequestTest {
    @Test
    public void testPingRequest() {
        Request ping = new PingRequest();
        RequestParser rp = new ClientRequestParser(null);
        rp.setRequest("GET /ping HTTP/1.1\nhost: localhost\n\n");

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ResponseNode node = ping.handleRequest(rp, "");
        assertEquals(node.getStatus(), "HTTP/1.1 200 OK");
        assertEquals(node.getHeaders(), new String[]
                {"Content-Type: text/html", "Server: Example Server"});

        String response = "";
        for(byte b : node.getContent())
            response += (char)b;

        assertTrue(response.contains(time.format(formatter)));
    }

    @Test
    public void testPingRequestDelay() {
        Request ping = new PingRequest();
        RequestParser rp = new ClientRequestParser(null);
        rp.setRequest("GET /ping/1 HTTP/1.1\nhost: localhost\n\n");

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ResponseNode node = ping.handleRequest(rp, "");
        assertEquals(node.getStatus(), "HTTP/1.1 200 OK");
        assertEquals(node.getHeaders(), new String[]
                {"Content-Type: text/html", "Server: Example Server"});

        String response = "";
        for(byte b : node.getContent())
            response += (char)b;

        assertTrue(response.contains(time.plusSeconds(1).format(formatter)));
    }
}
