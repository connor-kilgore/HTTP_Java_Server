package Core;

public interface Handler extends Runnable {
    public void handleClient() throws Exception;
}
