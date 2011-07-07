package ch.jester.common.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class JesterModelExporter {
	private static BundleResourceExporter exporter = new BundleResourceExporter();
	
	public  void exportModelAsZip(String pDest){
		List<Bundle> bundles = getModelBundles();
		for(Bundle b:bundles){
			getPaths(b);
			
			getClassFiles(b);
		}
		
	}
	

	private List<String> getPaths(Bundle b){
		List<String> fileEntries = new ArrayList<String>();
		exporter.getBundlePathEntries(fileEntries, b.getEntryPaths("/bin"), b);
		System.out.println(fileEntries);
		return fileEntries;
	}
	private List<String> getClassFiles(Bundle b){
		List<String> fileEntries = new ArrayList<String>();
		exporter.getBundleFileEntries(fileEntries, b.getEntryPaths("/bin"), b);
		System.out.println(fileEntries);
		return fileEntries;
	}
	private List<Bundle> getModelBundles(){
		List bundles = new ArrayList<Bundle>();
		bundles.add(Platform.getBundle("ch.jester.model"));
		bundles.add(Platform.getBundle("ch.jester.commonservices.api"));
		return bundles;
	}
}
