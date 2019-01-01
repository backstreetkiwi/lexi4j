package de.zaunkoenigweg.lexi4j.exiftool;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class ExifFieldTypeTest {

    @Test
    public void testStringDeserialization() {
        assertSame("bla", ExifFieldType.STRING.getDeserializer().apply("bla"));
        assertEquals(null, ExifFieldType.STRING.getDeserializer().apply(null));
        assertEquals(null, ExifFieldType.STRING.getDeserializer().apply(""));
        assertEquals(null, ExifFieldType.STRING.getDeserializer().apply("   "));
    }

    @Test
    public void testStringSerialization() {
        assertSame("bla", ExifFieldType.STRING.getSerializer().apply("bla"));
        assertEquals("", ExifFieldType.STRING.getSerializer().apply(null));
        assertEquals("", ExifFieldType.STRING.getSerializer().apply(""));
        assertEquals("", ExifFieldType.STRING.getSerializer().apply("   "));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThreeDigitIntegerSerializationValueTooLow() {
        ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeDigitIntegerSerializationValueTooHigh() {
        ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(1000);
    }

    @Test
    public void testThreeDigitIntegerSerialization() {
        assertEquals("", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(null));
        assertEquals("000", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(0));
        assertEquals("001", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(1));
        assertEquals("012", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(12));
        assertEquals("123", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(123));
        assertEquals("999", ExifFieldType.THREE_DIGIT_INTEGER.getSerializer().apply(999));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeDigitIntegerDeserializationValueTooLow() {
        ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeDigitIntegerDeserializationValueTooHigh() {
        ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeDigitIntegerDeserializationNoNumericValue() {
        ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("something");
    }

    @Test
    public void testThreeDigitIntegerDeserialization() {
        assertEquals(null, ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply(null));
        assertEquals(null, ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply(""));
        assertEquals(null, ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("    "));
        assertEquals(Integer.valueOf(0), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("0"));
        assertEquals(Integer.valueOf(0), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("000"));
        assertEquals(Integer.valueOf(1), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("1"));
        assertEquals(Integer.valueOf(1), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("001"));
        assertEquals(Integer.valueOf(12), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("12"));
        assertEquals(Integer.valueOf(12), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("012"));
        assertEquals(Integer.valueOf(123), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("123"));
        assertEquals(Integer.valueOf(999), ExifFieldType.THREE_DIGIT_INTEGER.getDeserializer().apply("999"));
    }

    @Test
    public void testDatetimeSerialization() {
        assertEquals("", ExifFieldType.DATETIME.getSerializer().apply(null));
        assertEquals("2018:12:24 21:55:53", ExifFieldType.DATETIME.getSerializer().apply(LocalDateTime.of(2018, 12, 24, 21, 55, 53)));
    }

    @Test
    public void testDatetimeDeserialization() {
        assertEquals(null, ExifFieldType.DATETIME.getDeserializer().apply(null));
        assertEquals(null, ExifFieldType.DATETIME.getDeserializer().apply(""));
        assertEquals(null, ExifFieldType.DATETIME.getDeserializer().apply("   "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDatetimeDeserializationInvalidValue() {
        ExifFieldType.DATETIME.getDeserializer().apply("something");
    }

    
    
}
