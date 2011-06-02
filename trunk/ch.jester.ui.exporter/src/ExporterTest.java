import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.ZipSerializationReader;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationWriter;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;



public class ExporterTest {
	public static void main(String args[]) throws IOException, JAXBException{
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		serializer.prepareContext(Player.class);
		SerializationWriter writer = serializer.createWriter("abc.zip");
		writer.newEntry("players.xml");
		List<Player> plist = createPlayers();
		writer.write(plist);
		writer.close();
		
		ZipSerializationReader reader =serializer.createReader("abc.zip");
		//serializer.prepare(Player.class);
		List<Player> plist1 = reader.read("players.xml");
		reader.close();
		System.out.println(plist1);
	}
	private static List<Player> createPlayers() {
		List<Player> mP = new ArrayList<Player>();
		mP.add(ModelFactory.getInstance().createPlayer());
		mP.add(ModelFactory.getInstance().createPlayer());
		return mP;
	}
}
