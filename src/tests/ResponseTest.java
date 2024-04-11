package tests;

import Core.Response;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    @Test
    public void testGenerateResponse() throws IOException {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html", "Host: localhost"};

        assertEquals("""
                        HTTP/1.1 200 OK
                        ContentType: text/html
                        Host: localhost
                        
                        """,
                Response.generateResponse(status, headers));
    }

    @Test
    public void testListFilesInHTML() {
        File[] files = {new File("my_file.txt"),
                new File("test.txt"), new File("photo.png")};
        String fileName = "my_file.txt";
        assertEquals("<h1>/my_dir</h1><ul><h3><li><a href=\"././\">.</a></li>" +
                        "<li><a href=\"./../\">..</a></li>" +
                        "<li><a href=\"./my_file.txt/\">my_file.txt</a></li>" +
                        "<li><a href=\"./test.txt/\">test.txt</a></li>" +
                        "<li><a href=\"./photo.png/\">photo.png</a></li></h3></ul>",
                Response.listFilesInHTML(files, "my_dir"));
    }

}
