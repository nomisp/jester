package ch.jester.reportengine.impl;


public class JasperReportDef {
	private static String BUNDLE_ID ="ch.jester.reportengine.jasper.impl";
	private static String BUNDLE_REPORT_LOCATION="reports";
	String mBundleId = BUNDLE_ID;
	String mBundleLocation = BUNDLE_REPORT_LOCATION;
	String mAlias;
	String mVisibleName;
	String mReportPath;
	Class<?> mInputBeanClass;
	public JasperReportDef(String pAlias, String pVisibleName, String pReportPath, Class<?> pInputBeanClass){
		mAlias = pAlias;
		mVisibleName=pVisibleName;
		mReportPath=pReportPath;
		mInputBeanClass = pInputBeanClass;
	}
	public JasperReportDef(String pReportPath){
		this(null, null, pReportPath, null);
	}
	public boolean isSubReport(){
		return mVisibleName==null&&mInputBeanClass==null&&mAlias==null;
	}
}