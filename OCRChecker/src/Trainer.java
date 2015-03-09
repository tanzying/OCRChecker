import java.util.ArrayList;


public class Trainer {
	
	protected String name;
	protected ArrayList<Pokemon> pokemonarr;

	public Trainer(String trainername, ArrayList<Pokemon> trainerspokemon){
		name = trainername;
		trainerspokemon = pokemonarr;
	}
	
	public Trainer(String trainername){
		name = trainername;
		pokemonarr = new ArrayList<Pokemon>();
	}
	
	// Printing methods -------------------------------------------------------------------------------------------------------------------------
	
	public String printTeamToString(){
		String outstring = name + "\t";
		for (Pokemon p : pokemonarr){
			outstring += p.getSpecies() + "\t";
		}
		return outstring;
	}
	
	public String printFullTeamDetailsToString(){
		String outstring = new String();
		for (Pokemon p : pokemonarr){
			outstring += name + p.printAttributesToString() + "\n";
		}
		return outstring;
	}
	
	// Getters and setters ----------------------------------------------------------------------------------------------------------------------
	
	public void addPokemon(Pokemon p){
		pokemonarr.add(p);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
