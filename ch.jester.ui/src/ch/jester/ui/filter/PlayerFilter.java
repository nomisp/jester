package ch.jester.ui.filter;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.filter.IFilter;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;
import ch.jester.ui.player.editor.PlayerListController;

public class PlayerFilter implements IFilter{
	ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
	ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	@Override
	public IStatus filter(String pSearch, IProgressMonitor monitor) {
		if(pSearch.equals("")){
			su.getExclusiveService(PlayerListController.class).clearSearched();
		}
		monitor.beginTask("searching for: "+pSearch, IProgressMonitor.UNKNOWN);
		mLogger.debug("filtering: "+pSearch);
		List<Player> players = su.getExclusiveService(IPlayerDao.class).findByName(pSearch);
		su.getExclusiveService(PlayerListController.class).setSearched(players);
		su.getService(IExtendedStatusLineManager.class).setMessage("Found "+players.size()+" Item(s)", 1000);
		return Status.OK_STATUS;
	}

}
