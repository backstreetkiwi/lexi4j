package de.zaunkoenigweg.lexi4j.thumbnails;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThumbnailGeneratorTest {

    private File baseFolder = new File(getClass().getResource("/thumbnails/").getPath());

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateThumbnailFromVideoSrcDoesNotExist() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "not_existing.mov");
        File target = new File(baseFolder, "not_existing.jpg");
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateThumbnailFromVideoTargetDoesExist() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "mov.mov");
        File target = new File(baseFolder, "existing_target.jpg");
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
    }

    @Test
    public void testGenerateThumbnailForMov() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "mov.mov");
        File target = new File(baseFolder, "mov.jpg");
        assertFalse(target.exists());
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
        assertTrue(target.exists());
    }

    @Test
    public void testGenerateThumbnailForMp4() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "mp4.mp4");
        File target = new File(baseFolder, "mp4.jpg");
        assertFalse(target.exists());
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
        assertTrue(target.exists());
    }

    @Test
    public void testGenerateThumbnailForAvi() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "avi.avi");
        File target = new File(baseFolder, "avi.jpg");
        assertFalse(target.exists());
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
        assertTrue(target.exists());
    }

    @Test
    public void testGenerateThumbnailForMpeg() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "mpg.mpg");
        File target = new File(baseFolder, "mpg.jpg");
        assertFalse(target.exists());
        ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
        assertTrue(target.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateThumbnailFromImageSrcDoesNotExist() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "not_existing.jpg");
        File target = new File(baseFolder, "thumbnail.jpg");
        ThumbnailGenerator.generateThumbnailFromImage(source, target, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateThumbnailFromImageTargetDoesExist() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "NikonD70.jpg");
        File target = new File(baseFolder, "existing_target.jpg");
        ThumbnailGenerator.generateThumbnailFromImage(source, target, 300);
    }

    @Test
    public void testGenerateThumbnailForImage() throws ThumbnailGeneratorException {
        File source = new File(baseFolder, "NikonD70.jpg");
        File target = new File(baseFolder, "thumbnail.jpg");
        assertFalse(target.exists());
        ThumbnailGenerator.generateThumbnailFromImage(source, target, 300);
        assertTrue(target.exists());
    }
}
