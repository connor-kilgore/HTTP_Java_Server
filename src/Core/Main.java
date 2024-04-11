package Core;

public class Main {
    public static void main(String[] args) throws Exception {
        Server myServer = new Server(ArgumentParser.getPort(args), ArgumentParser.getPath(args));
        myServer.startServer();
    }
}