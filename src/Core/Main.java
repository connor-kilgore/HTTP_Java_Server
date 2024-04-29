package Core;

public class Main {
    public static void main(String[] args) {
        ArgumentParser ap = new ArgumentParser(args);
        Server myServer = new Server(ap, new SocketHandlerFactory(),
                new ServerResponseFactory(ap.getRoot()), new ClientRPFactory());
        myServer.start();
    }
}