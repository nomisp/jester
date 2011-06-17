package ch.jester.ui.tournament;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.factories.ModelFactory;
import ch.jester.ui.tournament.cnf.PlayerFolder;

public class CommonDropPlayerAdapter extends CommonDropAdapterAssistant {

	public CommonDropPlayerAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus validateDrop(Object target, int operation,
			TransferData transferType) {
		if(target instanceof Category || target instanceof PlayerFolder){
			return Status.OK_STATUS;
		}
		return Status.CANCEL_STATUS;
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
					PlayerCard pc = ModelFactory.getInstance().createPlayerCard(cat, p);
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
