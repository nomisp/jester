package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.util.List;

/**
 * Die ReportEngineFactory, welche das physische Reporthandling übernimmt.
 *
 */
public interface IReportRepository {
	/**
	 * Erzeugt und registriert einen Report, welcher in einem Plugin gespeichert ist.<br>
	 * @param pBundle Das Bundle wo die Source liegt
	 * @param pAliasName Den internen Key
	 * @param pVisibleName Name fürs UI
	 * @param pFileName den FileNamen im SourceBundle
	 * @return
	 */
	public IBundleReport createBundleReport(String pBundle, String pAliasName, String pVisibleName, String pSource, String pFileName);
	
	/**
	 * Erzeugt und registriert einen Report, welcher im FileSystem gespeichert ist.<br>
	 * @param pAliasName
	 * @param pVisibleName
	 * @param pFileName
	 * @return
	 */
	public IReport createFSReport(String pAliasName, String pVisibleName, String pFileName);

	/**
	 * Gibt den Folder züruck, wohin die Reports installiert wurden.
	 * @return
	 */
	public File getInstallationDir();
	/**
	 * Löscht den Report
	 * @param pAlias
	 */
	public void deleteReport(String pAlias);
	/**
	 * Gibt den Report welcher mit dem alias verbunden ist zurück.
	 * @param pAlias
	 * @return
	 */
	public IReport getReport(String pAlias);
	/**
	 * @return alle Reporte
	 */
	public List<IReport> getReports();
	
}
