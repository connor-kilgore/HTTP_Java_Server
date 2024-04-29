package Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileResponse implements Response {
    public void handleResponse(RequestParser rp, String root) throws IOException {

        if (rp.getPath().startsWith("/ping")) {
            new PingResponse().handleResponse(rp, root);
            return;
        }

        File contentFile = getFile(root, rp.getPath());
        if (contentFile.isDirectory())
            generateDirectory(contentFile, root).sendResponse(rp.getSocket());
        else if (contentFile.exists()) {
            try {
                generateFile(contentFile).sendResponse(rp.getSocket());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else
            generateNotFound().sendResponse(rp.getSocket());
    }

    public ResponseNode generateFile(File contentFile) throws IOException {
        String contentType = Files.probeContentType(contentFile.toPath());
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: " + contentType, "Server: Example Server"};
        byte[] content = Files.readAllBytes(contentFile.toPath());

        return new ResponseNode(status, headers, content);
    }

    public ResponseNode generateDirectory(File contentFile, String root) {
        File[] files = contentFile.listFiles();
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        byte[] content = listFilesInHTML(files, root, contentFile.getName()).getBytes();

        return new ResponseNode(status, headers, content);
    }

    public String listFilesInHTML(File[] files, String root, String directoryName) {
        StringBuilder fileList = new StringBuilder();
        fileList.append("<h1>/").append(directoryName).append("</h1><ul>");
        if (files != null) {
            for (File f : files) {
                fileList.append(listFileInHTML(f, root));
            }
        }
        fileList.append("</h3></ul>");

        return fileList.toString();
    }

    private static String listFileInHTML(File file, String root) {
        String path;
        if (root.equals("."))
            path = file.getPath().split("\\.")[1];
        else
            path = file.getPath().split(root)[1];

        if (file.isDirectory()) {
            return "<li><a href=\"/listing" + path + "\">" + file.getName() + "</a></li>";
        } else {
            return "<li><a href=\"" + path + "\">" + file.getName() + "</a></li>";
        }
    }

    public ResponseNode generateNotFound() {
        String status = "HTTP/1.1 404 Not Found";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        String content = "<!DOCTYPE html>" +
                         "<html><head><title> Java HTTP Core.Server </title></head>" +
                         "<body><br><br><h1>Error 404 (File Not Found)</h1></body></html>";

        return new ResponseNode(status, headers, content.getBytes());
    }

    public File getFile(String root, String requestPath) {
        String filePath = requestPath.replaceFirst("/listing", "");

        File newFile = new File(root + "/" + filePath);
        File indexFile = new File(root + "/" + filePath + "/index.html");

        if (newFile.isDirectory() && indexFile.exists() && !requestPath.startsWith("/listing"))
            return indexFile;
        else
            return newFile;
    }

    public void setContentArray(byte[] content) {
    }
}
