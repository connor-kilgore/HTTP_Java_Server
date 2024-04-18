package tests;

import Core.FileParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileParserTest {

    @Test
    public void testGetFileSize() {
        String fileStr = "HEADERS\r\n\r\nCONTENT\r\n--FOOTER";
        assertEquals(7, FileParser.getFileSize(fileStr.getBytes()));

        fileStr = "HEADERS\r\n\r\nLARGER CONTENT\r\n--FOOTER";
        assertEquals(14, FileParser.getFileSize(fileStr.getBytes()));
    }

    @Test
    public void testExtractHeader() {
        String fileStr = "Content-Type: Image\r\n" +
                         "File Name: test.jpg\r\n\r\n" +
                         "CONTENT\r\n--FOOTER";
        assertEquals("Content-Type: Image", FileParser.extractHeader(
                fileStr.getBytes(), "Content-Type"));
        assertEquals("File Name: test.jpg", FileParser.extractHeader(
                fileStr.getBytes(), "File Name"));
        assertEquals("", FileParser.extractHeader(
                fileStr.getBytes(), "Not a real header"));
    }

    @Test
    public void testExtractHeaderValue(){
        String fileStr = "Content-Type: Image\r\n" +
                         "File Name: test.jpg\r\n\r\n" +
                         "CONTENT\r\n--FOOTER";
        assertEquals("Image", FileParser.extractHeaderValue(
                fileStr.getBytes(), "Content-Type"));
        assertEquals("test.jpg", FileParser.extractHeaderValue(
                fileStr.getBytes(), "File Name"));
        assertEquals("", FileParser.extractHeaderValue(
                fileStr.getBytes(), "Not a real header"));
    }

    @Test
    public void testExtractFileName(){
        String fileStr = "Content-Disposition: test=test; " +
                         "filename=name.txt; foo=bar\r\n" +
                         "File Name: test.jpg\r\n\r\n" +
                         "CONTENT\r\n--FOOTER";

        assertEquals("name.txt", FileParser.extractFileName(
                fileStr.getBytes()));

        fileStr = "Content-Disposition: test=test; " +
                         "filename=my-photo.jpg; foo=bar\r\n" +
                         "File Name: test.jpg\r\n\r\n" +
                         "CONTENT\r\n--FOOTER";

        assertEquals("my-photo.jpg", FileParser.extractFileName(
                fileStr.getBytes()));
    }
}
