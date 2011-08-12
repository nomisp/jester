package ch.jester.commonservices.api.reportengine;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Interface welches eine ReportEngine definiert.
 *
 */
public interface IReportEngine {
	/**
	 * Das Standardverzeichnis wohin die Templates/Reports installiert werden.
	 */
	public final static String TEMPLATE_DIRECTROY = "reportengine";
	/**
	 * Gibt das Report Repository zurück
	 * @return
	 */
	public IReportRepository getRepository();
	/**
	 * Generiert den Report als Rohformat
	 * @param pReport den zu erzeugenden IReport
	 * @param pBeans die Collection von JavaBeans
	 * @return das Resultat
	 * @throws ProcessingException
	 */
	public IReportResult generate(IReport pReport, Collection<?> pBeans, IProgressMonitor pMonitor) throws ProcessingException;
}
