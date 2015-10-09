import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Comparator;
import java.util.LinkedHashMap;

import net.sourceforge.tess4j.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.JSONException;

public class Tournament {
	
	public ArrayList<Participant> participantsarr;
	public Map<String,Integer> KPMap;
	public String name;
	public String date;
	
	public Tournament(String name, String date){
		participantsarr = new ArrayList<Participant>();
		this.name = name;
		this.date = date;
	}
	
	public void importParticipantsFromScreenshots(String inputdirectory, String outfilepath) throws IOException, TesseractException{	
		
		Tesseract instance = Tesseract.getInstance();
		
		File dir = new File(inputdirectory);
		File[] directoryListing = dir.listFiles();
		
		for (File subdir : directoryListing){
		if (subdir.isDirectory()) {
			
	        // Get player name and POP ID
	        String[] nameID = subdir.getName().split("_");
			Participant participant = new Participant(nameID[0], nameID[1]);
			System.out.println("Added new participant: " + nameID[0] + ", ID: " + nameID[1]);
			
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
	        
	        BufferedImage teamimg = ImageManip.stitchImages(bimgarr,2,nameID[0] + ", ID: " + nameID[1]);
	        ImageIO.write(teamimg, "png", new File(outfilepath + subdir.getName() + ".png"));

	    }
		} // For loop for subdir ends
		 
		System.out.println(printParticipantDetailsToString());
		System.out.println(printAllTeamsToString());
	}
	
	public void calculateKP(){
		
		KPMap = new HashMap<String,Integer>();
		
		for (Participant participant : participantsarr){
        	
			ArrayList<Pokemon> pokemonarr = participant.getPokemonArr();
			
			for (Pokemon pokemon : pokemonarr){
				
	        	if (KPMap.containsKey(pokemon.getSpecies())){
	        		KPMap.put(pokemon.getSpecies(),KPMap.get(pokemon.getSpecies()) + 1);
	        	} else {
	        		KPMap.put(pokemon.getSpecies(),1);
	        	}
	        	
			}
        }	
		KPMap = sortByComparator(KPMap, false);
	}
	
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order){

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>(){
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2){
                if (order){
                    return o1.getValue().compareTo(o2.getValue());
                }
                else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    
    public static void printMap(Map<String, Integer> map)
    {
        for (Entry<String, Integer> entry : map.entrySet())
        {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
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
// ---------------------------------------------------------------------------------------------------------------------------------------
	public ArrayList<Participant> getParticipantsarr() {return participantsarr;}
	public String getName() {return name;}
	public String getDate() {return date;}
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) throws IOException, TesseractException {
		
		String filepath = new String("data\\tournament\\");
		String outfilepath = new String("data\\teams\\");
		String name = "Sceptile series PC 2";
		String date = "27-09-15";
		
		Tournament tourney = new Tournament(name, date);
		
		tourney.importParticipantsFromScreenshots(filepath,outfilepath);
		
		//tourney.calculateKP();
		//printMap(tourney.KPMap);	
		
		WriteXML write = new WriteXML();
		write.writeTournament(tourney);
		
	}

	
}
