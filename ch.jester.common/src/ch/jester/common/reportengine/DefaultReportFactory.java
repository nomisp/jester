package ch.jester.common.reportengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;

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
			String pFileName) {
		DefaultReport report = new DefaultReport();
		report.setAlias(pAliasName);
		report.setVisibleName(pVisibleName);
		report.setBundleFileName(pFileName);
		report.setBundle(Platform.getBundle(pBundle));
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
		String reportName = pReport.getBundleFileName();
		File tmp = new File(reportName);
		reportName = tmp.getName();
		File destFile = new File(engineFolder+"/"+reportName);
		pReport.setInstalledFile(destFile);
		if(destFile.exists()){
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
		}
		
	}

	@Override
	public File getInstallationDir() {
		return mFileManager.getFolderInWorkingDirectory(IReportEngine.TEMPLATE_DIRECTROY);
	}
}
