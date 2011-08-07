package ch.jester.ui.tournament.handler;

import messages.Messages;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;

/**
 * Handler um Spieler zu entfernen
 *
 */
public class RemovePlayerHandler extends AbstractCommandHandler{
	@Override
	public Object executeInternal(ExecutionEvent event) {
		IStructuredSelection selection = mSelUtility.getAsStructuredSelection();
		if(selection instanceof TreeSelection){
			TreeSelection treeselection = (TreeSelection) selection;
			TreePath[] paths = treeselection.getPaths();
			for(TreePath p:paths){
				Category cat = null;
				for(int i=p.getSegmentCount()-1;i>=0;i--){
					Object o = p.getSegment(i);
					if(o instanceof Category){
						cat = (Category) o;
						break;
					}
				}
				if(cat!=null){
					 PlayerCard playerCard = null;
					for(PlayerCard card:cat.getPlayerCards()){
						if(card.getPlayer()==mSelUtility.getFirstSelectedAs(Player.class)){
							playerCard = card;
							break;
						}
					}
					
					final Category runCat = cat;
					final PlayerCard runCard = playerCard;
					if(cat.isPaired()){
						MessageDialog.openInformation(getShell(), Messages.RemovePlayerHandler_player_remove_failed_title, Messages.RemovePlayerHandler_player_remove_failed_cause);
						return null;
					}
					SafeRunner.run(new SafeMessageBoxRunner() {
						
						@Override
						public void run() throws Exception {
							runCat.removePlayerCard(runCard);
							IDaoService<Category> catDao = getServiceUtil().getDaoServiceByEntity(Category.class);
							catDao.save(runCat);
							catDao.close();
							
						}
					});
	
					
				}
			}
		}
		return null;
	}
	
}