package ch.jester.common.reportengine;

import ch.jester.commonservices.api.reportengine.IReportResult;

public abstract class DefaultReportResult<T> implements IReportResult{
	protected T mResult;
	protected String mOutptuName;
	public DefaultReportResult(T pResult){
		mResult=pResult;
	}
	protected void setResult(T o){
		mResult = o;
	}
	
	@Override
	public boolean canExport(ExportType ex) {
		// TODO Auto-generated method stub
		return false;
	}

}
