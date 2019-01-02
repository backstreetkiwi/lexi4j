package de.zaunkoenigweg.lexi4j.exiftool;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.Test;

public class ExifDataTest {

    @Test
    public void testRead() {
        HashMap<String, String> rawExif = new HashMap<>();
        rawExif.put("Image Description", "the image description");
        rawExif.put("Date/Time Original", "2003:04:08 19:10:57");
        rawExif.put("Sub Sec Time Original", "123");
        rawExif.put("Make", "Nikon");
        rawExif.put("Camera Model Name", "Nikon D70");
        rawExif.put("User Comment", "the user comment");
        ExifData exifData = ExifData.of(rawExif);
        assertNotNull(exifData);
        assertTrue(exifData.getImageDescription().isPresent());
        assertEquals("the image description", exifData.getImageDescription().get());
        assertTrue(exifData.getDateTimeOriginal().isPresent());
        assertEquals(LocalDateTime.of(2003, 4, 8, 19, 10, 57), exifData.getDateTimeOriginal().get());
        assertTrue(exifData.getSubsecTimeOriginal().isPresent());
        assertEquals(Integer.valueOf(123), exifData.getSubsecTimeOriginal().get());
        assertTrue(exifData.getCameraMake().isPresent());
        assertEquals("Nikon", exifData.getCameraMake().get());
        assertTrue(exifData.getCameraModel().isPresent());
        assertEquals("Nikon D70", exifData.getCameraModel().get());
        assertTrue(exifData.getUserComment().isPresent());
        assertEquals("the user comment", exifData.getUserComment().get());
    }

}
