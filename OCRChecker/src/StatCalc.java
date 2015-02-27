import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.file.Paths.get;
import static java.nio.file.Files.readAllBytes;

import org.json.*;


public class StatCalc {
	
	public JSONObject pokedex;
	
	public StatCalc() throws JSONException, IOException{
		pokedex = new JSONObject(new String(readAllBytes(get("src\\data\\pokedex.js"))));
	}
	
	public static int calculateStat(int base, int ivs, int evs, int level, double nature, boolean ishp){
		if (ishp){
			return (int)((base * 2 + ivs + (int)(evs / 4)) * level / 100) + level + 10;
		} else {		
			return (int)(((int)((base * 2 + ivs + (int)(evs / 4)) * level / 100) + 5) * nature);
		}
	}
	
	public static int calculateEVs(int stat, int base, int ivs, int level, double nature, boolean ishp){
		int evs;
		for (evs = 0; evs <= 252; evs += 4){
			int statforcurrentevs = calculateStat(base, ivs, evs, level, nature, ishp);
			if (stat == statforcurrentevs){
				return evs;
			} else if (stat < statforcurrentevs && evs == 0){
				return -2; // impossible stat, too low
			}
		}
		return -1; // impossible stat, too high or due to nature modifier
	}
	
	// Finds the lowest required EVs for a given stat value, base stat, level and nature, and unknown IVs
	public static int findLowestEVs(int stat, int base, int level, double nature, boolean ishp){
		
		// First assume an IV of 31 and calculate the required EVs
		int lowestEV = calculateEVs(stat, base, 31, level, nature, ishp);
		
		
		switch (lowestEV){
		
			// If the stat is possible with 31 IVs, then assume so since that will result in the minimum EVs required
			default:
				return lowestEV;
		
			// If the stat is too low for 31 IVs to be possible, calculate the stat that ivs = evs = 0 gives
			case -2: 
				int lowestpossiblestat = calculateStat(base, 0, 0, level, nature, ishp);
				// If the stat is still lower than that then it is an impossible case
				if (stat < lowestpossiblestat) {
					return -2; // too low
				} 
				// If the stat is higher than that then assume imperfect IV and 0 evs
				else {
					return 0; // 0 evs
				}
				
			// If the stat is too high even with 31 IVs or is impossible thanks to boosting natures, then it is an impossible case
			case -1:
				return -1; // too high/impossible due to boosting nature
		}
	}
	
	public int[] calculatePokemonsMinEVs(String species, String nature, int level, int[] statarr){
		
		int[] minEVsarr = new int[6];
		for (int stat : statarr){
			

		}
		return minEVsarr;
	}
	
    public static void main(String [] args) throws IOException, JSONException {

    	JSONObject obj = new JSONObject(new String(readAllBytes(get("src\\data\\pokedex.js"))));
    	String a = obj.getJSONObject("bulbasaur").getJSONArray("types").getString(1);
    	System.out.println(a);
    }

}
