package Core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

public class ClientResponse implements Response {

    public final Socket clientSocket;
    public final RequestParser rp;
    public final String root;

    public ClientResponse(Socket clientSocket, RequestParser rp, String root) {
        this.clientSocket = clientSocket;
        this.rp = rp;
        this.root = root;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public RequestParser getRequestParser() {
        return rp;
    }

    public String getRoot() {
        return root;
    }

    public static byte[] combineHeadWithContent(byte[] head, byte[] content) {
        byte[] combined = new byte[head.length + content.length];
        int n = 0;
        for (; n < head.length; n++)
            combined[n] = head[n];
        for (int m = 0; m < content.length; n++, m++)
            combined[n] = content[m];

        return combined;
    }

    public static byte[] generateResponse(String status, String[] headers, byte[] content) {
        StringBuilder str = new StringBuilder();
        str.append(status).append("\n");
        for (String header : headers)
            str.append(header).append("\n");
        str.append("\n");

        return combineHeadWithContent(str.toString().getBytes(), content);
    }

    public static String convertBytesToString(byte[] response) {
        StringBuilder str = new StringBuilder();
        for (byte b : response)
            str.append((char) b);
        return str.toString();
    }

    public static void sendFile(File contentFile, OutputStream clientOutput) throws IOException {
        String contentType = Files.probeContentType(contentFile.toPath());
        byte[] content = Files.readAllBytes(contentFile.toPath());
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: " + contentType};
        clientOutput.write(generateResponse(status, headers, content));
    }

    public static void sendNotFound(OutputStream clientOutput) throws IOException {
        byte[] content = "<b>Error 404 (File Not Found)</b>".getBytes();
        String[] headers = {"ContentType: text/html"};
        String status = "HTTP/1.1 404 Not Found";

        clientOutput.write(generateResponse(status, headers, content));
        clientOutput.write("\n".getBytes());
    }

    private static String listFileInHTML(String fileName) {
        return "<li><a href=\"./" + fileName + "/\">" + fileName + "</a></li>";
    }

    public static String listFilesInHTML(File[] files, String directoryName) {
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

        clientOutput.write(generateResponse(status, headers, content));
        clientOutput.write("\n".getBytes());
    }

    public static String generatePingResponse(long time) {
        StringBuilder str = new StringBuilder();
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html"};
        String content = "<h3>" + time + "</h3>";
        byte[] response = generateResponse(status, headers, content.getBytes());
        str.append(convertBytesToString(response));
        return str.toString();
    }

    public static void sendPing(OutputStream clientOutput)
            throws InterruptedException, IOException {
        Thread.sleep(1000);
        clientOutput.write(generatePingResponse(
                System.currentTimeMillis()).getBytes());
    }

    public static byte[] generateEndCondition(String endCond) {
        String str = "<!DOCTYPE html>" +
                "<html><head><title> Java HTTP Core.Server </title></head>" +
                "<body><br><br><div>" +
                "<h3>" + endCond + "</h3></div></body></html>";
        return str.getBytes();
    }

    public static byte[] generateGuess(GuessingGame gg) throws IOException {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"ContentType: text/html"};
        String content = "<!DOCTYPE html>" +
                "<html><head><title> Java HTTP Core.Server </title></head>" +
                "<body><br><br><div><form method=\"POST\" action=\"#\">" +
                "\t<label for=\"number\">Guess a number between 1-100</label>" +
                "<input type=\"number\" id=\"number\" name=\"number\">" +
                "<input type=\"hidden\" id=\"guesses-left\" name=\"guesses-left\" value=\"" +
                (gg.getGuesses()) + "\">" +
                "<input type=\"hidden\" id=\"number-to-guess\"" +
                " name=\"number-to-guess\" value=\"" + gg.getAnswer() + "\">" +
                "<br><input type=\"submit\"></form>" +
                "<h3>" + gg.getHint() + "</h3></div></body></html>";

        return generateResponse(status, headers, content.getBytes());
    }

    public static void updateGuessingGame(OutputStream clientOutput, GuessingGame gg)
            throws IOException {
        String endCond = gg.getEndCondition();
        if (endCond.isEmpty())
            clientOutput.write(generateGuess(gg));
        else
            clientOutput.write(generateEndCondition(endCond));
    }

    public void sendGETResponse()
            throws IOException, InterruptedException {

        String contentRoot = rp.getPath();
        File contentFile = rp.getFile(root);

        OutputStream clientOutput = clientSocket.getOutputStream();
        if (contentRoot.equals("/guess")) {
            GuessingGame gg = new GuessingGame();
            updateGuessingGame(clientOutput, gg);
        } else if (contentRoot.equals("/hello")) {
            sendFile(rp.getFile("website"), clientOutput);
        } else if (contentRoot.equals("/ping")) {
            sendPing(clientOutput);
        } else if (contentFile.isDirectory()) {
            sendDirectory(contentFile, clientOutput);
        } else if (contentFile.exists())
            sendFile(contentFile, clientOutput);
        else
            sendNotFound(clientOutput);

        clientOutput.flush();
    }

    public void sendPOSTResponse()
            throws IOException, InterruptedException {

        String contentRoot = rp.getPath();
        String[] parameters = rp.getParameters();
        OutputStream clientOutput = clientSocket.getOutputStream();

        if (contentRoot.equals("/guess")) {
            GuessingGame gg = new GuessingGame(parameters);
            updateGuessingGame(clientOutput, gg);
        } else
            sendGETResponse();

        clientOutput.flush();
    }
}
