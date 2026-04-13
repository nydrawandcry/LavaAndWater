package Model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelConfigReader {

    private List<String> readLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    private void validateNotEmpty(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Файл уровня пуст");
        }
    }

    private void validateRectangular(List<String> lines) {
        int width = lines.get(0).length();

        for (String line : lines) {
            if (line.length() != width) {
                throw new IllegalArgumentException(
                        "Все строки уровня должны быть одинаковой длины"
                );
            }
        }
    }
}
