package de.zaunkoenigweg.lexi4j.exiftool;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ExiftoolTest {

    private File someEmptyFolder;
    
    @Before
    public void setUp() throws IOException {
        someEmptyFolder = Files.createTempDirectory("someEmptyFolder").toFile();
        someEmptyFolder.deleteOnExit();
    }
    
    @Test
    public void testReadOneFileNull() {
        Optional<ExifData> exifData = Exiftool.read(null);
        assertNotNull(exifData);
        assertFalse(exifData.isPresent());
    }

    @Test
    public void testReadOneFileThatDoesNotExist() {
        Optional<ExifData> exifData = Exiftool.read(new File(someEmptyFolder, "noimage.jpg"));
        assertNotNull(exifData);
        assertFalse(exifData.isPresent());
    }

    @Test
    public void testReadOneFileThatIsNoImage() {
        Optional<ExifData> exifData = Exiftool.read(new File(getClass().getResource("/exiftool/noimage").getPath()));
        assertNotNull(exifData);
        assertFalse(exifData.isPresent());
    }

    @Test
    public void testReadOneFile() {
        File iPhone5sFile = new File(getClass().getResource("/exiftool/NikonD70.jpg").getPath());
        
        Optional<ExifData> exifData = Exiftool.read(iPhone5sFile);
        assertTrue(exifData.isPresent());
        assertTrue(exifData.get().getDateTimeOriginal().isPresent());
        assertEquals(LocalDateTime.of(2005,2,22,13,51,32), exifData.get().getDateTimeOriginal().get());
        assertTrue(exifData.get().getSubsecTimeOriginal().isPresent());
        assertEquals(Integer.valueOf(80), exifData.get().getSubsecTimeOriginal().get());
        assertTrue(exifData.get().getImageDescription().isPresent());
        assertEquals("Christchurch; auf dem Cathedral Square", exifData.get().getImageDescription().get());
        assertTrue(exifData.get().getCameraMake().isPresent());
        assertEquals("NIKON CORPORATION", exifData.get().getCameraMake().get());
        assertTrue(exifData.get().getCameraModel().isPresent());
        assertEquals("NIKON D70", exifData.get().getCameraModel().get());
        assertFalse(exifData.get().getUserComment().isPresent());
    }

    @Test
    public void testEmptyFolder() {
        Map<File, ExifData> exifDataMap = Exiftool.readPaths(someEmptyFolder.getAbsolutePath());
        assertNotNull(exifDataMap);
        assertEquals(0, exifDataMap.size());
    }

    @Test
    public void testReadAllFiles() {
        File exampleFolder = new File(getClass().getResource("/exiftool/").getPath());
        File iPhone5sFile = new File(getClass().getResource("/exiftool/iPhone5s.jpg").getPath());
        File nikonD70File = new File(getClass().getResource("/exiftool/NikonD70.jpg").getPath());
        
        Map<File, ExifData> exifDataMap = Exiftool.readPaths(exampleFolder.getAbsolutePath());
        assertNotNull(exifDataMap);
        assertEquals(2, exifDataMap.size());
        assertTrue(exifDataMap.containsKey(iPhone5sFile));
        assertTrue(exifDataMap.containsKey(nikonD70File));
        
        ExifData exifData = exifDataMap.get(iPhone5sFile);
        assertTrue(exifData.getCameraMake().isPresent());
        assertEquals("Apple", exifData.getCameraMake().get());
        assertTrue(exifData.getCameraModel().isPresent());
        assertEquals("iPhone 5s", exifData.getCameraModel().get());
        
        exifData = exifDataMap.get(nikonD70File);
        assertTrue(exifData.getCameraMake().isPresent());
        assertEquals("NIKON CORPORATION", exifData.getCameraMake().get());
        assertTrue(exifData.getCameraModel().isPresent());
        assertEquals("NIKON D70", exifData.getCameraModel().get());
    }

    @Test
    public void testReadOneFileViaPathsMethod() {
        File iPhone5sFile = new File(getClass().getResource("/exiftool/iPhone5s.jpg").getPath());
        
        Map<File, ExifData> exifDataMap = Exiftool.readPaths(iPhone5sFile.getAbsolutePath());
        assertNotNull(exifDataMap);
        assertEquals(1, exifDataMap.size());
        assertTrue(exifDataMap.containsKey(iPhone5sFile));
        
        ExifData exifData = exifDataMap.get(iPhone5sFile);
        assertTrue(exifData.getCameraMake().isPresent());
        assertEquals("Apple", exifData.getCameraMake().get());
        assertTrue(exifData.getCameraModel().isPresent());
        assertEquals("iPhone 5s", exifData.getCameraModel().get());
    }

}
