package tests;

import Core.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PingResponseTest {
    @Test
    public void testPingRequest() throws InterruptedException {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ResponseNode node = new PingResponse().generatePingStart();
        assertEquals(node.getStatus(), "HTTP/1.1 200 OK");
        assertEquals(node.getHeaders(), new String[]
                {"Content-Type: text/html", "Server: Example Server"});

        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains(time.format(formatter)));
    }

    @Test
    public void testPingRequestDelay() throws InterruptedException {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ResponseNode startNode = new PingResponse().generatePingStart();
        Thread.sleep(1000);
        ResponseNode endNode = new PingResponse().generatePingEnd();

        assertEquals(startNode.getStatus(), "HTTP/1.1 200 OK");
        assertTrue(ResponseNode.convertBytesToString(startNode.getContent())
                .contains(time.format(formatter)));
        assertTrue(ResponseNode.convertBytesToString(endNode.getContent())
                .contains(time.plusSeconds(1).format(formatter)));
    }
}
