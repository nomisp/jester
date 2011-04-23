package ch.jester.ui.filter;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.jester.commonservices.filter.IFilter;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;

public class PlayerFilter implements IFilter{
	ServiceUtility su = new ServiceUtility();
	@Override
	public IStatus filter(String pSearch, IProgressMonitor monitor) {
		monitor.beginTask("searching for: "+pSearch, IProgressMonitor.UNKNOWN);
		System.out.println("executing search: "+pSearch);
		List<Player> players = su.getExclusiveService(IPlayerPersister.class).findByName(pSearch);
		su.getExclusiveService(PlayerListController.class).setPlayers(players);
		return Status.OK_STATUS;
	}

}
