package info.lindblad.radio.util;


import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.Point;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class InputParser {

    public static final String DEFAULT_INPUT_FILENAME = "input.txt";

    private static final int ISLAND_DIMENSIONS_PARAMETER_SIZE = 2;
    private static final int TRANSMITTER_TOWER_PARAMETER_SIZE = 4;
    private static final int RECEIVER_TOWER_PARAMETER_SIZE = 3;

    /**
     * Attempt to parse an input stream and return an optional island
     *
     * @param inputStream The InputStream
     * @return An Optional<Island> whose value depends on successful parsing
     */
    private static Optional<Island> parse(Stream<String> inputStream) {
        List<List<Integer>> input = inputStream
                .map(InputParser::extractIntegerValues).collect(Collectors.toList());

        if (input.size() >= 1 && input.get(0).size() == ISLAND_DIMENSIONS_PARAMETER_SIZE) {
            Island island = new Island(input.get(0).get(0), input.get(0).get(1));

            input.subList(1, input.size()).forEach(parameters -> {
                if (parameters.size() == TRANSMITTER_TOWER_PARAMETER_SIZE) {
                    int id = parameters.get(0);
                    Point point = new Point(parameters.get(1), parameters.get(2));
                    int power = parameters.get(3);
                    island.addTransmitterTower(new TransmitterTower(id, point, power));
                } else if (parameters.size() == RECEIVER_TOWER_PARAMETER_SIZE) {
                    int id = parameters.get(0);
                    Point point = new Point(parameters.get(1), parameters.get(2));
                    island.addReceiverTower(new ReceiverTower(id, point));
                }
            });
            return Optional.ofNullable(island);
        }

        return Optional.empty();
    }

    /**
     * Parse from BufferedReader
     *
     * @param bufferedReader Input BufferedReader to parse
     * @return An Optional<Island> whose value depends on successful parsing
     */
    public static Optional<Island> parse(BufferedReader bufferedReader) {
        return parse(bufferedReader.lines());
    }

    /**
     * Parse from array of strings
     *
     * @param input Array of strings to parse
     * @return An Optional<Island> whose value depends on successful parsing
     */
    public static Optional<Island> parse(String[] input) {
        return parse(Arrays.stream(input));
    }

    /**
     * Parse from string
     *
     * @param input String to parse
     * @return An Optional<Island> whose value depends on successful parsing
     */
    public static Optional<Island> parse(String input) {
        String eol = System.getProperty("line.separator");
        return parse(input.split(eol));
    }

    /**
     * Read a file from the class resources
     *
     * @param filename The full filename of the file within resources
     * @return An optional BufferedReader
     */
    private static Optional<BufferedReader> readFileFromResources(String filename) {
        ClassLoader classLoader = InputParser.class.getClassLoader();
        try {
            FileReader fileReader = new FileReader(classLoader.getResource(filename).getFile());
            return Optional.of(new BufferedReader(fileReader));
        } catch (FileNotFoundException exception) {
            System.err.println(String.format("No such file in resources '%s': %s", filename, exception));
        }
        return Optional.empty();
    }

    /**
     * Read a file from the file system
     *
     * @param filename The filename of the file
     * @return An optional BufferedReader
     */
    private static Optional<BufferedReader> readFileFromFileSystem(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            return Optional.of(new BufferedReader(fileReader));
        } catch (FileNotFoundException exception) {
            System.err.println(String.format("No such file '%s': %s", filename, exception));
        }
        return Optional.empty();
    }

    /**
     * Read an island from a file within the class resources
     *
     * Useful for tests
     *
     * @param filename The full filename of the file within resources
     * @return
     */
    public static Island islandFromResourceFile(String filename) {
        Optional<BufferedReader> readerOptional = readFileFromResources(filename);
        Optional<Island> islandOptional = InputParser.parse(readerOptional.orElseThrow(() -> new RuntimeException("Cannot load required test resource")));
        return islandOptional.orElseThrow(() -> new RuntimeException("Cannot parse input file into island"));
    }

    /**
     * Read an island from a file within the class resources
     *
     * Useful for tests
     *
     * @param filename The full filename of the file within resources
     * @return
     */
    public static Island islandFromFile(String filename) {
        Optional<BufferedReader> readerOptional = readFileFromFileSystem(filename);
        Optional<Island> islandOptional = InputParser.parse(readerOptional.orElseThrow(() -> new RuntimeException("Cannot load required test resource")));
        return islandOptional.orElseThrow(() -> new RuntimeException("Cannot parse input file into island"));
    }

    /**
     * Extract a list of integers from a given input string
     *
     * @param line The line to extract integers from
     * @return A list of integers extracted from the string
     */
    private static List<Integer> extractIntegerValues(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        List<Integer> values = new ArrayList<>();
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            try {
                values.add(Integer.parseInt(matcher.group()));
            } catch (Exception e) {
                System.err.println(String.format("Could not extract integer values from line '%s': %s", line, e.toString()));
            }
        }
        return values;
    }

}
