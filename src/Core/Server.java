package Core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    int port;
    String root;
    private static final int THREAD_POOL_SIZE = 25;

    public Server(int port, String root) {
        this.port = port;
        this.root = root;
    }

    public void startServer() throws Exception {
        try (ServerSocket sSocket = new ServerSocket(port)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            System.out.println("\nRunning server...\nPort: " + port + " Root: " + root);
            while (true) {
                Socket client = sSocket.accept();
                threadPool.submit(new Client(client, root));
            }
        }
    }
}
