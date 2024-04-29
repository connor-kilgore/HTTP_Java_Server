package Core;

import java.io.IOException;

public class SocketHandlerFactory implements HandlerFactory {

    public Handler newHandler(ResponseFactory responseFactory, RequestParser rp) throws IOException {
        return new SocketHandler(responseFactory, rp);
    }
}
