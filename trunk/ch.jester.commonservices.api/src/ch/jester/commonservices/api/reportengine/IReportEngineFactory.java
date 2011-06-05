package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.util.List;

public interface IReportEngineFactory {
	public IReport createReport(String pBundle, String pAliasName, String pVisibleName, String pFileName);
	public void installReport(IReport pReport);
	public File getInstallationDir();
	public void deleteReport(String pAlias);
	public IReport getReport(String pAlias);
	public List<IReport> getReports();
	
}
