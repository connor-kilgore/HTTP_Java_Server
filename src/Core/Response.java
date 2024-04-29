package Core;

import java.io.IOException;

public interface Response {
    void handleResponse(RequestParser rp, String root) throws IOException;
    void setContentArray(byte[] content);
}
