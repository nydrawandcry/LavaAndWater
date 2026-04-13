package Model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelConfigReader {

    public LevelConfig read(String fileName) throws IOException {
        List<String> lines = readLines(fileName);

        validateNotEmpty(lines);
        validateRectangular(lines);

        LevelConfig config = createConfig(lines);
        parseLines(lines, config);
        config.validate();

        return config;
    }

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

    private LevelConfig createConfig(List<String> lines) {
        int height = lines.size();
        int width = lines.get(0).length();

        return new LevelConfig(height, width);
    }

    private void parseLines(List<String> lines, LevelConfig config) {
        for (int row = 0; row < lines.size(); row++) {
            parseLine(lines.get(row), row, config);
        }
    }

    private void parseLine(String line, int row, LevelConfig config) {
        for (int col = 0; col < line.length(); col++) {
            parseSymbol(line.charAt(col), row, col, config);
        }
    }

    private void parseSymbol(char symbol, int row, int col, LevelConfig config) {
        Position position = new Position(row, col);

        switch (symbol) { //мне не нравится этот switch. я только начала разбираться в конфигах, поэтому пока не знаю, как сделать лучше
            case '#':
                config.addWall(position);
                break;
            case 'P':
                config.setPlayerPosition(position);
                break;
            case 'E':
                config.setExitPosition(position);
                break;
            case 'L':
                config.addLava(position);
                break;
            case 'W':
                config.addWater(position);
                break;
            case 'I':
                config.addIronBlock(position);
                break;
            case '.':
                break;
            default:
                throw new IllegalArgumentException("Неизвестный символ уровня '" + symbol + "' в позиции (" + row + ", " + col + ")");
        }
    }
}
