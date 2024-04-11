package Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class RequestParser {

    public static int buildHeaders(BufferedReader clientReader,
                                   StringBuilder request) throws IOException {
        String line;
        int contentLen = 0;
        while ((line = clientReader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length: "))
                contentLen = Integer.parseInt(line.substring("Content-Length: ".length()));
            request.append(line).append("\n");
        }

        return contentLen;
    }

    public static void buildContent(BufferedReader clientReader,
                                   StringBuilder request, int contentLen) throws IOException {
        while (contentLen > 0) {
            request.append((char) clientReader.read());
            contentLen--;
        }
    }

    public static String buildRequest(BufferedReader clientReader) throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        int contentLen = buildHeaders(clientReader, requestBuilder);
        requestBuilder.append("\n");
        buildContent(clientReader, requestBuilder, contentLen);
        return requestBuilder.toString();
    }

    public static String getRoot(String request) {
        return request.split(" ")[1].split("\\?")[0];
    }

    public static String getRequestType(String request) {
        return request.split(" ")[0].split("\\?")[0];
    }

    public static String[] getParameters(String request) {
        return request.split("\n\n")[1].split("&");
    }

    public static File getFile(String path, String root) {
        File newFile = new File(root + "/" + path);
        File indexFile = new File(root + "/" + path + "/index.html");

        if (newFile.isDirectory() && indexFile.exists())
            return indexFile;
        else
            return newFile;
    }

}
