package ch.jester.common.reportengine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportEngineFactory;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;

public class DefaultReportFactory implements IReportEngineFactory {
	private HashMap<String, IReport> mReportMap = new HashMap<String, IReport>();
	private IFileManager mFileManager;
	private ServiceUtility mServices = new ServiceUtility();
	public DefaultReportFactory(){
		mFileManager = mServices.getService(IFileManager.class);
		
	}
	
	@Override
	public IReport createReport(String pBundle, String pAliasName, String pVisibleName,
			 String pSource, String pFileName) {
		DefaultReport report = new DefaultReport();
		report.setAlias(pAliasName);
		report.setVisibleName(pVisibleName);
		report.setBundleReportFile(pFileName);
		report.setBundle(Platform.getBundle(pBundle));
		report.setBundleSourceRoot(pSource);
		mReportMap.put(pAliasName, report);
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

	@Override
	public void installReport(IReport pReport) {
		File engineFolder = mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY);
		String fullReportPath = pReport.getBundleReportFile();
		File tmp = new File(fullReportPath);
		String reportName = tmp.getName();
		//File destFile = new File(engineFolder+"/"+reportName);
		//pReport.setInstalledFile(destFile);
		
		//install sourcefolder
		String path = pReport.getBundleSourceRoot();
		
		List<String> fileEntries = new ArrayList<String>();
		getBundleFileEntries(fileEntries, pReport.getBundle().getEntryPaths(path), pReport.getBundle());
		
		List<String> pathEntries = new ArrayList<String>();
		getBundlePathEntries(pathEntries, pReport.getBundle().getEntryPaths(path), pReport.getBundle());
	
		System.out.println(fileEntries);
		
		for(String dir:pathEntries){
			mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY+"/"+dir);
		}
		
		
		for(String entry:fileEntries){
			URL src = pReport.getBundle().getResource(entry);
			File dst = new File(engineFolder+"/"+entry);
			if(entry.equals(fullReportPath)){
				pReport.setInstalledFile(dst);
			}
			if(dst.exists()){continue;}
			try {
				//mFileManager.getFolderInWorkingDirectory(destFile.getAbsolutePath());
				mFileManager.toFile(src.openStream(), dst);
			} catch (ProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//	en = pReport.getBundle().getEntryPaths(sourcepaths.get(0));
		//	String e0 = en.nextElement();
		//	System.out.println(e0);
	
		/*System.out.println(f.exists());
		System.out.println(f.isDirectory());
		System.out.println(sourcepaths);*/
		//
		
		
		
	/*	if(destFile.exists()){
			return;
		}
		try {
			mFileManager.toFile(pReport.getBundleFileAsStream(), destFile);
			

			
		} catch (ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	private boolean getBundleFileEntries(List<String> entries, Enumeration<String> en, Bundle bundle) {
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

	private boolean getBundlePathEntries(List<String> entries, Enumeration<String> en, Bundle bundle) {
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
	
	@Override
	public File getInstallationDir() {
		return mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY);
	}
}
