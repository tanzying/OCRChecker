import java.util.ArrayList;


public class Participant extends Trainer {
	
	private String participantID;

	public Participant(String trainername, String id, ArrayList<Pokemon> trainerspokemon) {
		super(trainername, trainerspokemon);
		participantID = id;				
	}

	public Participant(String trainername, String id) {
		super(trainername);	
		participantID = id;
	}
	
	// Printing methods -------------------------------------------------------------------------------------------------------------------------
	
	public String printTeamToString(){
		String outstring = name + "\t" + participantID + "\t";
		for (Pokemon p : pokemonarr){
			outstring += p.getSpecies() + "\t";
		}
		return outstring;
	}
	
	public String printFullTeamDetailsToString(){
		String outstring = new String();
		for (Pokemon p : pokemonarr){
			outstring += name + "\t" + participantID + "\t" + p.printAttributesToString() + "\n";
		}
		return outstring;
	}
	
	// Getters and setters ----------------------------------------------------------------------------------------------------------------------

	public String getParticipantID() {
		return participantID;
	}

	public void setParticipantID(String participantID) {
		this.participantID = participantID;
	}

}
