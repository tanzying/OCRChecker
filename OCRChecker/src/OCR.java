import java.io.File;
import java.util.Arrays;
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

	public static Pokemon extractInfo(BufferedImage bimg, Tesseract instance) throws TesseractException, IOException{
		
		 // Get the dimensions of the image file
	    double width = bimg.getWidth();
	    double height = bimg.getHeight();
	    
	    // Draw rectangles to demarcate the area of the image in which the moves are displayed
	    // Images will vary in size so only relative positions should be used, not absolute ones!
	    // 0: species, 1: level, 2: HP, 3: Atk, 4: Def, 5: SAtk, 6: SDef, 7: Spe, 8: nature, 9: ability, 10: held item
	    // 11-14: moves 1-4 
	    Rectangle[] rectarr = new Rectangle[15];
	    //rectarr[0] = new Rectangle((int) (width * 0.36), (int) (height * 2.1 / 12), (int) (width * 0.22), (int) (height * 0.9 / 12)); //XY
        //rectarr[1] = new Rectangle((int) (width * 0.455), (int) (height * 1.1 / 24), (int) (width * 0.1), (int) (height * 0.9 / 12)); //XY
	    rectarr[0] = new Rectangle((int) (width * 0.31), (int) (height * 1.1 / 12), (int) (width * 0.22), (int) (height * 0.9 / 12)); //ORAS
	    rectarr[1] = new Rectangle((int) (width * 0.46), (int) (height * 0.1 / 24), (int) (width * 0.09), (int) (height * 0.9 / 12)); //ORAS
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
	    	    	
	    	String[] attribsarr = new String[15];
	    	for (int i = 0; i <=14; i++){
	    		switch (i){
		    		// 0: species, 8: nature, 9: ability, 10: held item, 11-14: moves 1-4
	    			// All strings, so restrict OCR to recognise only letters and allowed punctuation (and 2 for Porygon2)
		    		case 0: case 8: case 9: case 10: case 11: case 12: case 13: case 14: 
		    			instance.setTessVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'2");
		    			break;
		    		// 1: level, 2: HP, 3: Atk, 4: Def, 5: SAtk, 6: SDef, 7: Spe
		    		// All numbers, so restrict OCR to recognise only numbers
		    		case 2: case 3: case 4: case 5: case 6: case 7: 
		    			instance.setTessVariable("tessedit_char_whitelist", "0123456789");
		    			break;
		    		case 1: 
		    			instance.setTessVariable("tessedit_char_whitelist", "0123456789.");
		    			break;
	    		}
	    		attribsarr[i] = instance.doOCR(bimg, rectarr[i]).replaceAll("\\n|\\t|\\s(?=\\s)|^\\s", "");
	    		if (i==1) attribsarr[i] = attribsarr[i].replaceAll("\\.", "");
	    	}
	    	return new Pokemon(attribsarr);       
		
	}

public static void main(String[] args) throws TesseractException, IOException {
	
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
	        Pokemon pokemon = OCR.extractInfo(bimg, instance);
	        pokemon.printAttributes();
	          
	}	    
	}
	   
}
	
}
