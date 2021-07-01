package de.zaunkoenigweg.lexi4j.thumbnails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public class ThumbnailGenerator {

    public static void generateThumbnailFromVideo(File source, File target, int height) throws ThumbnailGeneratorException {
        Objects.requireNonNull(source, "The source file must not be null.");
        Objects.requireNonNull(target, "The target file must not be null.");
        if (!source.exists()) {
            throw new IllegalArgumentException(String.format("The source file %s does not exist.", source));
        }
        if (target.exists()) {
            throw new IllegalArgumentException(String.format("The target file %s does already exist.", target));
        }
        if (height < 0 || height > 1000) {
            throw new IllegalArgumentException(String.format("The height is %d but must be between 0 and 999", height));
        }
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String command = String.format("ffmpeg -y -i %s -ss 00:00:00 -vf scale=-1:%d -vframes 1 %s", source, height, target);
            builder.command("sh", "-c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            String output = IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new ThumbnailGeneratorException(String.format("ffmpeg exited with code %d:%n%n%s%n", exitCode, output));
            }
        } catch (IOException | InterruptedException e) {
            throw new ThumbnailGeneratorException("System call to ffmpeg failed", e);
        }
        if (!target.exists()) {
            throw new ThumbnailGeneratorException("No thumbnail was generated for unknown reasons.");
        }
    }

    public static void generateThumbnailFromImage(File source, File target, int height) throws ThumbnailGeneratorException {
        Objects.requireNonNull(source, "The source file must not be null.");
        Objects.requireNonNull(target, "The target file must not be null.");
        if (!source.exists()) {
            throw new IllegalArgumentException(String.format("The source file %s does not exist.", source));
        }
        if (target.exists()) {
            throw new IllegalArgumentException(String.format("The target file %s does already exist.", target));
        }
        if (height < 0 || height > 1000) {
            throw new IllegalArgumentException(String.format("The height is %d but must be between 0 and 999", height));
        }
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String command = String.format("ffmpeg -y -i %s -vf scale=-1:%d %s", source, height, target);
            builder.command("sh", "-c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            String output = IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new ThumbnailGeneratorException(String.format("ffmpeg exited with code %d:%n%n%s%n", exitCode, output));
            }
        } catch (IOException | InterruptedException e) {
            throw new ThumbnailGeneratorException("System call to ffmpeg failed", e);
        }
        if (!target.exists()) {
            throw new ThumbnailGeneratorException("No thumbnail was generated for unknown reasons.");
        }
    }

}
