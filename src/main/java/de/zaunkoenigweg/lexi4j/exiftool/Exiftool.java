package de.zaunkoenigweg.lexi4j.exiftool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Exiftool {

    private static final Pattern KEY_VALUE_LINE_PATTERN = Pattern.compile("^([^:]+)\\s*:\\s(.+)$");
    private static final Pattern SEPARATOR = Pattern.compile("^========.*$");

    // TODO Wrap method for single file usage
    
    /**
     * Reads EXIF metadata from all mediafiles in the given path (pattern).
     * 
     * @param pathPattern Path pattern, passed to exiftool 'as is'.
     * 
     * @return Map of Files/ExifData.
     */
    public static Map<File, ExifData> readPaths(String pathPattern) {
        if (StringUtils.isBlank(pathPattern)) {
            throw new IllegalArgumentException("missing argument 'pathPattern'");
        }

        String exiftoolParams = ExifData.exiftoolParams()
                        .map(param -> "-" + param)
                        .collect(Collectors.joining(" ")) + " -fileName -directory";

        Pair<Integer, List<String>> result = callExiftool(exiftoolParams, pathPattern);

        if (0 != result.getLeft()) {
            String message = String.format("call of exiftool not successfull (%d)", result.getLeft());
            if (result.getRight() != null) {
                message += "\n" + result.getRight().stream().collect(Collectors.joining("\n"));
            }
            throw new IllegalStateException(message);
        }

        List<Map<String,String>> intermediateData = new ArrayList<>();

        Map<String,String> currentFile = new HashMap<>();
        
        for (String line : result.getRight()) {
            Matcher separatorMatcher = SEPARATOR.matcher(line);
            if (separatorMatcher.matches()) {
                intermediateData.add(currentFile);
                currentFile = new HashMap<>();
            } else {
                Optional<Pair<String, String>> parsedLine = parseLine(line);
                if(parsedLine.isPresent()) {
                    currentFile.put(parsedLine.get().getKey(), parsedLine.get().getValue());
                }
            }
        }
        intermediateData.add(currentFile);
        
        Function<Map<String,String>, File> keyMapper = rawExifMap -> 
            new File(new File(rawExifMap.get("Directory")), rawExifMap.get("File Name"));
        
        return intermediateData.stream().collect(Collectors.toMap(keyMapper, ExifData::of));
    }

    private static Pair<Integer, List<String>> callExiftool(String params, String path) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String command = String.format("exiftool %s %s", params, path);
            builder.command("sh", "-c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            return Pair.of(exitCode, lines);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Optional<Pair<String, String>> parseLine(String line) {
        Matcher matcher = KEY_VALUE_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(Pair.of(matcher.group(1).trim(), matcher.group(2)));
    }
}
