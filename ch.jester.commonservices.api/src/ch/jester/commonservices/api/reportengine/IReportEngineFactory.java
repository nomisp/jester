package ch.jester.commonservices.api.reportengine;

import java.io.File;
import java.util.List;

/**
 * Die ReportEngineFactory, welche das physische Reporthandling übernimmt.
 *
 */
public interface IReportEngineFactory {
	/**
	 * Erzeugt und registriert einen Report.<br>
	 * @param pBundle Das Bundle wo die Source liegt
	 * @param pAliasName Den internen Key
	 * @param pVisibleName Name fürs UI
	 * @param pFileName den FileNamen im SourceBundle
	 * @return
	 */
	public IReport createReport(String pBundle, String pAliasName, String pVisibleName, String pSource, String pFileName);
	/**
	 * Installiert und exportiert einen Report ins Filesystem.<br>
	 * Soll nicht von Clients aufgerufen werden!
	 * @param pReport
	 */
	public void installReport(IReport pReport);
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
