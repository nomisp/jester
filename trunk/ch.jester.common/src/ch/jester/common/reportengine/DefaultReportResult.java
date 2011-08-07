package ch.jester.common.reportengine;

import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;

/**
 * Default Impl. für ein (abstraktes) Result eines generierten Reports
 *
 * @param <T>
 */
public abstract class DefaultReportResult<T> implements IReportResult{
	protected T mResult;
	protected String mOutptuName;
	protected IReportEngine mEngine;
	public DefaultReportResult(T pResult, IReportEngine pEngine){
		mResult=pResult;
		mEngine = pEngine;
	}
	protected void setResult(T o){
		mResult = o;
	}
	
	@Override
	public boolean canExport(ExportType ex) {
		return false;
	}
	
	@Override
	public IReportEngine getReportEngine() {
		return mEngine;
	}

}
