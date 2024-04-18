package Core;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService threadPool;
    private final ArgumentParser ap;
    private static final int THREAD_POOL_SIZE = 25;
    private boolean isRunning = false;
    private final HandlerFactory handlerFactory;
    private final ResponseFactory responseFactory;
    private final RPFactory rpFactory;

    private final String HELP_MESSAGE = """
            The HTTP server behaves like a traditional command line tool with options.

            e.g. The command name to start the server is 'my-http-server', the -help option should print the following, perhaps
             with additional options:

            Usage: my-http-server [options]
              -p     Specify the port.  Default is 80.
              -r     Specify the root directory.  Default is the current working directory.
              -h     Print this help message
              -x     Print the startup configuration without starting the server

            Upon startup, the server should print it's startup configuration

            <server name>
            Running on port: <port>
            Serving files from: <dir>
            """;

    public Server(ArgumentParser ap, HandlerFactory handler,
                  ResponseFactory responseFactory, RPFactory rpFactory) {
        this.ap = ap;
        this.handlerFactory = handler;
        this.responseFactory = responseFactory;
        this.rpFactory = rpFactory;

        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public ArgumentParser getArgumentParser() {
        return ap;
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
        if (!ap.getHelpFlag()) {
            new Thread(() -> {
                try (ServerSocket sSocket = new ServerSocket(ap.getPort())) {
                    isRunning = !ap.getConfigFlag();
                    System.out.println("\nExample Server\nRunning on port: " + ap.getPort() +
                                       "\nServing files from: " +
                                       new File(responseFactory.getRoot()).getAbsolutePath() + "\n");
                    while (isRunning) {
                        Socket client = sSocket.accept();
                        RequestParser rp = rpFactory.newRP(client);
                        threadPool.submit(handlerFactory.newHandler(responseFactory, rp));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            System.out.println(HELP_MESSAGE);
        }
    }
}
