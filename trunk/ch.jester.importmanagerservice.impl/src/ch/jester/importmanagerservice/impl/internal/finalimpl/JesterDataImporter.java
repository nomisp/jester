package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;

public class JesterDataImporter implements IImportHandler {

	public JesterDataImporter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object handleImport(InputStream pInputStream,
			IProgressMonitor pMonitor) {
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		ServiceUtility su = new ServiceUtility();
		try {
		serializer.prepareContext(ModelFactory.getInstance().getAllExportableClasses());
		SerializationReader reader = 	serializer.createReader(pInputStream);
		List<IEntityObject> list = reader.read();

		for(IEntityObject o:list){
			Class<IEntityObject> clz = (Class<IEntityObject>) o.getClass();
			IDaoService<IEntityObject> service = su.getDaoService(clz);
			service.save(o);
			service.close();
			
			
		}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
