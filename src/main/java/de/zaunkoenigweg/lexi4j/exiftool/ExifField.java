package de.zaunkoenigweg.lexi4j.exiftool;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface ExifField {

    /**
     * Param that must be set as param when calling the Linux exiftool.
     */
    String exiftoolParam();
    
    /**
     * EXIF key.
     */
    String exifKey();
    
    /**
     * Type of the field.
     */
    String exifFieldType();

}
