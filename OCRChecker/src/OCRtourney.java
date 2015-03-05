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

public class OCRtourney {
	

public static void main(String[] args) throws IOException, JSONException {
	
    String KP = new String();
	
	String filepath = new String("data\\tournament\\");
	String outfilepath = new String("data\\tournament2\\");
	
	Tesseract instance = Tesseract.getInstance();
	
	File dir = new File(filepath);
	File[] directoryListing = dir.listFiles();
	
	for (File subdir : directoryListing){
	if (subdir.isDirectory()) {
		
        // Get player name and POP ID
        String[] nameID = subdir.getName().split("_");
		KP += nameID[0] + "\t";
		
		// Loop through every file in the directory
		File[] imagefilearr = subdir.listFiles();
		BufferedImage[] bimgarr = new BufferedImage[imagefilearr.length];
		
        if (imagefilearr != null) {
		for (int i = 0; i < imagefilearr.length; i++) {
   
	        BufferedImage bimg = ImageIO.read(imagefilearr[i]);
	        bimgarr[i] = bimg;
	        String info = OCR.extractInfo(bimg, instance);
	        System.out.println(OCR.arrToTabbedString(nameID) + info);
	           	           	        
		}	   
    	} // For loop for screenshot file ends
        
        KP += "\n";
        
        BufferedImage teamimg = ImageManip.stitchImages(bimgarr,2);
        ImageIO.write(teamimg, "png", new File(outfilepath + subdir.getName() + ".png"));

    }
	} // For loop for subdir ends
	 
	   System.out.println(KP);
} // main ends
	
}
