package ch.jester.reportengine.impl;

import java.util.ArrayList;
import java.util.List;


import messages.Messages;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
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

public class Activator extends AbstractUIActivator implements IAdapterFactory {

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
		Platform.getAdapterManager().registerAdapters(this, Tournament.class);

	}
	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if(adapterType==RankingReportInput.class){
			return new RankingReportInput((Tournament)adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[]{RankingReportInput.class};
	}

	 public void exportModelForStudioOrReports() {
			Job job = new Job(Messages.Activator_exporting_jester_model){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(Messages.Activator_exporting_model, IProgressMonitor.UNKNOWN);
					exporter.exportModelToFolder(IReportEngine.TEMPLATE_DIRECTROY+"/model"); //$NON-NLS-1$
					monitor.done();
					exporter = null;
					return Status.OK_STATUS;
				}
				
				
			};
			job.schedule();
			try {
				job.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}
	
	private void setExportableBundles(JesterModelExporter exporter) {
		exporter.addExportableBundle("ch.jester.model"); //$NON-NLS-1$
		exporter.addExportableBundle("ch.jester.commonservices.api"); //$NON-NLS-1$
		exporter.addExportableBundle("ch.jester.reportengine.jasper.impl"); //$NON-NLS-1$
		
	}

	 List<JasperReportDef> getReportDefinitions(){
		List<JasperReportDef> reportlist = new ArrayList<JasperReportDef>();
		reportlist.add(new JasperReportDef("playerlist", Messages.JasperReportEngine_player_report_name, "reports/PlayerList.jrxml", Player.class)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-2$
		//reportlist.add(new JasperReportDef("pairinglist_tournament", Messages.JasperReportEngine_tournament_report_name, "reports/PairingList.jrxml",Tournament.class));
		reportlist.add(new JasperReportDef("pairinglist_category", Messages.JasperReportEngine_category_pairing_name, "reports/PairingListCat.jrxml",Category.class)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-2$
		
		reportlist.add(new JasperReportDef("rankinglist", Messages.JasperReportEngine_ranking_report_name, "reports/RankingList.jrxml", RankingReportInput.class)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-2$
		
		reportlist.add(new JasperReportDef("rankinglist_internal", null, "reports/RankingListInternal.jrxml", RankingReportInput.class){ //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public boolean isSubReport() {
				return true;
			}
		});
		reportlist.add(new JasperReportDef("reports/RankingListCat.jrxml")); //$NON-NLS-1$
		reportlist.add(new JasperReportDef("reports/Category_RoundSubReport.jrxml")); //$NON-NLS-1$
		reportlist.add(new JasperReportDef("reports/Category_sub.jrxml")); //$NON-NLS-1$
		
		return reportlist;
	}


	@Override
	public void stopDelegate(BundleContext pContext) {
	}



}
