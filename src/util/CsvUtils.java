package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public final class CsvUtils {

    private CsvUtils() {
    }

    public enum CsvActions {
        DELETE,
        UPDATE
    }

    public static void manageCSV(
            final Path inputFile,
            final int column,
            final String matchValue,
            final CsvActions action,
            final String newValue
    ) throws IOException {
        Path tempFile = Paths.get("src", "resources", "tempDataStorage.csv");

        try (
                BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(
                        tempFile,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                if (parts.length > column && parts[column].equals(matchValue)) {
                    if (action == CsvActions.DELETE) {
                        continue;
                    } else if (action == CsvActions.UPDATE) {
                        line = newValue;
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        }
        Files.move(tempFile, inputFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteLine(final Path inputFile, final int column, final String matchValue) throws IOException {
        manageCSV(inputFile, column, matchValue, CsvActions.DELETE, null);
    }

    public static void updateLine(final Path inputFile, final String matchValue, final String newValue) throws IOException {
        manageCSV(inputFile, 0, matchValue, CsvActions.UPDATE, newValue);
    }

    public static ArrayList<String> getDataFromFile(final Path inputFile) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("id,type,name,status,description,epic")) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }
}
