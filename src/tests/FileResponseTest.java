package tests;

import Core.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileResponseTest {

    @Test
    public void testSendNotFound(){
        ResponseNode node = new FileResponse().generateNotFound();
        assertEquals("HTTP/1.1 404 Not Found", node.getStatus());
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h1>Error 404 (File Not Found)</h1>"));
    }

    @Test
    public void testSendHomePage() throws IOException {
        FileResponse response = new FileResponse();
        File contentFile = response.getFile(".", "index.html");
        ResponseNode node = response.generateFile(contentFile);
        assertEquals("HTTP/1.1 200 OK", node.getStatus());
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("Welcome to my HTTP Server!"));
    }

    @Test
    public void testSendFile() throws IOException {
        FileResponse response = new FileResponse();
        File contentFile = response.getFile(".",
                "website/no-index/test.txt");
        ResponseNode node = response.generateFile(contentFile);
        assertEquals("HTTP/1.1 200 OK", node.getStatus());
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("I am a test file."));
    }

    @Test
    public void testSendDirectory() throws IOException {
        FileResponse response = new FileResponse();
        File contentFile = response.getFile(".",
                "website");
        ResponseNode node = response.generateDirectory(contentFile, ".");
        assertEquals("HTTP/1.1 200 OK", node.getStatus());
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<h1>/website</h1>"));
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<li><a href=\"/listing/website/no-index\">no-index</a></li>"));
    }
}
