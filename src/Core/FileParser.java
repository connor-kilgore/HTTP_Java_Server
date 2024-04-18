package Core;

public class FileParser {

    public static int getFileSize(byte[] file) {
        String fileStr = getFileString(file);
        return fileStr.split("\r\n\r\n")[1].split("\r\n--")[0].length();
    }

    public static String extractHeaderValue(byte[] file, String query){
        String header = extractHeader(file, query);
        if (header.isEmpty())
            return header;
        else
            return header.split(": ")[1];
    }

    public static String extractFileName(byte[] file) {
        String contentLine = extractHeader(file, "Content-Disposition");
        for(String s : contentLine.split("; "))
            if(s.startsWith("filename"))
                return s.split("=")[1].replaceAll("\"", "");

        return "";
    }

    public static String extractHeader(byte[] file, String query){
        String fileStr = getFileString(file);

        for(String s : fileStr.split("\r\n"))
            if(s.startsWith(query))
                return s;

        return "";
    }

    private static String getFileString(byte[] file) {
        StringBuilder fileStr = new StringBuilder();
        for(byte b: file)
            fileStr.append((char) b);

        return fileStr.toString();
    }
}
