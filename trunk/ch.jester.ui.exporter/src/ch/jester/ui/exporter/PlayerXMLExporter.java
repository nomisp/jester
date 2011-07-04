package ch.jester.ui.exporter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationWriter;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;


public class PlayerXMLExporter extends Wizard implements IExportWizard {
	PlayerXMLPage firstPage = new PlayerXMLPage();
	public final static String EXPORT_ZIP_ENTRY = "jester-export.xml";
	public PlayerXMLExporter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.setNeedsProgressMonitor(true);

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(firstPage);

	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean performFinish() {
		try {
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		String outFile = firstPage.getFileName();
		Class<?>[] outClass = firstPage.getSelectedCalsses();
		SerializationWriter writer = serializer.createWriter(outFile);
		writer.newEntry(EXPORT_ZIP_ENTRY);
		ServiceUtility su = new ServiceUtility();
		
		for(Class c:outClass){
			writer.prepareContext(c);
			
			@SuppressWarnings("unchecked")
			IDaoService<?> service = su.getDaoServiceByEntity(c);
			List<?> allDomainObjects = service.getAll();
			writer.write(allDomainObjects);
			service.close();
			
			
		}
		writer.close();
		
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


}
