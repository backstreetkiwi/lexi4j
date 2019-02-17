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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Exiftool {

    private static final Pattern KEY_VALUE_LINE_PATTERN = Pattern.compile("^([^:]+)\\s*:\\s(.+)$");
    private static final Pattern SEPARATOR = Pattern.compile("^========.*$");

    Map<File,ExifData> cache;
    
    public Exiftool() {
        this.cache = new HashMap<>();
    }
    
    /**
     * Reads EXIF metadata for the given file.
     * 
     * @param file Image file.
     * 
     * @return EXIF data (optional)
     */
    public Optional<ExifData> read(File file) {
        if(file==null || !file.exists() || file.isDirectory()) {
            return Optional.empty();
        }
        
        ExifData cachedExifData = this.cache.get(file);
        if(cachedExifData!=null) {
            return Optional.of(cachedExifData);
        }
        try {
            fillCache(file.getAbsolutePath());
        }
        catch(IllegalStateException e) {
            return Optional.empty();
        }
        return Optional.of(this.cache.get(file));
    }
    
    /**
     * Reads EXIF metadata from all mediafiles in the given path (pattern) and puts them into the cache.
     * 
     * This method forces a call of the underlying exiftool binary and does not read from cache.
     * The returned values will be cached though.
     * 
     * @param pathPattern Path pattern, passed to exiftool 'as is'.
     * 
     * @return Map of Files/ExifData.
     */
    public void fillCache(String pathPattern) {
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
            if (separatorMatcher.matches() && !currentFile.isEmpty()) {
                intermediateData.add(currentFile);
                currentFile = new HashMap<>();
            } else {
                Optional<Pair<String, String>> parsedLine = parseKeyValueLine(line);
                if(parsedLine.isPresent()) {
                    currentFile.put(parsedLine.get().getKey(), parsedLine.get().getValue());
                }
            }
        }
        
        if(!currentFile.isEmpty()) {
            intermediateData.add(currentFile);
        }
        
        synchronized (this.cache) {
            intermediateData.stream().forEach(rawExifData -> {
                this.cache.put(new File(new File(rawExifData.get("Directory")), rawExifData.get("File Name")), ExifData.of(rawExifData));
            });
        }
    }
    
    public ExifDataUpdate update(File file) {
        return new Update(file);
    }
    
    private class Update extends ExifDataUpdate {

        private File file;
        
        private Update(File file) {
            this.file = file;
        }
        
        @Override
        public void perform() {
            String params = this.updateValues.entrySet().stream()
                            .map(entry -> String.format("-%s=\"%s\"", entry.getKey().getExiftoolParam(), StringUtils.replace(entry.getValue(), "\"", "\\\"")))
                            .collect(Collectors.joining(" "));

            params = "-overwrite_original " + params;

            Pair<Integer, List<String>> result = callExiftool(params, file.getAbsolutePath());

            if (0 != result.getLeft()) {
                String message = String.format("call of exiftool not successful (%d)", result.getLeft());
                if (result.getRight() != null) {
                    message += "\n" + result.getRight().stream().collect(Collectors.joining("\n"));
                }
                throw new IllegalStateException(message);
            }
            Exiftool.this.cache.remove(this.file);
        }
        
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

    private static Optional<Pair<String, String>> parseKeyValueLine(String line) {
        Matcher matcher = KEY_VALUE_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(Pair.of(matcher.group(1).trim(), matcher.group(2)));
    }
}
