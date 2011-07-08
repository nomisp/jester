package ch.jester.ui.round.editors;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.model.Player;
import ch.jester.ui.tournament.internal.RankingEntityHelp;

public class TestHandler extends AbstractCommandHandler{

	@Override
	public Object executeInternal(ExecutionEvent event) {
		List<Player> players = getServiceUtil().getDaoServiceByEntity(Player.class).getAll();
		RankingEntityHelp hlp = new RankingEntityHelp(players);
		openEditor(hlp);
		return null;
	}

}
