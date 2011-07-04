package ch.jester.common.ui.contributions;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;

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
	ServiceUtility su = new ServiceUtility();
	public ReportNameContributionItem() {

	}

	public ReportNameContributionItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {

		IReportEngine engine = su.getService(IReportEngine.class);
		List<IReport> reports = engine.getRepository().getReports();
		IContributionItem[] list = new IContributionItem[reports.size()];
		int i=0;
		
		for(IReport report:reports){
			list[i] = new CommandContributionItem(getParameter(report));
			i++;
		}
	    return list;

	}

	private CommandContributionItemParameter getParameter(IReport report){
		IServiceLocator s1 = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		CommandContributionItemParameter para = new CommandContributionItemParameter(s1, null, "ch.jester.reportengine.generate", SWT.PUSH);
		para.label = report.getVisibleName();
		para.parameters = new HashMap();
		para.parameters.put("ch.jester.reportengine.generate.property.reportalias", report.getAlias());
		return para;
	}
}
