package ch.jester.commonservices.api.reportengine;

import java.util.Collection;

import ch.jester.commonservices.exceptions.ProcessingException;

public interface IReportEngine {
	public IReportEngineFactory getFactory();
	public IReportResult generate(IReport pReport, Collection<?> pBeans) throws ProcessingException;
}
