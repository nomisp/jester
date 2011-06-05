package ch.jester.common.reportengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;

import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngineFactory;

public class DefaultReportFactory implements IReportEngineFactory {
	private HashMap<String, IReport> mReportMap = new HashMap<String, IReport>();
	private String mCompileDir;
	@Override
	public IReport createReport(String pBundle, String pAliasName, String pVisibleName,
			String pFileName) {
		DefaultReport report = new DefaultReport();
		report.setAlias(pAliasName);
		report.setVisibleName(pVisibleName);
		report.setFileName(pFileName);
		report.setBundle(Platform.getBundle(pBundle));
		mReportMap.put(pAliasName, report);
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

	private void compile(){
		for(IReport r:getReports()){
			
		}
	}
}
