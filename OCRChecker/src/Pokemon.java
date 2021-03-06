import java.io.IOException;
import org.json.JSONException;


public class Pokemon {

	private String species;
	private String nature;
	private String ability;
	private String helditem;
	private String type1;
	private String type2;
	private int level;
	private int[] actualstats = new int[6];
	private int[] minEVs = new int[6];
	private String[] moves = new String[4];
	public int KP;
	
	// Constructs a Pokemon object from an array of strings with the following indices/elements:
	// 0: species, 1: level, 2: HP, 3: Atk, 4: Def, 5: SAtk, 6: SDef, 7: Spe
	// 8: nature, 9: ability, 10: held item, 11-14: moves 1-4, 15: type 1, 16: type 2
	public Pokemon(String[] inputdata) throws IOException{
		
    	species = MatchFinder.getClosetItem(inputdata[0], "species");
    	nature = MatchFinder.getClosetItem(inputdata[8], "nature");
    	ability = MatchFinder.getClosetItem(inputdata[9], "ability");
    	helditem = MatchFinder.getClosetItem(inputdata[10], "item");
    	//type1 = inputdata[15];
    	//type2 = inputdata[16];
    	
    	// Moves
    	for (int i = 0; i <=3; i++){
    		moves[i] = MatchFinder.getClosetItem(inputdata[i + 11], "move");
    	}
    	// Level
    	level = statFromStringToInt(inputdata[1]);

    	// Actual stats
        for (int i = 0; i <= 5; i++){
            actualstats[i] = statFromStringToInt(inputdata[i + 2]);
        }
        
     // Disambiguate formes - currently only genies, Rotoms and Meowstics are supported
        if (species == "Landorus" || species == "Thundurus" || species == "Tornadus"){
        	if (ability == "Intimidate" || ability == "Volt Absorb" || ability == "Regenerator"){
            	species += "-therian";
            } else {
            	species += "-incarnate";
            }
        } else if (species == "Rotom"){
        	loop: for (int i = 0; i <= 3; i++){
	    		switch (moves[i]){
	    			case "Hydro Pump":
	    				species += "-wash";
	    				break loop;
	    			case "Overheat":
	    				species += "-heat";
	    				break loop;
	    			case "Blizzard":
	    				species += "-frost";
	    				break loop;
	    			case "Air Slash":
	    				species += "-fan";
	    				break loop;
	    			case "Leaf Storm":
	    				species += "-mow";
	    				break loop;
	    		}	    		
    		}
        } else if (species == "Meowstic"){
        	if (ability == "Competitive"){
            	species += "-female";
            } else {
            	species += "-male";
            }
        }
     // Calculate minimum EVs using the given level and stats. Assume maximum IVs
        try {
        	StatCalc calcer = new StatCalc();
        	minEVs = calcer.calculatePokemonsMinEVs(species, nature, level, actualstats);
        } catch (JSONException e){
        	 System.err.println("Caught Exception: " + e.getMessage());
        	 minEVs = new int[]{-3,-3,-3,-3,-3,-3};
        }
        
	}
	
	
	// Converts the numerical stats from strings to ints. Returns -1 if the OCR returned something invalid
	private int statFromStringToInt(String string){
        try {
        	return Integer.parseInt(string);
        } catch (NumberFormatException e){
        	return -1;
        }
	}
	
	// IO methods ----------------------------------------------------------------------------------------------------------------------
	
	public void printAttributes(){
		System.out.println(printAttributesToString());
	}
	
	public String printAttributesToString(){
		return	    species + "\t" + 
					level + "\t" + 
					arrToTabbedString(toObject(actualstats)) +
					arrToTabbedString(toObject(minEVs)) +
					sumOfIntArr(minEVs) + "\t" + 
					arrToTabbedString(moves) +
					nature + "\t" + 
					ability + "\t" + 
					helditem/* + "\t" + 
					type1 + "\t" + 
					type2*/
					;
	}
	
	private static String arrToTabbedString(Object[] objarr){
		String outstring = new String();
		for (Object obj : objarr){
			outstring += obj.toString() + "\t";
		}
		return outstring;
	}

	private static Integer[] toObject(int[] intArray) {
		 
		Integer[] result = new Integer[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			result[i] = Integer.valueOf(intArray[i]);
		}
		return result;
	}

	private static int sumOfIntArr(int[] intArray){
		int outint = 0;
		for (int i : intArray){
			outint += i;
		}
		return outint;
	}
	
	// Getters and setters --------------------------------------------------------------------------------------------------------
	public String getSpecies() {return species;}
	public String getNature() {return nature;}
	public String getAbility() {return ability;}
	public String getHelditem() {return helditem;}
	public int getLevel() {return level;}
	public int[] getActualstats() {return actualstats;}
	public int[] getMinEVs() {return minEVs;}
	public String[] getMoves() {return moves;}
	public void setSpecies(String species) {this.species = species;}
	public void setNature(String nature) {this.nature = nature;}
	public void setAbility(String ability) {this.ability = ability;}
	public void setHelditem(String helditem) {this.helditem = helditem;}
	public void setLevel(int level) {this.level = level;}
	public void setActualstats(int[] actualstats) {this.actualstats = actualstats;}
	public void setMinEVs(int[] minEVs) {this.minEVs = minEVs;}
	public void setMoves(String[] moves) {this.moves = moves;}
}
