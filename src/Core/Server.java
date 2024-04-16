package Core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService threadPool;
    private final int port;
    private static final int THREAD_POOL_SIZE = 25;
    private boolean isRunning = false;
    private final HandlerFactory handlerFactory;
    private final ResponseFactory responseFactory;
    private final RPFactory rpFactory;

    public Server(int port, HandlerFactory handler,
                  ResponseFactory responseFactory, RPFactory rpFactory) {
        this.port = port;
        this.handlerFactory = handler;
        this.responseFactory = responseFactory;
        this.rpFactory = rpFactory;

        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public int getPort() {
        return port;
    }

    public HandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public ResponseFactory getResponseFactory() {
        return responseFactory;
    }

    public RPFactory getRPFactory() {
        return rpFactory;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket sSocket = new ServerSocket(port)) {
                isRunning = true;
                System.out.println("\nRunning server...\nPort: " + port +
                        " Root: " + responseFactory.getRoot());
                while (isRunning) {
                    Socket client = sSocket.accept();
                    RequestParser rp = rpFactory.newRP(client);
                    threadPool.submit(handlerFactory.newHandler(responseFactory, rp));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
