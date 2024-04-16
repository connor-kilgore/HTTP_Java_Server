package Core;

public class Main {
    public static void main(String[] args) throws Exception {
        Server myServer = new Server(ArgumentParser.getPort(args), new SocketHandlerFactory(),
                new ClientResponseFactory(ArgumentParser.getPath(args)), new ClientRPFactory());
        myServer.start();
    }
}