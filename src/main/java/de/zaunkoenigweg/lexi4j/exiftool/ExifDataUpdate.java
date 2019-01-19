package de.zaunkoenigweg.lexi4j.exiftool;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * This object stores a set of {@link ExifField}s and their values to be used as a change set to update the EXIF data of a file.
 * 
 * The setters for the values can be chained, a call to {@link #perform()} terminates the chain and performs the update.
 * 
 * @author mail@nikolaus-winter.de
 */
public abstract class ExifDataUpdate {

    protected Map<ExifField, String> updateValues = new HashMap<>();
    
    public ExifDataUpdate withImageDescription(String imageDescription) {
        updateValues.put(ExifField.IMAGE_DESCRIPTION, ExifFieldType.STRING.getSerializer().apply(imageDescription));
        return this;
    }

    public ExifDataUpdate withDateTimeOriginal(LocalDateTime dateTimeOriginal) {
        updateValues.put(ExifField.DATETIME_ORIGINAL, ExifFieldType.DATETIME.getSerializer().apply(dateTimeOriginal));
        return this;
    }

    public ExifDataUpdate withSubsecTimeOriginal(Integer subsecTimeOriginal) {
        updateValues.put(ExifField.SUBSEC_TIME_ORIGINAL, ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(subsecTimeOriginal));
        return this;
    }

    public ExifDataUpdate withCameraMake(String cameraMake) {
        updateValues.put(ExifField.CAMERA_MAKE, ExifFieldType.STRING.getSerializer().apply(cameraMake));
        return this;
    }

    public ExifDataUpdate withCameraModel(String cameraModel) {
        updateValues.put(ExifField.CAMERA_MODEL, ExifFieldType.STRING.getSerializer().apply(cameraModel));
        return this;
    }

    public ExifDataUpdate withUserComment(String userComment) {
        updateValues.put(ExifField.USER_COMMENT, ExifFieldType.STRING.getSerializer().apply(userComment));
        return this;
    }
    
    public abstract void perform();
}
