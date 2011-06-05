package ch.jester.commonservices.api.reportengine;

import java.util.Collection;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Interface welches eine ReportEngine definiert.
 *
 */
public interface IReportEngine {
	/**
	 * Das Standardverzeichnis wohin die Templates/Reports installiert werden.
	 */
	public final static String TEMPLATE_DIRECTROY = "reportengine_templates";
	/**
	 * Gibt die ReportEngineFactory zurück
	 * @return
	 */
	public IReportEngineFactory getFactory();
	/**
	 * Generiert den Report
	 * @param pReport den zu erzeugenden IReport
	 * @param pBeans die Collection von JavaBeans
	 * @return das Resultat
	 * @throws ProcessingException
	 */
	public IReportResult generate(IReport pReport, Collection<?> pBeans) throws ProcessingException;
}
