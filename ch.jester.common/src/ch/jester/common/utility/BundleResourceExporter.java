package ch.jester.common.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Utility Klasse um Resourcen von einem Plugin zu exportieren
 *
 */
public class BundleResourceExporter {
	ServiceUtility mService = new ServiceUtility();
	IFileManager mFileManager = mService.getService(IFileManager.class);

	/**
	 * Erzeugung der Directory Struktur
	 * @param entries
	 * @param pRootDirectory
	 */
	public void createDirStructure(List<String> entries, String pRootDirectory){
		for(String dir:entries){
			mFileManager.getFolderInWorkingDirectory(pRootDirectory+"/"+dir);
		}
	}
	/**
	 * Exportiert die URL zum Destination File.
	 * @param src
	 * @param dest
	 * @param replace
	 * @throws ProcessingException
	 * @throws IOException
	 */
	public void export(URL src, File dest, boolean replace) throws ProcessingException, IOException{
		if(!replace&&dest.exists()){return;}
		InputStream ins;
		mFileManager.toFile(ins = src.openStream(), dest);
		ins.close();
		
	}
	/**
	 * Liest die Entries aus der übergebenen Enumeration
	 * @param entries Liste in die geschrieben wird
	 * @param en die Entries
	 * @param bundle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getBundleFileEntries(List<String> entries, Enumeration<String> en, Bundle bundle) {
		if(en==null){return false;}
		while(en.hasMoreElements()){
			String entry = en.nextElement();
			if(entry.indexOf("/.")==-1){
				boolean added = getBundleFileEntries(entries, bundle.getEntryPaths(entry), bundle);
				if(!added){
					entries.add(entry);
				}
			}
		}
		return true;
	}

	/**
	 * Liest die Entries (Dir) aus der übergebenen Enumeration
	 * @param entries
	 * @param en
	 * @param bundle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean getBundlePathEntries(List<String> entries, Enumeration<String> en, Bundle bundle) {
		if(en==null){return true;}
		while(en.hasMoreElements()){
			String entry = en.nextElement();
			if(entry.indexOf("/.")==-1){
				boolean added = getBundlePathEntries(entries, bundle.getEntryPaths(entry), bundle);
				if(!added){
					entries.add(entry);
				}
			}
		}
		return false;
	}
}
