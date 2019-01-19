package de.zaunkoenigweg.lexi4j.exiftool;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Exif Data 
 * 
 * As this lib is just a facade to the technical layer, all fields are {@link Optional} and there
 * is no semantic validation whatsoever.
 * 
 * @author mail@nikolaus-winter.de
 */
public class ExifData {

    @ExifMapping(exifField = ExifField.IMAGE_DESCRIPTION)
    private Optional<String> imageDescription = Optional.empty();
    
    @ExifMapping(exifField = ExifField.DATETIME_ORIGINAL)
    private Optional<LocalDateTime> dateTimeOriginal = Optional.empty();

    @ExifMapping(exifField = ExifField.SUBSEC_TIME_ORIGINAL)
    private Optional<Integer> subsecTimeOriginal = Optional.empty();

    @ExifMapping(exifField = ExifField.CAMERA_MAKE)
    private Optional<String> cameraMake = Optional.empty();

    @ExifMapping(exifField = ExifField.CAMERA_MODEL)
    private Optional<String> cameraModel = Optional.empty();
    
    @ExifMapping(exifField = ExifField.USER_COMMENT)
    private Optional<String> userComment = Optional.empty();
    
    /**
     * Mapping of Exif fields to fields of this class.
     */
    private static Map<ExifField, Field> mappedFields = Arrays.asList(ExifData.class.getDeclaredFields()).stream()
                    .filter(f -> f.isAnnotationPresent(ExifMapping.class))
                    .collect(Collectors.toMap((Field field) -> {
                        return ((ExifMapping) field.getAnnotation(ExifMapping.class)).exifField();
                    }, Function.identity()));
    
    /**
     * Reads the Exif Data from a String-based map and produces an {@link ExifData} object.
     * @param rawExif raw map from exiftool
     * @return Exif Data object
     */
    static ExifData of(Map<String, String> rawExif) {
        
        ExifData exifData = new ExifData();
        
        mappedFields.keySet().stream()
            .filter(exifField -> rawExif.containsKey(exifField.getExifKey()))
            .forEach(exifField -> {
                Field field = mappedFields.get(exifField);
                ExifMapping exifMapping = (ExifMapping)(field.getAnnotation(ExifMapping.class));
                try {
                    Object deserializedValue = exifMapping.exifField().getExifFieldType().getDeserializer().apply(rawExif.get(exifField.getExifKey()));
                    field.set(exifData, Optional.ofNullable(deserializedValue));
                } catch(IllegalAccessException | IllegalArgumentException e) {
                    // value stays empty
                }
            });
        
        return exifData;
    }

    static Stream<String> exiftoolParams() {
        return mappedFields.keySet().stream().map(ExifField::getExiftoolParam);
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
