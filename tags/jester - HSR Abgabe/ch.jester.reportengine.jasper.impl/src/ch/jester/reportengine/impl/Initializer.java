package ch.jester.reportengine.impl;

import java.util.UUID;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportRepository;


/**
 * Initialisierungshilfsklasse für die ReportEngine, um Konfigurationen zu speichern und zu laden.
 *
 */
public class Initializer extends AbstractPreferenceInitializer {
	public static String STORED_REPORT = "storedReports";
	public static String STORED_REPORT_NAME = "report.name.";
	public static String STORED_REPORT_FILE = "report.file.";
	public static String STORED_REPORT_CLASS = "report.class.";
	IPreferenceStore store;
	public Initializer() {
		store = Activator.getDefault().getPreferenceStore();
	}

	@Override
	public void initializeDefaultPreferences() {
		store.setDefault(STORED_REPORT , -1);

	}

	public void storeReport(IReport report) {
		int storedExternalReports = store.getInt(STORED_REPORT);
		storedExternalReports++;
		store.setValue(STORED_REPORT, storedExternalReports);
		store.setValue(STORED_REPORT_NAME+storedExternalReports, report.getVisibleName());
		store.setValue(STORED_REPORT_FILE+storedExternalReports, report.getInstalledFile().toString());	
		store.setValue(STORED_REPORT_CLASS+storedExternalReports, report.getInputBeanClass().getCanonicalName());
	}
	
	public void load(IReportRepository factory) {
		initializeDefaultPreferences();
		int reps = store.getInt(STORED_REPORT);
		for(int i=0;i<=reps;i++){
			String rName = store.getString(STORED_REPORT_NAME+i);
			String rPath = store.getString(STORED_REPORT_FILE+i);
			String rClass = store.getString(STORED_REPORT_CLASS+i);
			Class<?> clz = getClass(rClass);
			IReport report = factory.createFSReport(UUID.randomUUID().toString(), rName,  rPath);
			if(clz!=null){
				report.setInputBeanClass(clz);
			}
		}
		
	}

	private Class<?> getClass(String rClass) {
		try {
			Class<?> clz = Class.forName(rClass);
			return clz;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
