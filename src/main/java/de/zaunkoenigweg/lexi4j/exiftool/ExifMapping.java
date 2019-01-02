package de.zaunkoenigweg.lexi4j.exiftool;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields of {@link ExifData} must be annotated with this mapping to a {@link ExifField}.
 * 
 * @author mail@nikolaus-winter.de
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface ExifMapping {

    /**
     * Exif field to which the annotated field should be mapped.
     */
    ExifField exifField();
}
