package ch.jester.common.utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

public class JesterModelExporter {
	private static BundleResourceExporter exporter = new BundleResourceExporter();
	private ServiceUtility mServices = new ServiceUtility();
	private List<Bundle> bundlesToExport = new ArrayList<Bundle>();
	private ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
	public void addExportableBundle(String pBundleId){
		Bundle b = Platform.getBundle(pBundleId);
		if(!bundlesToExport.contains(b)){
			bundlesToExport.add(b);
		}
	}
	
	public  void exportModelToFolder(String pDest){
		List<Bundle> bundles = bundlesToExport;
		String target = pDest;
		//String target = mServices.getService(IFileManager.class).createTempFolder();
		File absoluteTarget = mServices.getService(IFileManager.class).getFolderInWorkingDirectory(target);
		for(Bundle b:bundles){
			List<String> paths = getPaths(b);
			exporter.createDirStructure(paths,target );

			List<String> fileEntries = getClassFiles(b);
			for(String entry:fileEntries){
				URL src = b.getResource(entry);
				File dst = new File(absoluteTarget.getAbsoluteFile()+"/"+entry);
				try {
					exporter.export(src, dst, false);
				} catch (ProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	

	@SuppressWarnings("unchecked")
	private List<String> getPaths(Bundle b){
		List<String> fileEntries = new ArrayList<String>();
		exporter.getBundlePathEntries(fileEntries, b.getEntryPaths("/bin"), b);
		mLogger.debug(fileEntries.toString());
		return fileEntries;
	}
	@SuppressWarnings("unchecked")
	private List<String> getClassFiles(Bundle b){
		List<String> fileEntries = new ArrayList<String>();
		exporter.getBundleFileEntries(fileEntries, b.getEntryPaths("/bin"), b);
		mLogger.debug(fileEntries.toString());
		return fileEntries;
	}

}
