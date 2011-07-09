package ch.jester.ui.tournament;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.cnf.PlayerFolder;
import ch.jester.ui.tournament.internal.Activator;

public class CommonDropPlayerAdapter extends CommonDropAdapterAssistant {
	ILogger mLogger;
	public CommonDropPlayerAdapter() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
	}

	protected boolean dragSourceEqualsDropTarget(){
		return LocalSelectionTransfer.getTransfer().getSelection()!=null;
	}
	public IStatus validateDropWithSameSourceTarget(Object target, int operation,
			TransferData transferType) {
		SelectionUtility su = new SelectionUtility(LocalSelectionTransfer.getTransfer().getSelection());
		if(su.getFirstSelectedAs(Player.class)==null){
			return null;
		}
		if(isDropInSameHierarchy(target)){
			mLogger.debug("DND: DND with same root object not allowed");
			return null;
		}
		return Status.OK_STATUS;
		
	}
	public IStatus validateDropWitDiffSourceTarget(Object target, int operation,
			TransferData transferType) {
			return Status.OK_STATUS;
		
	}
	
	protected boolean isTargetClassValid(Object pTarget){
		if(pTarget instanceof Category || pTarget instanceof PlayerFolder){
			return true;
		}
		return false;
	}
	protected boolean isSupported(TransferData transferType){
		return LocalSelectionTransfer.getTransfer().isSupportedType(transferType);
		
	}
	
	protected boolean isDropInSameHierarchy(Object target){
		SelectionUtility su = new SelectionUtility(LocalSelectionTransfer.getTransfer().getSelection());
		if(su.getSelection() instanceof TreeSelection){
			TreeSelection treeselection = (TreeSelection) su.getSelection();
			TreePath[] paths = treeselection.getPaths();
			for(TreePath p:paths){
				for(int i=p.getSegmentCount()-1;i>=0;i--){
					Object pathObj = p.getSegment(i);
					if(pathObj==target){
						return true;
					}
				}
			}
		}
		
		
		return false;
	}
	
	@Override
	public IStatus validateDrop(Object target, int operation,
			TransferData transferType) {
		mLogger.debug("DND: DragSourceEqualsDropTarget: "+dragSourceEqualsDropTarget());
		
		if(!isSupported(transferType)){
			mLogger.debug("DND: TransferType not supported");
			return null;
		}
		
		if(!isTargetClassValid(target)){
			mLogger.debug("DND: TargetClass not valid");
			return null;
		}
	
		if(dragSourceEqualsDropTarget()){
			return validateDropWithSameSourceTarget(target, operation, transferType);
		}
		return validateDropWitDiffSourceTarget(target, operation, transferType);
		
	}

	@Override
	public IStatus handleDrop(CommonDropAdapter aDropAdapter,
			DropTargetEvent aDropTargetEvent, Object aTarget) {
		
		Category cat = null;
		if(aTarget instanceof Category){
			cat = (Category) aTarget;
		}else if(aTarget instanceof PlayerFolder){
			cat = (Category) ((PlayerFolder)aTarget).getParent();
		}

		Object data = aDropTargetEvent.data;
		if(data instanceof ISelection){
			SelectionUtility su = new SelectionUtility((ISelection) data);
			Object[] objs = su.getAsStructuredSelection().toArray();
			for(Object o:objs){
					Player p = (Player)o;
					PlayerCard pc = ModelFactory.getInstance().createPlayerCard(cat, p, cat.getTournament().getPrimaryRankingSystem().getShortType());
					cat.addPlayerCard(pc);
			}	
				ServiceUtility util = new ServiceUtility();
				IDaoService<Category> catDao= util.getDaoServiceByServiceInterface(ICategoryDao.class);
				catDao.save(cat);
				catDao.close();
				util.closeAllTrackers();
				return Status.OK_STATUS;
			}

		
		return null;
	}

	
}