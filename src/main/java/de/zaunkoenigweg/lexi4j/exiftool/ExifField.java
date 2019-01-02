package de.zaunkoenigweg.lexi4j.exiftool;

public enum ExifField {

    IMAGE_DESCRIPTION("imageDescription", "Image Description", ExifFieldType.STRING),
    DATETIME_ORIGINAL("dateTimeOriginal", "Date/Time Original", ExifFieldType.DATETIME),
    SUBSEC_TIME_ORIGINAL("subSecTimeOriginal", "Sub Sec Time Original", ExifFieldType.THREE_DIGIT_INTEGER),
    CAMERA_MAKE("make", "Make", ExifFieldType.STRING),
    CAMERA_MODEL("model", "Camera Model Name", ExifFieldType.STRING),
    USER_COMMENT("userComment", "User Comment", ExifFieldType.STRING);
    
    private ExifField(String exiftoolParam, String exifKey, ExifFieldType<?> exifFieldType) {
        this.exiftoolParam = exiftoolParam;
        this.exifKey = exifKey;
        this.exifFieldType = exifFieldType;
    }

    /**
     * Param that must be set as param when calling the Linux exiftool.
     */
    private String exiftoolParam;
    
    /**
     * EXIF key.
     */
    private String exifKey;
    
    /**
     * Type of the field.
     */
    private ExifFieldType<?> exifFieldType;

    String getExiftoolParam() {
        return exiftoolParam;
    }

    String getExifKey() {
        return exifKey;
    }

    ExifFieldType<?> getExifFieldType() {
        return exifFieldType;
    }

    
}
