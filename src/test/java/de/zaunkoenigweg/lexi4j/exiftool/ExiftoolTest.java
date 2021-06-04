package de.zaunkoenigweg.lexi4j.exiftool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ExiftoolTest {

    private Exiftool exiftool;
    
    private File someEmptyFolder;
    
    @Before
    public void setUp() throws IOException {
        someEmptyFolder = Files.createTempDirectory("someEmptyFolder").toFile();
        someEmptyFolder.deleteOnExit();
        exiftool = new Exiftool();
    }
    
    @Test
    public void testReadOneFileNull() {
        Optional<ExifData> exifData = exiftool.read(null);
        assertNotNull(exifData);
        assertFalse(exifData.isPresent());
    }

    @Test
    public void testReadOneFileThatDoesNotExist() {
        Optional<ExifData> exifData = exiftool.read(new File(someEmptyFolder, "noimage.jpg"));
        assertNotNull(exifData);
        assertFalse(exifData.isPresent());
    }

    @Test
    public void testReadOneFileThatIsNoImage() {
        Optional<ExifData> exifData = exiftool.read(new File(getClass().getResource("/exiftool/noimage").getPath()));
        assertNotNull(exifData);
        assertTrue(exifData.isPresent());
        assertTrue(exifData.get().getCameraMake().isEmpty());
        assertTrue(exifData.get().getCameraModel().isEmpty());
        assertTrue(exifData.get().getDateTimeOriginal().isEmpty());
        assertTrue(exifData.get().getImageDescription().isEmpty());
        assertTrue(exifData.get().getSubsecTimeOriginal().isEmpty());
        assertTrue(exifData.get().getUserComment().isEmpty());
    }

    @Test
    public void testReadOneFile() {
        File iPhone5sFile = new File(getClass().getResource("/exiftool/NikonD70.jpg").getPath());
        
        Optional<ExifData> exifData = exiftool.read(iPhone5sFile);
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
        exiftool.fillCache(someEmptyFolder.getAbsolutePath());
    }

    @Test
    public void testReadAllFiles() {
        File exampleFolder = new File(getClass().getResource("/exiftool/").getPath());
        File iPhone5sFile = new File(getClass().getResource("/exiftool/iPhone5s.jpg").getPath());
        File nikonD70File = new File(getClass().getResource("/exiftool/NikonD70.jpg").getPath());
        
        exiftool.fillCache(exampleFolder.getAbsolutePath());
        
        Optional<ExifData> exifData = this.exiftool.read(iPhone5sFile);
        assertTrue(exifData.isPresent());
        assertTrue(exifData.get().getCameraMake().isPresent());
        assertEquals("Apple", exifData.get().getCameraMake().get());
        assertTrue(exifData.get().getCameraModel().isPresent());
        assertEquals("iPhone 5s", exifData.get().getCameraModel().get());
        
        exifData = this.exiftool.read(nikonD70File);
        assertTrue(exifData.get().getCameraMake().isPresent());
        assertEquals("NIKON CORPORATION", exifData.get().getCameraMake().get());
        assertTrue(exifData.get().getCameraModel().isPresent());
        assertEquals("NIKON D70", exifData.get().getCameraModel().get());
    }

    @Test
    public void testUpdateFile() throws IOException {
        File fileSource = new File(getClass().getResource("/exiftool/NikonD70.jpg").getFile());
        File file = new File(someEmptyFolder, "ImageWithDescription.jpg");
        Files.copy(fileSource.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        String imageDescription = "new-image-description";
        LocalDateTime dateTimeOriginal = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Integer subsecTimeOriginal = 13;
        String cameraModel = "new-camera-model";
        String cameraMake = "new-camera-make";
        String userComment = "new-user-comment";
        exiftool.update(file)
            .withImageDescription(imageDescription)
            .withDateTimeOriginal(dateTimeOriginal)
            .withSubsecTimeOriginal(subsecTimeOriginal)
            .withCameraMake(cameraMake)
            .withCameraModel(cameraModel)
            .withUserComment(userComment)
            .perform();

        Optional<ExifData> exifData = exiftool.read(file);
        assertTrue(exifData.isPresent());
        assertEquals(imageDescription, exifData.get().getImageDescription().get());
        assertEquals(dateTimeOriginal, exifData.get().getDateTimeOriginal().get());
        assertEquals(subsecTimeOriginal, exifData.get().getSubsecTimeOriginal().get());
        assertEquals(cameraMake, exifData.get().getCameraMake().get());
        assertEquals(cameraModel, exifData.get().getCameraModel().get());
        assertEquals(userComment, exifData.get().getUserComment().get());
    }

}
