package ch.jester.ui.filter;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IViewPart;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.ui.filter.IUIFilter;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.ui.Activator;

/**
 * Filtern der Player.<br>
 * Es werden nur die ersten 1000 Hits berücksichtigt.<br>
 * Gesucht wird nach Namen.<br>
 *
 */
public class PlayerFilter implements IUIFilter{
	ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
	ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	DaoController<Player> daoc;
	@SuppressWarnings("unchecked")
	@Override
	public IStatus filter(String pSearch, IViewPart pPart, IProgressMonitor monitor) {
		if(daoc==null){
			daoc = AdapterUtility.getAdaptedObject(pPart, DaoController.class);
		}

		monitor.beginTask("searching for: "+pSearch, IProgressMonitor.UNKNOWN);
		mLogger.debug("filtering: "+pSearch);
		List<Player> players = su.getDaoServiceByServiceInterface(IPlayerDao.class).findByNamesOrCity(pSearch);
		daoc.setSearched(players);
		su.getService(IExtendedStatusLineManager.class).setMessage("Found "+players.size()+" Item(s)", 1000);
		return Status.OK_STATUS;
	}
	@Override
	public void clear() {
		daoc.clearSearched();
	}

}
