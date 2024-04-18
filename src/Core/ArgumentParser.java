package Core;

public class ArgumentParser {

    private final int port;
    private final String root;
    private final boolean configFlag;
    private final boolean helpFlag;
    public ArgumentParser(String[] args) {
        port = findPort(args);
        root = findRoot(args);
        configFlag = findFlag(args, "-x");
        helpFlag = findFlag(args, "-h");
    }

    public int getPort(){
        return port;
    }

    public String getRoot(){
        return root;
    }

    public boolean getConfigFlag(){
        return configFlag;
    }

    public boolean getHelpFlag(){
        return helpFlag;
    }

    public int findPort(String[] args) {
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

    public String findRoot(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-r")) {
                return args[i + 1];
            }
        }
        return ".";
    }

    public boolean findFlag(String[] args, String flag) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flag)) {
                return true;
            }
        }
        return false;
    }


}
