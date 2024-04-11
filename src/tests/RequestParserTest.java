package tests;

import Core.Client;
import Core.RequestParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    @Test
    public void testBuildRequest() throws IOException {
        String input = """
                GET / HTTP/1.1
                Content-Length: 7
                
                content""";
        BufferedReader request = new BufferedReader(new StringReader(input));

        assertEquals(input, RequestParser.buildRequest(request));
    }

    @Test
    public void testRootFile() {
        String path = "/";
        assertEquals(new File("website/index.html"), RequestParser.getFile(path, "website"));
    }

    @Test
    public void testGetRoot() {
        String path = "GET /guess HTTP/1.1";
        assertEquals("/guess", RequestParser.getRoot(path));
    }

    @Test
    public void testGetRequestType() {
        String path = "GET /guess HTTP/1.1";
        assertEquals("GET", RequestParser.getRequestType(path));
    }

    @Test
    public void testGetFile() {
        String path = "/no-index/test.txt";
        assertEquals(new File("website/no-index/test.txt"), RequestParser.getFile(path, "website"));
    }

    @Test
    public void testDirectoryWithIndex() {
        String path = "/hello";
        assertEquals(new File("website/hello/index.html"), RequestParser.getFile(path, "website"));
    }

    @Test
    public void testDirectoryWithoutIndex() {
        String path = "/no-index";
        assertEquals(new File("website/no-index"), RequestParser.getFile(path, "website"));
    }

    @Test
    public void testGetParameters()
    {
        String request = "GET /guess HTTP/1.1\n" +
                "Host: localhost\n\n" +
                "number=5&my-name=bob";
        String[] answer = {"number=5", "my-name=bob"};
        assertEquals(answer, RequestParser.getParameters(request));

    }

}
