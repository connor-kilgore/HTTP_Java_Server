package Core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ResponseNode {

    private final String status;
    private final String[] headers;
    private final byte[] content;

    public ResponseNode(String status, String[] headers, byte[] content) {
        this.status = status;
        this.headers = headers;
        this.content = content;
    }

    public void sendResponse(Socket clientSocket) throws IOException {
        OutputStream os = clientSocket.getOutputStream();

        os.write(generateResponse());
    }

    public byte[] generateResponse() {
        StringBuilder str = new StringBuilder();
        str.append(status).append("\n");
        for (String header : headers)
            str.append(header).append("\n");
        str.append("\n");

        return combineHeadWithContent(str.toString().getBytes());
    }

    public byte[] combineHeadWithContent(byte[] head) {
        byte[] combined = new byte[head.length + content.length];
        int n = 0;
        for (; n < head.length; n++)
            combined[n] = head[n];
        for (int m = 0; m < content.length; n++, m++)
            combined[n] = content[m];

        return combined;
    }

    public static String convertBytesToString(byte[] response) {
        StringBuilder str = new StringBuilder();
        for (byte b : response)
            str.append((char) b);
        return str.toString();
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
