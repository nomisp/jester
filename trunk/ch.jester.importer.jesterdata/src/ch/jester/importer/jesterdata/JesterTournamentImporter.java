package ch.jester.importer.jesterdata;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Club;
import ch.jester.model.Player;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;

@SuppressWarnings("rawtypes")
public class JesterTournamentImporter implements IImportHandler {
	private ServiceUtility mServices = new ServiceUtility();
	public JesterTournamentImporter() {
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) throws ProcessingException {
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();

		try {
		serializer.prepareContext(ModelFactory.getInstance().getAllExportableClasses());
		SerializationReader reader = 	serializer.createReader(pInputStream);
		List<IEntityObject> list = reader.read();

		for(IEntityObject o:list){
			Class<IEntityObject> clz = (Class<IEntityObject>) o.getClass();
			if(o instanceof Tournament){
				mergeTournamentData((Tournament) o);
			}
			IDaoService<IEntityObject> service = mServices.getDaoServiceByEntity(clz);
			service.save(o);
			service.close();
		}
		
		//EventQueue für Spieler manuell notifizieren.
		mServices.getService(IPersistencyEventQueue.class).dispatch(new IPersistencyEvent(){

			@Override
			public Class<?> getLoadClass() {
				return Player.class;
			}

			@Override
			public Operation getCRUD() {
				return Operation.SAVED;
			}

			@Override
			public Object getSource() {
				return null;
			}

			@Override
			public Collection<?> getLoad() {
				return null;
			}
			
		});
		
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
		mServices.closeAllTrackers();
		return null;
	}

	private void mergeTournamentData(Tournament o) {
		merge(o);
		
	}
	private boolean doMergeExisting(Class<? extends IEntityObject> clz){
		int existing =  mServices.getDaoServiceByEntity(clz).count();
		if(existing==0){
			return false;
		}
		return true;
	}

	
	private void merge(Tournament o) {
		boolean doMergePlayers = doMergeExisting(Player.class);
		boolean doMergeClubs = doMergeExisting(Club.class);
		
		if(doMergeClubs==false && doMergePlayers==false){
			return;
		}
		if(doMergeClubs){
			mergeClubs(o.getPlayers());
		}
		
	}

	private void mergeClubs(List<Player> players) {
		IDaoService<Club> clubService = mServices.getDaoServiceByEntity(Club.class);
		List<Club> existingClubs = clubService.getAll();
		for(Club existing:existingClubs){
			for(Player p:players){
				if(p.getClubs().contains(existing)){
					//Korrektur der Objektinstanzen (equals ist überschrieben)
					//Damit es nicht auf der DB knallt, da Club name unique;
					p.removeClub(existing);
					p.addClub(existing);
					
				}
			}
		}
		
		
	}

}
