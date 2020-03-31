package server;

import java.io.File;
import java.io.IOException;

public class Main {
    private static void ensureExpectedArgs(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -jar path/to/server.jar <directory>");
        }

        File directory = new File(args[0]);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Verify the provided argument is an existing and valid directory path");
        }
    }

    public static void main(String... args) throws IOException {
        ensureExpectedArgs(args);

        File directory = new File(args[0]);
        Server.start(directory);
    }
}
