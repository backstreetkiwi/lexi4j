package de.zaunkoenigweg.lexi4j.exiftool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

/**
 * Exif Fields that can be used via the Linux exiftool.
 * 
 * An Exif Field has a type T that can be serialized to a String or deserialized from a String.
 * 
 * @author mail@nikolaus-winter.de
 */
class ExifFieldType<T> {

    private static final DateTimeFormatter EXIF_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
    
    static ExifFieldType<Integer> THREE_DIGIT_INTEGER = new ExifFieldType<>(ExifFieldType::deserializeTreeDigitInteger, ExifFieldType::serializeTreeDigitInteger);
    static ExifFieldType<String> STRING = new ExifFieldType<>(ExifFieldType::deserializeString, ExifFieldType::serializeString);
    static ExifFieldType<LocalDateTime> DATETIME = new ExifFieldType<>(ExifFieldType::deserializeDatetime, ExifFieldType::serializeDatetime);

    /**
     * Function to deserialize field from String.
     * Null values are allowed and may be returned.
     */
    private Function<String, T> deserializer;
    
    /**
     * Function to serialize field to String.
     * Null values are allowed and may be returned.
     */
    private Function<T, String> serializer;
    
    private ExifFieldType(Function<String, T> deserializer, Function<T, String> serializer) {
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    Function<String, T> getDeserializer() {
        return deserializer;
    }

    Function<T, String> getSerializer() {
        return serializer;
    }
    
    static ExifFieldType<?> byKey(String key) {
        switch (key) {
        case "STRING":
            return STRING;
        case "DATETIME":
            return DATETIME;
        case "THREE_DIGIT_INTEGER":
            return THREE_DIGIT_INTEGER;
        default:
            throw new IndexOutOfBoundsException(String.format("Unknown key for ExifFieldType: '%s'", key));
        }
    }
    
    // serializers / deserializers
    
    private static String serializeString(String in) {
        if(in==null) {
            return "";
        }
        return in.trim();
    }

    private static String deserializeString(String in) {
        if(StringUtils.isBlank(in)) {
            return null;
        }
        return in.trim();
    }

    private static String serializeTreeDigitInteger(Integer in) {
        if(in==null) {
            return "";
        }
        if(in < 0 || in > 999) {
            throw new IllegalArgumentException(String.format("%d must be between 0 and 999.", in));
        }
        return String.format("%03d", in);
    }

    private static Integer deserializeTreeDigitInteger(String in) {
        if(StringUtils.isBlank(in)) {
            return null;
        }
        if(!StringUtils.isNumeric(in)) {
            throw new IllegalArgumentException(String.format("%s is not a numeric value.", in));
        }
        Integer value = Integer.parseInt(in);
        if(value < 0 || value > 999) {
            throw new IllegalArgumentException(String.format("%s is not a numeric value between 0 and 999.", in));
        }
        return value;
    }

    private static String serializeDatetime(LocalDateTime in) {
        if(in==null) {
            return "";
        }
        return EXIF_DATE_TIME_FORMATTER.format(in);
    }

    private static LocalDateTime deserializeDatetime(String in) {
        if(StringUtils.isBlank(in)) {
            return null;
        }
        try {
            return LocalDateTime.parse(StringUtils.replaceChars(in, '-', ':'), EXIF_DATE_TIME_FORMATTER);
          } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("Exif Datetime '%s' could not be parsed.", in));
          }
    }

}
