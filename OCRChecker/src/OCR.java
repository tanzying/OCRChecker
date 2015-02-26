import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class OCR {

public static void main(String[] args) throws IOException {
	
	String filepath = new String("C:\\Users\\Tan Zong Ying\\Desktop\\OCRChecker\\CC_raw\\");
    String filepathbinarized = new String("C:\\Users\\Tan Zong Ying\\Desktop\\OCRChecker\\binarized\\");
    String filepathgreyscale = new String("C:\\Users\\Tan Zong Ying\\Desktop\\OCRChecker\\greyscale\\");
	//String filename = new String("Image 026");
	//String fileext = new String(".png");
	
	Tesseract instance = Tesseract.getInstance();
	
	File dir = new File(filepath);
	File[] directoryListing = dir.listFiles();
	
	 if (directoryListing != null) {
		// Loop through every file in the directory
		for (File imageFile : directoryListing) {

	    	//File imageFile = new File(filepath + filename + fileext);	        
	        BufferedImage bimg = ImageIO.read(imageFile);
	        
	        // Get the dimensions of the image file
	        double width = bimg.getWidth();
	        double height = bimg.getHeight();
	        
	        // Draw rectangles to demarcate the area of the image in which the moves are displayed
	        // Images will vary in size so only relative positions should be used, not absolute ones!
	        Rectangle speciesrect = new Rectangle((int) (width * 0.36), (int) (height * 2.1 / 12), (int) (width * 0.22), (int) (height * 0.9 / 12));
	        Rectangle levelrect = new Rectangle((int) (width * 0.455), (int) (height * 1.1 / 24), (int) (width * 0.1), (int) (height * 0.9 / 12));
	        Rectangle HPrect = new Rectangle((int) (width * 0.20), (int) (height * 3.1 / 12), (int) (width * 0.2), (int) (height * 0.9 / 12));
	        Rectangle atkrect = new Rectangle((int) (width * 0.276), (int) (height * 4.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	        Rectangle defrect = new Rectangle((int) (width * 0.276), (int) (height * 5.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	        Rectangle satkrect = new Rectangle((int) (width * 0.276), (int) (height * 6.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	        Rectangle sdefrect = new Rectangle((int) (width * 0.276), (int) (height * 7.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	        Rectangle sperect = new Rectangle((int) (width * 0.276), (int) (height * 8.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));	        
	        Rectangle move1rect = new Rectangle((int) (width * 0.59), (int) (height * 8 / 12), (int) (width * 0.35), (int) (height / 12));
	        Rectangle move2rect = new Rectangle((int) (width * 0.59), (int) (height * 9 / 12), (int) (width * 0.35), (int) (height / 12));
	        Rectangle move3rect = new Rectangle((int) (width * 0.59), (int) (height * 10 / 12), (int) (width * 0.35), (int) (height / 12));
	        Rectangle move4rect = new Rectangle((int) (width * 0.59), (int) (height * 11 / 12), (int) (width * 0.35), (int) (height / 12));
	        Rectangle naturerect = new Rectangle((int) (width * 0.21), (int) (height * 9 / 12), (int) (width * 0.31), (int) (height / 12));
	        Rectangle abilityrect = new Rectangle((int) (width * 0.21), (int) (height * 10 / 12), (int) (width * 0.31), (int) (height / 12));
	        Rectangle itemrect = new Rectangle((int) (width * 0.21), (int) (height * 11 / 12), (int) (width * 0.31), (int) (height / 12));
	
	        
	        bimg = OtsuBinarize.greyscaleImage(bimg);
	        //bimg = OtsuBinarize.binarizeImage(bimg);        
	        
	        try {
	        	
	        	// Restrict OCR to only alphabet characters, -, ' and 2 (for Porygon2 lol) for the following attributes
	        	instance.setTessVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'2");
	        	
	        	// Run OCR on moves
	        	String species = instance.doOCR(bimg, speciesrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String move1 = instance.doOCR(bimg, move1rect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String move2 = instance.doOCR(bimg, move2rect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String move3 = instance.doOCR(bimg, move3rect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String move4 = instance.doOCR(bimg, move4rect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String nature = instance.doOCR(bimg, naturerect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String ability = instance.doOCR(bimg, abilityrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            String item = instance.doOCR(bimg, itemrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	            
	            // Restrict OCR to only numerical digits and "Lv." for the following attributes
	        	instance.setTessVariable("tessedit_char_whitelist", "0123456789.");
	            
	        	String level = instance.doOCR(bimg, levelrect).replaceAll("\\.|\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	
		         // Restrict OCR to only numerical digits and / for the following attributes
		        instance.setTessVariable("tessedit_char_whitelist", "0123456789/");
		        	
	        	String HP = instance.doOCR(bimg, HPrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	
	        	// Restrict OCR to only numerical digits for the following attributes
	        	instance.setTessVariable("tessedit_char_whitelist", "0123456789");
	        	
	        	String atk = instance.doOCR(bimg, atkrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	String def = instance.doOCR(bimg, defrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	String satk = instance.doOCR(bimg, satkrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	String sdef = instance.doOCR(bimg, sdefrect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	String spe = instance.doOCR(bimg, sperect).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        	
	            // Find the nearest matches in the list of names for these attributes
	        	species = MatchFinder.getClosetItem(species, "species");
	        	nature = MatchFinder.getClosetItem(nature, "nature");
	        	ability = MatchFinder.getClosetItem(ability, "ability");
	        	item = MatchFinder.getClosetItem(item, "item");
	            move1 = MatchFinder.getClosetItem(move1, "move");
	            move2 = MatchFinder.getClosetItem(move2, "move");
	            move3 = MatchFinder.getClosetItem(move3, "move");
	            move4 = MatchFinder.getClosetItem(move4, "move");
	            
	            // Return attributes
	            System.out.println(species + "\t" + 
	            					level + "\t" + 
	            					HP + "\t" + 
	            					atk + "\t" + 
	            					def + "\t" + 
	            					satk + "\t" + 
	            					sdef + "\t" + 
	            					spe + "\t" + 
	            					move1 + "\t" + 
	            					move2 + "\t" + 
	            					move3 + "\t" + 
	            					move4 + "\t" + 
	            					nature + "\t" + 
	            					ability + "\t" + 
	            					item);

	        } catch (TesseractException e) {
	            System.err.println(e.getMessage());
	        }   	
		    
	        // Debug code to check whether rectangles are drawn in the correct locations	        
	        Graphics2D graph = bimg.createGraphics();
	        graph.setColor(Color.BLUE);
	        graph.draw(speciesrect);
	        graph.draw(move1rect);
	        graph.draw(levelrect);
	        graph.draw(satkrect);
	        graph.setColor(Color.RED);
	        graph.draw(move2rect);
	        graph.draw(naturerect);
	        graph.draw(sperect);
	        graph.draw(atkrect);
	        graph.setColor(Color.YELLOW);
	        graph.draw(move3rect);
	        graph.draw(abilityrect);
	        graph.draw(HPrect);
	        graph.draw(sdefrect);
	        graph.setColor(Color.GREEN);
	        graph.draw(move4rect);
	        graph.draw(itemrect);
	        graph.draw(defrect);
	        graph.dispose();
	        
	        // Output greyscale/binarized images
	        ImageIO.write(bimg, "png", new File(filepathgreyscale + imageFile.getName()));
	        //ImageIO.write(bimg, "png", new File(filepathbinarized + filename + fileext));*/
	   
		}	    
	}
	   
}
	
}
