package Core;

public interface Request {
    ResponseNode handleRequest(RequestParser rp, String root);

    void setContentArray(byte[] content);
}
