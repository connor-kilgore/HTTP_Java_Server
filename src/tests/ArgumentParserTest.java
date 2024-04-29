package tests;

import Core.ArgumentParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentParserTest {
    @Test
    public void testConstructor() {
        String[] args = {"-p", "90", "-r", "fileOne/fileTwo", "-x"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(90, ap.getPort());
        assertEquals("fileOne/fileTwo", ap.getRoot());
        assertTrue(ap.getConfigFlag());
    }

    @Test
    public void testNullArgs() {
        String[] args = null;
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(80, ap.getPort());
        assertEquals(".", ap.getRoot());
    }

    @Test
    public void testPort90() {
        String[] args = {"-p", "90"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(90, ap.findPort(args));
    }

    @Test
    public void testNoPort() {
        String[] args = {};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(80, ap.findPort(args));
    }

    @Test
    public void testInvalidPort() {
        String[] args = {"-p", "not-a-real-port"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(80, ap.findPort(args));
    }

    @Test
    public void testInvalidPortOrder() {
        String[] args = {"40", "-p"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(80, ap.findPort(args));
    }

    @Test
    public void testDifferentArgsForPort() {
        String[] args = {"-r", "my-path/path", "-p", "120", "random flag"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(120, ap.findPort(args));
    }

    @Test
    public void testNoPath() {
        String[] args = {};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(".", ap.findRoot(args));
    }

    @Test
    public void testPath() {
        String[] args = {"-r", "fileOne/fileTwo"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals("fileOne/fileTwo", ap.findRoot(args));
    }

    @Test
    public void testInvalidPathOrder() {
        String[] args = {"fileOne/fileTwo", "-r"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals(".", ap.findRoot(args));
    }

    @Test
    public void testDifferentArgsForPath() {
        String[] args = {"-p", "120", "-r", "my-path/path", "random flag"};
        ArgumentParser ap = new ArgumentParser(args);
        assertEquals("my-path/path", ap.findRoot(args));
    }

    @Test
    public void testFindFlag(){
        String[] args = {"-p", "120", "-r", "my-path/path", "-x"};
        ArgumentParser ap = new ArgumentParser(args);
        assertTrue(ap.findFlag(args, "-x"));

        String[] args2 = {"-p", "120", "-r", "my-path/path"};
        ArgumentParser ap2 = new ArgumentParser(args2);
        assertFalse(ap2.findFlag(args2, "-x"));
    }

}
