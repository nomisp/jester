package ch.jester.common.reportengine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.jester.common.utility.BundleResourceExporter;
import ch.jester.common.utility.JesterModelExporter;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IBundleReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportRepository;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * @author  t117221
 */
public class DefaultReportRepository implements IReportRepository {
	private HashMap<String, IReport> mReportMap = new HashMap<String, IReport>();
	private BundleResourceExporter exporter = new BundleResourceExporter();
	private boolean modelExported = false;
	/**
	 * @uml.property  name="mFileManager"
	 * @uml.associationEnd  
	 */
	private IFileManager mFileManager;
	/**
	 * @uml.property  name="mServices"
	 * @uml.associationEnd  
	 */
	private ServiceUtility mServices = new ServiceUtility();
	public DefaultReportRepository(){
		mFileManager = mServices.getService(IFileManager.class);
		
	}
	
	@Override
	public IBundleReport createBundleReport(String pBundle, String pAliasName, String pVisibleName,
			 String pSource, String pFileName) {
			return createInternalReport(pBundle, pAliasName, pVisibleName, pSource, pFileName);
	}

	@Override
	public IReport createFSReport(String pAliasName, String pVisibleName,
			String pFileName) {
		return createExternalReport(pAliasName, pVisibleName, pFileName);
	}
	
	private IReport createExternalReport(String pAliasName, String pVisibleName, String pFileName){
		DefaultReport report = new DefaultReport();
		report.setAlias(pAliasName);
		report.setVisibleName(pVisibleName);
		mReportMap.put(pAliasName, report);
		report.setInstalledFile(new File(pFileName));
		return report;
	}
	private IBundleReport createInternalReport(String pBundle, String pAliasName, String pVisibleName,
			 String pSource, String pFileName) {
		DefaultFSReport report = new DefaultFSReport();
		report.setAlias(pAliasName);
		report.setVisibleName(pVisibleName);
		report.setBundleReportFile(pFileName);
		report.setBundle(Platform.getBundle(pBundle));
		report.setBundleSourceRoot(pSource);
		if(pAliasName!=null){
			mReportMap.put(pAliasName, report);
		}
		installReport(report);
		return report;
	}
	@Override
	public IReport getReport(String pAlias) {
		return mReportMap.get(pAlias);
	}

	@Override
	public void deleteReport(String pAlias) {
		mReportMap.remove(pAlias);
	}

	@Override
	public List<IReport> getReports() {
		Iterator<String> it = mReportMap.keySet().iterator();
		List<IReport> rList = new ArrayList<IReport>();
		while(it.hasNext()){
			rList.add(mReportMap.get(it.next()));
		}
		return rList;
	}

	private void installReport(IBundleReport pReport) {
		JesterModelExporter ex = new JesterModelExporter();
		ex.exportModelAsZip(null);
		File engineFolder = mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY);
		String fullReportPath = pReport.getBundleReportFile();
		//install sourcefolder
		String path = pReport.getBundleSourceRoot();
		
		List<String> fileEntries = new ArrayList<String>();
		exporter.getBundleFileEntries(fileEntries, pReport.getBundle().getEntryPaths(path), pReport.getBundle());
		
		List<String> pathEntries = new ArrayList<String>();
		exporter.getBundlePathEntries(pathEntries, pReport.getBundle().getEntryPaths(path), pReport.getBundle());

		exporter.createDirStructure(pathEntries, IReportEngine.TEMPLATE_DIRECTROY);
		
		
		for(String entry:fileEntries){
			URL src = pReport.getBundle().getResource(entry);
			File dst = new File(engineFolder+"/"+entry);
			if(entry.equals(fullReportPath)){
				pReport.setInstalledFile(dst);
			}
			try {
				exporter.export(src, dst, false);
			} catch (ProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Override
	public File getInstallationDir() {
		return mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY);
	}

}
