import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import net.sourceforge.tess4j.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.JSONException;

public class Tournament {
	
	public ArrayList<Participant> participantsarr;
	
	public Tournament(){
		participantsarr = new ArrayList<Participant>();
	}
	
	public void importParticipants(String inputdirectory) throws IOException, TesseractException{
		
		//String KP = new String();		
		
		Tesseract instance = Tesseract.getInstance();
		
		File dir = new File(inputdirectory);
		File[] directoryListing = dir.listFiles();
		
		for (File subdir : directoryListing){
		if (subdir.isDirectory()) {
			
	        // Get player name and POP ID
	        String[] nameID = subdir.getName().split("_");
			//KP += nameID[0] + "\t";
			Participant participant = new Participant(nameID[0], nameID[1]);
			
			// Loop through every file in the directory
			File[] imagefilearr = subdir.listFiles();
			BufferedImage[] bimgarr = new BufferedImage[imagefilearr.length];
			
	        if (imagefilearr != null) {
			for (int i = 0; i < imagefilearr.length; i++) {
	   
		        BufferedImage bimg = ImageIO.read(imagefilearr[i]);
		        bimgarr[i] = bimg;
		        Pokemon pokemon = OCR.extractInfo(bimg, instance);
		        //System.out.println(nameID[0] + "\t" + nameID[1] + "\t" + pokemon.printAttributesToString());
		        //KP += pokemon.getSpecies() + "\t";
		        participant.addPokemon(pokemon);
		           	           	        
			}	   
	    	} // For loop for screenshot file ends
	        
	        participantsarr.add(participant);
	        //KP += "\n";
	        
	        //BufferedImage teamimg = ImageManip.stitchImages(bimgarr,2);
	        //ImageIO.write(teamimg, "png", new File(outfilepath + subdir.getName() + ".png"));

	    }
		} // For loop for subdir ends
		 
		  //System.out.println(KP);
		System.out.println(printParticipantDetailsToString());
		System.out.println(printAllTeamsToString());
	}
	
	public String printParticipantDetailsToString(){
		String outstring = new String();
		for (Participant p : participantsarr){
			outstring += p.printFullTeamDetailsToString() + "\n";
		}
		return outstring;
	}
	
	public String printAllTeamsToString(){
		String outstring = new String();
		for (Participant p : participantsarr){
			outstring += p.printTeamToString() + "\n";
		}
		return outstring;
	}

	public static void main(String[] args) throws IOException, TesseractException {
		
		String filepath = new String("data\\tournamenttest\\");
		String outfilepath = new String("data\\tournament2\\");
		
		Tournament tourney = new Tournament();
		
		tourney.importParticipants(filepath);
		
		
	}
	
}
