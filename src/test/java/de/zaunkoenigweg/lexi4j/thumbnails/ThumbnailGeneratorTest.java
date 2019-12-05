package de.zaunkoenigweg.lexi4j.thumbnails;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThumbnailGeneratorTest {

	private File baseFolder = new File(getClass().getResource("/thumbnails/").getPath());
	
    @Test(expected=IllegalArgumentException.class)
    public void testGenerateThumbnailSrcDoesNotExist() throws ThumbnailGeneratorException {
    	File source = new File(baseFolder, "not_existing.mov"); 
    	File target = new File(baseFolder, "not_existing.jpg"); 
    	ThumbnailGenerator.generateThumbnailFromVideo(source, target, 300);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGenerateThumbnailTargetDoesExist() throws ThumbnailGeneratorException {
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

}
