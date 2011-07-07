package ch.jester.common.utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

public class BundleResourceExporter {
	ServiceUtility mService = new ServiceUtility();
	IFileManager mFileManager = mService.getService(IFileManager.class);

	public void createDirStructure(List<String> entries, String templateDirectroy){
		for(String dir:entries){
			mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY+"/"+dir);
		}
	}
	public void export(URL src, File dest, boolean replace) throws ProcessingException, IOException{
		if(!replace&&dest.exists()){return;}
		mFileManager.toFile(src.openStream(), dest);
	}
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
