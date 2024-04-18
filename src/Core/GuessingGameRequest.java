package Core;

public class GuessingGameRequest implements Request {
    private String[] parameters;
    public ResponseNode handleRequest(RequestParser rp, String root) {
        GuessingGame gg;

        if (parameters != null)
            gg = new GuessingGame(parameters);
        else
            gg = new GuessingGame();

        return updateGuessingGame(gg);
    }

    public ResponseNode updateGuessingGame(GuessingGame gg){
        String endCond = gg.getEndCondition();
        if (endCond.isEmpty())
            return generateGuess(gg);
        else
            return generateEndCondition(endCond);
    }

    public ResponseNode generateEndCondition(String endCond) {
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
        String content = "<!DOCTYPE html>" +
                     "<html><head><title> Java HTTP Core.Server </title></head>" +
                     "<body><br><br><div>" +
                     "<h3>" + endCond + "</h3></div></body></html>";

        return new ResponseNode(status, headers, content.getBytes());
    }

    public ResponseNode generateGuess(GuessingGame gg){
        String status = "HTTP/1.1 200 OK";
        String[] headers = {"Content-Type: text/html", "Server: Example Server"};
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

        return new ResponseNode(status, headers, content.getBytes());
    }

    public void setContentArray(byte[] content) {
        String contentStr = "";
        for(int i: content)
            contentStr += (char) i;
        this.parameters = contentStr.split("\n")[0].split("&");
    }

}
