package Core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

public class Response {

    public static String generateResponse(String status, String[] headers) {
        StringBuilder str = new StringBuilder();
        str.append(status).append("\n");
        for (String header : headers)
            str.append(header).append("\n");
        str.append("\n");

        return str.toString();
    }

    public static void sendFile(File contentFile, OutputStream clientOutput) throws IOException {
        String contentType = Files.probeContentType(contentFile.toPath());
        byte[] content = Files.readAllBytes(contentFile.toPath());
        String[] headers = {"ContentType: " + contentType};
        String status = "HTTP/1.1 200 OK";


        clientOutput.write(generateResponse(status, headers).getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n".getBytes());
    }

    public static void sendNotFound(OutputStream clientOutput) throws IOException {
        byte[] content = "<b>Error 404 (File Not Found)</b>".getBytes();
        String[] headers = {"ContentType: text/html"};
        String status = "HTTP/1.1 404 Not Found";

        clientOutput.write(generateResponse(status, headers).getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n".getBytes());
    }

    private static String listFileInHTML(String fileName) {
        return "<li><a href=\"./" + fileName + "/\">" + fileName + "</a></li>";
    }

    public static String listFilesInHTML(File[] files, String directoryName)
    {
        StringBuilder fileList = new StringBuilder();
        fileList.append("<h1>/").append(directoryName).append("</h1>");
        fileList.append("<ul><h3>").append(listFileInHTML(".")).append(listFileInHTML(".."));
        if (files != null) {
            for (File f : files)
                fileList.append(listFileInHTML(f.getName()));
        }
        fileList.append("</h3></ul>");

        return fileList.toString();
    }

    public static void sendDirectory(File contentFile, OutputStream clientOutput) throws IOException {
        File[] files = contentFile.listFiles();
        byte[] content = listFilesInHTML(files, contentFile.getName()).getBytes();
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html"};

        clientOutput.write(generateResponse(status, headers).getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n".getBytes());
    }

    public static void sendGETResponse(Socket clientSocket, String request, String root)
            throws IOException, InterruptedException {

        String contentRoot = RequestParser.getRoot(request);
        File contentFile = RequestParser.getFile(contentRoot, root);

        OutputStream clientOutput = clientSocket.getOutputStream();
        if (contentRoot.equals("/guess")) {
            GuessingGame gg = new GuessingGame();
            gg.updateGame(clientOutput);
        } else if (contentRoot.equals("/ping")) {
            Thread.sleep(1000);
            System.out.println("Pinged at: " + System.currentTimeMillis());
        } else if (contentFile.isDirectory()) {
            sendDirectory(contentFile, clientOutput);
        } else if (contentFile.exists())
            sendFile(contentFile, clientOutput);
        else
            sendNotFound(clientOutput);

        clientOutput.flush();
    }

    public static void sendPOSTResponse(Socket clientSocket, String request, String root)
            throws IOException, InterruptedException {

        String contentRoot = RequestParser.getRoot(request);
        String[] parameters = RequestParser.getParameters(request);
        OutputStream clientOutput = clientSocket.getOutputStream();

        if (contentRoot.equals("/guess")) {
            GuessingGame gg = new GuessingGame(parameters);
            gg.updateGame(clientOutput);
        } else
            sendGETResponse(clientSocket, request, root);

        clientOutput.flush();
    }
}
