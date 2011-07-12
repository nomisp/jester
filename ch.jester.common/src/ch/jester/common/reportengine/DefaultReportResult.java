package ch.jester.common.reportengine;

import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;

/**
 * @author  t117221
 */
public abstract class DefaultReportResult<T> implements IReportResult{
	protected T mResult;
	protected String mOutptuName;
	/**
	 * @uml.property  name="mEngine"
	 * @uml.associationEnd  
	 */
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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public IReportEngine getReportEngine() {
		return mEngine;
	}

}
