package ch.jester.importer.jesterdata;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationReader;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.factories.ModelFactory;

@SuppressWarnings("rawtypes")
public class JesterDataImporter implements IImportHandler {

	public JesterDataImporter() {
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
		
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
		return null;
	}

}
