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
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

@SuppressWarnings("rawtypes")
public class JesterTournamentImporter implements IImportHandler {

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
		ServiceUtility su = new ServiceUtility();
		try {
		serializer.prepareContext(ModelFactory.getInstance().getAllExportableClasses());
		SerializationReader reader = 	serializer.createReader(pInputStream);
		List<IEntityObject> list = reader.read();

		for(IEntityObject o:list){
			Class<IEntityObject> clz = (Class<IEntityObject>) o.getClass();
			IDaoService<IEntityObject> service = su.getDaoServiceByEntity(clz);
			service.save(o);
			service.close();
		}
		
		//EventQueue manuell notifizieren.
		su.getService(IPersistencyEventQueue.class).dispatch(new IPersistencyEvent(){

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
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
		su.closeAllTrackers();
		return null;
	}

}
