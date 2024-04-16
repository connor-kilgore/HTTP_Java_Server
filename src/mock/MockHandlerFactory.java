package mock;

import Core.*;

import java.io.IOException;
import java.net.Socket;

public class MockHandlerFactory implements HandlerFactory {
    public Handler newHandler(ResponseFactory responseFactory, RequestParser rp) throws IOException {
        return new MockHandler(rp.getSocket());
    }
}
