package tests;

import Core.ArgumentParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentParserTest {

    @Test
    public void testPort90() {
        String[] args = {"-p", "90"};
        assertEquals(90, ArgumentParser.getPort(args));
    }

    @Test
    public void testNoPort() {
        String[] args = {};
        assertEquals(80, ArgumentParser.getPort(args));
    }

    @Test
    public void testInvalidPort() {
        String[] args = {"-p", "not-a-real-port"};
        assertEquals(80, ArgumentParser.getPort(args));
    }

    @Test
    public void testInvalidPortOrder() {
        String[] args = {"40", "-p"};
        assertEquals(80, ArgumentParser.getPort(args));
    }

    @Test
    public void testDifferentArgsForPort() {
        String[] args = {"-r", "my-path/path", "-p", "120", "random flag"};
        assertEquals(120, ArgumentParser.getPort(args));
    }

    @Test
    public void testNoPath() {
        String[] args = {};
        assertEquals("website", ArgumentParser.getPath(args));
    }

    @Test
    public void testPath() {
        String[] args = {"-r", "fileOne/fileTwo"};
        assertEquals("fileOne/fileTwo", ArgumentParser.getPath(args));
    }

    @Test
    public void testInvalidPathOrder() {
        String[] args = {"fileOne/fileTwo", "-r"};
        assertEquals("website", ArgumentParser.getPath(args));
    }

    @Test
    public void testDifferentArgsForPath() {
        String[] args = {"-p", "120", "-r", "my-path/path", "random flag"};
        assertEquals("my-path/path", ArgumentParser.getPath(args));
    }

}
