package de.zaunkoenigweg.lexi4j.exiftool;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.junit.Test;

public class ExiftoolTest {

    // TODO Test Edge cases
    // TODO Test all fields
    // TODO Test multiple files
    
    @Test
    public void testReadPaths() {
        File file = new File(getClass().getResource("/exiftool/NikonD70.jpg").getFile());
        Map<File, ExifData> exifDataMap = Exiftool.readPaths(file.getAbsolutePath());
        assertNotNull(exifDataMap);
        assertEquals(1, exifDataMap.size());
        assertTrue(exifDataMap.containsKey(file));
        ExifData exifData = exifDataMap.get(file);
        assertTrue(exifData.getCameraMake().isPresent());
        assertEquals("NIKON CORPORATION", exifData.getCameraMake().get());
        assertTrue(exifData.getCameraModel().isPresent());
        assertEquals("NIKON D70", exifData.getCameraModel().get());
    }

}
