package Core;

import java.io.IOException;

public interface HandlerFactory {

    public Handler newHandler(ResponseFactory responseFactory, RequestParser rp) throws IOException;

}
