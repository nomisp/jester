package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.navigator.CommonNavigator;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

public class EditTournamentHandler extends AbstractCommandHandler implements IHandler {

	private Tournament selectedTournament;
//	private DaoController<?> mController;
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		selectedTournament = getFirstSelectedAs(Tournament.class);
		if (selectedTournament != null) {
//			IDaoService<Tournament> tdao= Activator.getDefault().getActivationContext().getService(ITournamentDao.class);
//			mController = new DaoController<Tournament>(this, getTable(), tdao){
			openEditor(selectedTournament);
			CommonNavigator cn = (CommonNavigator)getView(TournamentNavigator.ID);
			cn.getCommonViewer().refresh();
		}
		return null;
	}

}
