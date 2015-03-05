import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.sourceforge.tess4j.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.JSONException;

public class OCR {
	
	public static String arrToTabbedString(Object[] objarr){
		String outstring = new String();
		for (Object obj : objarr){
			outstring += obj.toString() + "\t";
		}
		return outstring;
	}

	public static Integer[] toObject(int[] intArray) {
		 
		Integer[] result = new Integer[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			result[i] = Integer.valueOf(intArray[i]);
		}
		return result;
	}

	public static int sumOfIntArr(int[] intArray){
		int outint = 0;
		for (int i : intArray){
			outint += i;
		}
		return outint;
	}

	public static int randInt(int min, int max) {

	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	public static void drawRects(BufferedImage bimg, Rectangle[] rectarr, String filename) throws IOException{
	    // Debug code to check whether rectangles are drawn in the correct locations	        
	    Graphics2D graph = bimg.createGraphics();
	    for (Rectangle rect : rectarr){
	        graph.setColor(new Color(randInt(0,255),randInt(0,255),randInt(0,255)));
	        graph.draw(rect);
	    }
	    graph.dispose();
	    ImageIO.write(bimg, "png", new File(filename));
	}

	public static String extractInfo(BufferedImage bimg, Tesseract instance) throws JSONException, IOException{
		
		 // Get the dimensions of the image file
	    double width = bimg.getWidth();
	    double height = bimg.getHeight();
	    
	    // Draw rectangles to demarcate the area of the image in which the moves are displayed
	    // Images will vary in size so only relative positions should be used, not absolute ones!
	    // 0: species, 1: level, 2: HP, 3: Atk, 4: Def, 5: SAtk, 6: SDef, 7: Spe, 8: nature, 9: ability, 10: held item
	    // 11-14: moves 1-4 
	    Rectangle[] rectarr = new Rectangle[15];
	    rectarr[0] = new Rectangle((int) (width * 0.36), (int) (height * 2.1 / 12), (int) (width * 0.22), (int) (height * 0.9 / 12)); //XY
        rectarr[1] = new Rectangle((int) (width * 0.455), (int) (height * 1.1 / 24), (int) (width * 0.1), (int) (height * 0.9 / 12)); //XY
	    //rectarr[0] = new Rectangle((int) (width * 0.31), (int) (height * 1.1 / 12), (int) (width * 0.22), (int) (height * 0.9 / 12)); //ORAS
	    //rectarr[1] = new Rectangle((int) (width * 0.46), (int) (height * 0.1 / 24), (int) (width * 0.09), (int) (height * 0.9 / 12)); //ORAS
	    rectarr[2] = new Rectangle((int) (width * 0.32), (int) (height * 3.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[3] = new Rectangle((int) (width * 0.276), (int) (height * 4.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[4] = new Rectangle((int) (width * 0.276), (int) (height * 5.1 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[5] = new Rectangle((int) (width * 0.276), (int) (height * 6 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[6] = new Rectangle((int) (width * 0.276), (int) (height * 7 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[7] = new Rectangle((int) (width * 0.276), (int) (height * 8 / 12), (int) (width * 0.08), (int) (height * 0.9 / 12));
	    rectarr[8] = new Rectangle((int) (width * 0.21), (int) (height * 9 / 12), (int) (width * 0.31), (int) (height / 12));
	    rectarr[9] = new Rectangle((int) (width * 0.21), (int) (height * 10 / 12), (int) (width * 0.31), (int) (height / 12));
	    rectarr[10] = new Rectangle((int) (width * 0.21), (int) (height * 11 / 12), (int) (width * 0.31), (int) (height / 12));
	    rectarr[11] = new Rectangle((int) (width * 0.59), (int) (height * 8 / 12), (int) (width * 0.35), (int) (height / 12));
	    rectarr[12] = new Rectangle((int) (width * 0.59), (int) (height * 9 / 12), (int) (width * 0.35), (int) (height / 12));
	    rectarr[13] = new Rectangle((int) (width * 0.59), (int) (height * 10 / 12), (int) (width * 0.35), (int) (height / 12));
	    rectarr[14] = new Rectangle((int) (width * 0.59), (int) (height * 11 / 12), (int) (width * 0.35), (int) (height / 12));


	    
	    bimg = ImageManip.greyscaleImage(bimg);
	    //bimg = ImageManip.binarizeImage(bimg);

	    //drawRects(bimg,rectarr,"data\\tournament2\\debug.png");
	    
	    try {
	    	
	    	// Restrict OCR to only alphabet characters, -, ' and 2 (for Porygon2 lol) for the following attributes
	    	instance.setTessVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'2");
	    	
	    	// Run OCR on the string. Replaceall clears any whitespace not between words
	    	String species = instance.doOCR(bimg, rectarr[0]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        String nature = instance.doOCR(bimg, rectarr[8]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        String ability = instance.doOCR(bimg, rectarr[9]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        String item = instance.doOCR(bimg, rectarr[10]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        
	    	String[] movesarr = new String[4];
	    	movesarr[0] = instance.doOCR(bimg, rectarr[11]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	movesarr[1] = instance.doOCR(bimg, rectarr[12]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	movesarr[2] = instance.doOCR(bimg, rectarr[13]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	movesarr[3] = instance.doOCR(bimg, rectarr[14]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	        
	        // Restrict OCR to only numerical digits and "Lv." for the following attributes
	    	instance.setTessVariable("tessedit_char_whitelist", "0123456789.");
	        
	    	String levelstr = instance.doOCR(bimg, rectarr[1]).replaceAll("\\.|\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	
	    	String[] statstrarr = new String[6];
	    	
	         // Restrict OCR to only numerical digits and / for the following attributes
	        instance.setTessVariable("tessedit_char_whitelist", "0123456789");
	        	
	        statstrarr[0] = instance.doOCR(bimg, rectarr[2]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	
	    	// Restrict OCR to only numerical digits for the following attributes
	    	instance.setTessVariable("tessedit_char_whitelist", "0123456789");
	    	
	    	statstrarr[1] = instance.doOCR(bimg, rectarr[3]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	statstrarr[2] = instance.doOCR(bimg, rectarr[4]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	statstrarr[3] = instance.doOCR(bimg, rectarr[5]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	statstrarr[4] = instance.doOCR(bimg, rectarr[6]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	statstrarr[5] = instance.doOCR(bimg, rectarr[7]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    	
	        // Find the nearest matches in the list of names for these attributes
	    	species = MatchFinder.getClosetItem(species, "species");
	    	nature = MatchFinder.getClosetItem(nature, "nature");
	    	ability = MatchFinder.getClosetItem(ability, "ability");
	    	item = MatchFinder.getClosetItem(item, "item");      	
	    	for (int i = 0; i < movesarr.length; i++){
	    		movesarr[i] = MatchFinder.getClosetItem(movesarr[i], "move");
	    	}
	    	
	    	// Convert the number based items to ints, assign values of -1 to denote an error if the OCR failed
	        int[] statarr = new int[6];
	        for (int i = 0; i <= 5; i++){
	            try {
		            statarr[i] = Integer.parseInt(statstrarr[i]);
	            } catch (NumberFormatException e){
	            	statarr[i] = -1;
	            }
	        }
	        
	        int level = 0;
	        try {
	            level = Integer.parseInt(levelstr);
	        } catch (NumberFormatException e){
	        	level = -1;
	        }

	        // Calculate minimum EVs using the given information	            	            
	        StatCalc calcer = new StatCalc();
	        
	        int[] minEVsarr = calcer.calculatePokemonsMinEVs(species, nature, level, statarr);

	        // Return attributes
	        return //arrToTabbedString(nameID) +
	        					species + "\t" + 
	        					level + "\t" + 
	        					arrToTabbedString(statstrarr) +
	        					arrToTabbedString(toObject(minEVsarr)) +
	        					sumOfIntArr(minEVsarr) + "\t" + 
	        					arrToTabbedString(movesarr) +
	        					nature + "\t" + 
	        					ability + "\t" + 
	        					item;
	        
	        //KP += species + "\t";
	        

	    } catch (TesseractException e) {
	        return "Error - Extraction failed";
	    }
		
		
	}

public static void main(String[] args) throws IOException, JSONException {
	
    /*StatCalc calcer = new StatCalc();
    
    int[] minEVsarr = calcer.calculatePokemonsMinEVs("Kyogre", "Naughty", 82, new int[]{307,223,214,297,242,180});
    for(int number: minEVsarr){
    	   System.out.println(number);
    	 }*/
	
	String filepath = new String("data\\CC_raw\\");

	Tesseract instance = Tesseract.getInstance();
	
	File dir = new File(filepath);
	File[] directoryListing = dir.listFiles();
	
	if (directoryListing != null) {
	for (File imageFile : directoryListing) {

	    	//File imageFile = new File(filepath + filename + fileext);	        
	        BufferedImage bimg = ImageIO.read(imageFile);
	        String info = OCR.extractInfo(bimg, instance);
	        System.out.println(info);
	          
	}	    
	}
	   
}
	
}
