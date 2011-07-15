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
/*
*
*	IBundleReport report = factory.createBundleReport(BUNDLE_ID, "playerlist", Messages.JasperReportEngine_player_report_name, BUNDLE_REPORT_LOCATION, "reports/PlayerList.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Player.class);

    	report = factory.createBundleReport(BUNDLE_ID, "rankinglist", "RankingList", BUNDLE_REPORT_LOCATION, "reports/RankingList.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(RankingReportInput.class);
    	
    	report = factory.createBundleReport(BUNDLE_ID, "pairinglist_tournament", Messages.JasperReportEngine_tournament_report_name, BUNDLE_REPORT_LOCATION, "reports/PairingList.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Tournament.class);
    	report = factory.createBundleReport(BUNDLE_ID, "pairinglist_category", Messages.JasperReportEngine_category_pairing_name, BUNDLE_REPORT_LOCATION, "reports/PairingListCat.jrxml"); //$NON-NLS-1$ //$NON-NLS-3$
    	report.setInputBeanClass(Category.class);
    	IBundleReport src = factory.createBundleReport(BUNDLE_ID, null, null, BUNDLE_REPORT_LOCATION, "reports/Category_RoundSubReport.jrxml"); //$NON-NLS-1$
    	cache.addCachedReport(src, null, null);
    	src = factory.createBundleReport(BUNDLE_ID, null, null, BUNDLE_REPORT_LOCATION, "reports/Category_sub.jrxml"); //$NON-NLS-1$
    	cache.addCachedReport(src, null, null);
*
*
*
*
*
*
*/