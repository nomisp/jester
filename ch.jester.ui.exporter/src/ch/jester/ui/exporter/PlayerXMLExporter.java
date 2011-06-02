package ch.jester.ui.exporter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.common.utility.ObjectXMLSerializer;
import ch.jester.common.utility.ObjectXMLSerializer.SerializationWriter;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Player;


public class PlayerXMLExporter extends Wizard implements IExportWizard {
	PlayerXMLPage firstPage = new PlayerXMLPage();
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
	@Override
	public boolean performFinish() {
		try {
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();
		String outFile = firstPage.getFileName();
		Class[] outClass = firstPage.getSelectedCalsses();
		SerializationWriter writer = serializer.createWriter(outFile);
		writer.newEntry("jester-export.xml");
		ServiceUtility su = new ServiceUtility();
		
		for(Class c:outClass){
			writer.prepareContext(c);
			
			IDaoService<?> service = su.getDaoService(c);
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
