import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.*;


public class CalcStats {
	
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
	
    public static void main(String [] args) {

    	JSONParser parser = new JSONParser();
    	try {
    		
    		Object obj = parser.parse(new FileReader("data\\pokedex.js"));
    		JSONObject jsonobj = (JSONObject) obj;
    		
    		String a = (String) jsonobj.get("bulbasaur");
    		System.out.println(a);
    		
    	
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

}
