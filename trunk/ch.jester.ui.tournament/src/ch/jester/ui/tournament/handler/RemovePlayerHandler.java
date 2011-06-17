package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;

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
					cat.removePlayerCard(playerCard);
					IDaoService<Category> catDao = getServiceUtil().getDaoServiceByEntity(Category.class);
					catDao.save(cat);
					catDao.close();
					
				}
			}
		}
		return null;
	}
	
}