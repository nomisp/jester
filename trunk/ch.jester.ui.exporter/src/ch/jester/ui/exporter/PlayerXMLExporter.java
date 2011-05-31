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
		// TODO Auto-generated method stub

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(firstPage);

	}
	@Override
	public boolean performFinish() {
		try {
			JAXBContext jc = JAXBContext.newInstance(Players.class);
			Marshaller marshaller = jc.createMarshaller();
			IDaoService<Player> persister = new ServiceUtility().getDaoService(Player.class);
			List<Player> plist = persister.getAll();
			Players p = new Players();
			p.setPlayers(plist);
			try {
				 	FileOutputStream fout = new FileOutputStream(firstPage.getFileName());
				    ZipOutputStream zout = new ZipOutputStream(fout);
				    zout.putNextEntry(new ZipEntry("players.xml"));
			
				marshaller.marshal(p, zout);
				zout.flush();
				zout.closeEntry();
				zout.close();
				fout.flush();
				fout.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	@XmlRootElement(name = "Players")
	public static class Players {

	    @XmlElement(name = "Player")
	    protected List<Player> item;

	    public List<Player> getPlayers() {
	        return this.item;
	    }
	    public void setPlayers(List<Player> p){
	    	this.item=p;
	    }
	}

}
