package Core;

public class ArgumentParser {

    public static int getPort(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-p")) {
                try {
                    return Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    return 80;
                }
            }
        }
        return 80;
    }

    public static String getPath(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-r")) {
                return args[i + 1];
            }
        }
        return ".";
    }


}
