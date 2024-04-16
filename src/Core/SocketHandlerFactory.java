package Core;

import mock.MockRequestParser;
import mock.MockResponse;

import java.io.IOException;
import java.net.Socket;

public class SocketHandlerFactory implements HandlerFactory {

    public Handler newHandler(ResponseFactory responseFactory, RequestParser rp) throws IOException {
        return new SocketHandler(responseFactory, rp);
    }
}
