package ch.jester.common.ui.contributions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;

import ch.jester.common.ui.filter.ReportEngineInputTester;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;

/**
 */
public class ReportNameContributionItem extends CompoundContributionItem {
	/**
	 * @uml.property  name="su"
	 * @uml.associationEnd  
	 */
	private ServiceUtility su = new ServiceUtility();
	private SelectionUtility selUtil = new SelectionUtility(null);
	private List<Object> NullList = new ArrayList<Object>(1);
	public ReportNameContributionItem() {

	}

	public ReportNameContributionItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		selUtil.setSelection(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection());
		List<?> beanlist;
		if(selUtil.getSelection()==null){
			beanlist = NullList;
		}else{
			beanlist = selUtil.getAsStructuredSelection().toList();
		}
		IReportEngine engine = su.getService(IReportEngine.class);
		List<IReport> reports = engine.getRepository().getReports();
		EnableStateCommandContributionItem[] list = new EnableStateCommandContributionItem[reports.size()];
		int i=0;
		
		for(IReport report:reports){
			list[i] = new EnableStateCommandContributionItem(getParameter(report));
			Collection<?> col = ReportEngineInputTester.getInputCollectionForReport(report.getInputBeanClass(), beanlist);
			if(col==null||col.isEmpty()){
				list[i].setEnabled(false);
			}else{
				list[i].setEnabled(true);
			}
			i++;
		}
	    return list;

	}

	private CommandContributionItemParameter getParameter(IReport report){
		IServiceLocator s1 = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		CommandContributionItemParameter para = null;
		
		para = new CommandContributionItemParameter(s1, null, "ch.jester.reportengine.generate", SWT.PUSH);
		para.label = report.getVisibleName();
		para.parameters = new HashMap();
		para.parameters.put("ch.jester.reportengine.generate.property.reportalias", report.getAlias());
		return para;
	}
	
	class EnableStateCommandContributionItem extends CommandContributionItem{
		private boolean mEnabled;
		public EnableStateCommandContributionItem(
				CommandContributionItemParameter contributionParameters) {
			super(contributionParameters);
		}
		public void setEnabled(boolean b){
			mEnabled = b;
		}
		@Override
		public boolean isEnabled() {
			return mEnabled;
		}
		
	}
}
