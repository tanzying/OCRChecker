import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXML {

	public WriteXML() {
	}
	
	public void writeTournament(Tournament tournament){
		
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// Tourney node
			Document doc = docBuilder.newDocument();
			Element tourney = doc.createElement("tournament");
			tourney.setAttribute("name", tournament.getName());
			tourney.setAttribute("date", tournament.getDate());
			doc.appendChild(tourney);
			
			// Participant nodes
			Element participants = doc.createElement("participants");
			tourney.appendChild(participants);
			
			for (Participant p : tournament.getParticipantsarr()){
				
				Element participant = doc.createElement("participant");
				participant.setAttribute("name", p.getName());
				participant.setAttribute("id", p.getParticipantID());
				participants.appendChild(participant);
				
				// Pokemon nodes				
				for (Pokemon poke : p.getPokemonArr()){
				
					Element pokemon = doc.createElement("pokemon");
					participant.appendChild(pokemon);
					
					// Pokemon attribute nodes
					/*
						private String species;
						private String nature;
						private String ability;
						private String helditem;
						private String[] moves = new String[4];
					 */
					Element species = doc.createElement("species");
					species.appendChild(doc.createTextNode(poke.getSpecies()));
					pokemon.appendChild(species);
					
					Element nature = doc.createElement("nature");
					nature.appendChild(doc.createTextNode(poke.getNature()));
					pokemon.appendChild(nature);
					
					Element ability = doc.createElement("ability");
					ability.appendChild(doc.createTextNode(poke.getAbility()));
					pokemon.appendChild(ability);
					
					Element helditem = doc.createElement("helditem");
					helditem.appendChild(doc.createTextNode(poke.getHelditem()));
					pokemon.appendChild(helditem);
					
					String[] moves = poke.getMoves();
					for (int i = 0; i < moves.length; i++){
						Element move = doc.createElement("move" + Integer.toString(i+1));
						move.appendChild(doc.createTextNode(moves[i]));
						pokemon.appendChild(move);
					}
					
				}
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File( "data\\" + tournament.getName() + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
