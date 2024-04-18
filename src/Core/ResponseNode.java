package Core;

public class ResponseNode {

    private final String status;
    private final String[] headers;
    private final byte[] content;

    public ResponseNode(String status, String[] headers, byte[] content) {
        this.status = status;
        this.headers = headers;
        this.content = content;
    }

    public String getStatus(){
        return status;
    }

    public String[] getHeaders(){
        return headers;
    }

    public byte[] getContent(){
        return content;
    }
}
