package de.zaunkoenigweg.lexi4j.exiftool;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Exif Data 
 * 
 * @author mail@nikolaus-winter.de
 */
public class ExifData {

    @ExifField(exiftoolParam="imageDescription", exifKey="Image Description", exifFieldType="STRING")
    private Optional<String> imageDescription = Optional.empty();
    
    @ExifField(exiftoolParam="dateTimeOriginal", exifKey="Date/Time Original", exifFieldType="DATETIME")
    private Optional<LocalDateTime> dateTimeOriginal = Optional.empty();

    @ExifField(exiftoolParam="subSecTimeOriginal", exifKey="Sub Sec Time Original", exifFieldType="THREE_DIGIT_INTEGER")
    private Optional<Integer> subsecTimeOriginal = Optional.empty();

    @ExifField(exiftoolParam="make", exifKey="Make", exifFieldType="STRING")
    private Optional<String> cameraMake = Optional.empty();

    @ExifField(exiftoolParam="model", exifKey="Camera Model Name", exifFieldType="STRING")
    private Optional<String> cameraModel = Optional.empty();
    
    @ExifField(exiftoolParam="userComment", exifKey="User Comment", exifFieldType="STRING")
    private Optional<String> userComment = Optional.empty();
    
    static ExifData read(Map<String, String> rawExif) {
        Map<String, Field> exifKeyToFieldMap = Arrays.asList(ExifData.class.getDeclaredFields()).stream()
            .filter(f -> f.isAnnotationPresent(ExifField.class))
            .collect(Collectors.toMap((Field field) -> {
                return ((ExifField)field.getAnnotation(ExifField.class)).exifKey();
            }, Function.identity()));
        
        ExifData exifData = new ExifData();
        
        rawExif.keySet().stream()
            .filter(exifKeyToFieldMap::containsKey)
            .forEach(key -> {
                Field field = exifKeyToFieldMap.get(key);
                ExifField exifField = (ExifField)(field.getAnnotation(ExifField.class));
                ExifFieldType<?> exifFieldType = ExifFieldType.byKey(exifField.exifFieldType());
                try {
                    Object deserializedValue = exifFieldType.getDeserializer().apply(rawExif.get(key));
                    field.set(exifData, Optional.ofNullable(deserializedValue));
                } catch(IllegalAccessException | IllegalArgumentException e) {
                    // value stays empty
                }
            });

        return exifData;
    }

    public Optional<String> getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(Optional<String> imageDescription) {
        this.imageDescription = imageDescription;
    }

    public Optional<LocalDateTime> getDateTimeOriginal() {
        return dateTimeOriginal;
    }

    public void setDateTimeOriginal(Optional<LocalDateTime> dateTimeOriginal) {
        this.dateTimeOriginal = dateTimeOriginal;
    }

    public Optional<Integer> getSubsecTimeOriginal() {
        return subsecTimeOriginal;
    }

    public void setSubsecTimeOriginal(Optional<Integer> subsecTimeOriginal) {
        this.subsecTimeOriginal = subsecTimeOriginal;
    }

    public Optional<String> getCameraMake() {
        return cameraMake;
    }

    public void setCameraMake(Optional<String> cameraMake) {
        this.cameraMake = cameraMake;
    }

    public Optional<String> getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(Optional<String> cameraModel) {
        this.cameraModel = cameraModel;
    }

    public Optional<String> getUserComment() {
        return userComment;
    }

    public void setUserComment(Optional<String> userComment) {
        this.userComment = userComment;
    }
}
