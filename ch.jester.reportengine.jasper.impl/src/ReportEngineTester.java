import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.Bundle;

import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.model.factories.ModelFactory;
import ch.jester.reportengine.impl.JasperReportEngine;


public class ReportEngineTester {
	public static void main(String args[]){
		IReportEngine engine = new JasperReportEngine();
		IReport report = getReport();
		IReportResult result = engine.generate(report, getBeans());
		result.export(ExportType.HTML);
		result.export(ExportType.PDF);
		result.export(ExportType.XML);
		
	}
	
	public static Collection<?> getBeans(){
		List<Object> al = new ArrayList<Object>();
		al.add(ModelFactory.getInstance().createPlayer("John","Doe"));
		al.add(ModelFactory.getInstance().createPlayer("a","b"));
		al.add(ModelFactory.getInstance().createPlayer());
		al.add(ModelFactory.getInstance().createPlayer());
		al.add(ModelFactory.getInstance().createPlayer());
		return al;
	}
	public static IReport getReport(){
		return new IReport() {
			
			@Override
			public void setAlias(String pString) {
				
			}
			
			@Override
			public void setBundleReportFile(String pFilePath) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getAlias() {
				return "playerlist";
			}
			
			@Override
			public String getBundleReportFile() {
				return "PlayerList.jrxml";
			}

			@Override
			public void setVisibleName(String pString) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getVisibleName() {
				return "Player List";
			}

			@Override
			public InputStream getBundleFileAsStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setBundle(Bundle b) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setInstalledFile(File pInstalled) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public InputStream getInstalledFileAsStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public File getInstalledFile() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setBundleSourceRoot(String pRoot) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getBundleSourceRoot() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
