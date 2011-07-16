package ch.jester.reportengine.impl;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.utility.JesterModelExporter;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Tournament;
import ch.jester.model.util.RankingReportInput;

public class Activator extends AbstractUIActivator {

	private static Activator instance;
	private JesterModelExporter exporter;
	public static Activator getDefault(){
		return instance;
	}
	
	@Override
	public void startDelegate(BundleContext pContext) {
		instance=this;
		exporter = new JesterModelExporter();
		setExportableBundles(exporter);
		exportModelForStudioOrReports();

	}

	private void exportModelForStudioOrReports() {
			Job job = new Job("Exporting jester model"){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("exporting model", IProgressMonitor.UNKNOWN);
					exporter.exportModelToFolder(IReportEngine.TEMPLATE_DIRECTROY+"/model");
					monitor.done();
					exporter = null;
					return Status.OK_STATUS;
				}
				
				
			};
			job.schedule();

		
	}
	
	private void setExportableBundles(JesterModelExporter exporter) {
		exporter.addExportableBundle("ch.jester.model");
		exporter.addExportableBundle("ch.jester.commonservices.api");
		exporter.addExportableBundle("ch.jester.reportengine.jasper.impl");
		
	}

	 List<JasperReportDef> getReportDefinitions(){
		List<JasperReportDef> reportlist = new ArrayList<JasperReportDef>();
		reportlist.add(new JasperReportDef("playerlist", Messages.JasperReportEngine_player_report_name, "reports/PlayerList.jrxml", Player.class));
		reportlist.add(new JasperReportDef("pairinglist_tournament", Messages.JasperReportEngine_tournament_report_name, "reports/PairingList.jrxml",Tournament.class));
		reportlist.add(new JasperReportDef("pairinglist_category", Messages.JasperReportEngine_category_pairing_name, "reports/PairingListCat.jrxml",Category.class));
		
		reportlist.add(new JasperReportDef("rankinglist", Messages.JasperReportEngine_ranking_report_name, "reports/RankingList.jrxml", RankingReportInput.class));
		
		reportlist.add(new JasperReportDef("rankinglist_internal", null, "reports/RankingListInternal.jrxml", RankingReportInput.class){
			@Override
			public boolean isSubReport() {
				return true;
			}
		});
		reportlist.add(new JasperReportDef("reports/RankingListCat.jrxml"));
	
		
		
		
		reportlist.add(new JasperReportDef("reports/Category_RoundSubReport.jrxml"));
		reportlist.add(new JasperReportDef("reports/Category_sub.jrxml"));
		
		return reportlist;
	}


	@Override
	public void stopDelegate(BundleContext pContext) {
	}

}