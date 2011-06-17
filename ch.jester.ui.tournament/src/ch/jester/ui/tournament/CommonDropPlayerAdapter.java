package ch.jester.ui.tournament;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.eclipse.ui.navigator.CommonNavigator;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

public class CommonDropPlayerAdapter extends CommonDropAdapterAssistant {

	public CommonDropPlayerAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus validateDrop(Object target, int operation,
			TransferData transferType) {
		if(target instanceof Category){
			return Status.OK_STATUS;
		}
		return Status.CANCEL_STATUS;
	}

	@Override
	public IStatus handleDrop(CommonDropAdapter aDropAdapter,
			DropTargetEvent aDropTargetEvent, Object aTarget) {
		Category cat = (Category) aTarget;
		//TransferData data = aDropAdapter.getCurrentTransfer();
		Object data = aDropTargetEvent.data;
		if(data instanceof ISelection){
			SelectionUtility su = new SelectionUtility((ISelection) data);
			Object[] objs = su.getAsStructuredSelection().toArray();
			Player last = null;
			for(Object o:objs){
					Player p = (Player)o;
					PlayerCard pc = ModelFactory.getInstance().createPlayerCard(cat, p);
					cat.addPlayerCard(pc);
					last = p;
			}	
				ServiceUtility util = new ServiceUtility();
				IDaoService<Category> catDao= util.getDaoServiceByServiceInterface(ICategoryDao.class);
				catDao.save(cat);
				catDao.close();
				util.closeAllTrackers();
				CommonNavigator cn = (CommonNavigator)UIUtility.getActiveWorkbenchWindow().getActivePage().findView(TournamentNavigator.ID);
		//		TreePath[] ex = cn.getCommonViewer().getExpandedTreePaths();
				//cn.getCommonViewer().getE
				
				//cn.getCommonViewer().setExpandedElements(ex);
		//		Object o = ex[ex.length-1].getLastSegment();
		//		cn.getCommonViewer().refresh(false);
		//		cn.getCommonViewer().setExpandedTreePaths(ex);
				
				//cn.getCommonViewer().setExpandedTreePaths();
				
				//cn.getCommonViewer().refresh();
				return Status.OK_STATUS;
			}

		
		return null;
	}

	
}
