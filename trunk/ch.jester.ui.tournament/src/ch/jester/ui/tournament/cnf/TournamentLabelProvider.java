package ch.jester.ui.tournament.cnf;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.ui.tournament.internal.Activator;

/**
 * LabelProvider für die Turnier-Navigation
 * @author Peter
 *
 */
public class TournamentLabelProvider extends LabelProvider implements ILabelProvider, IDescriptionProvider {
	
	public String getText(Object element) {
		if (element instanceof Tournament) {
			return ((Tournament)element).getName();
		} else if (element instanceof Category) {
			return ((Category)element).getDescription();
		} else if (element instanceof Player) {
			Player player = (Player)element;
			StringBuffer sb = new StringBuffer(player.getFirstName());
			sb.append(" ");
			sb.append(player.getLastName());
			return  sb.toString();
		} else if (element instanceof Round) {
			Round round = ((Round)element);
			StringBuffer sb = new StringBuffer("Round ");
			sb.append(round.getNumber());
			return  sb.toString();
		}
		return null;
	}

	@Override
	public String getDescription(Object element) {
		String text = getText(element);
		if (element instanceof Tournament) {
			return ((Tournament)element).getDescription().isEmpty() ? text : ((Tournament)element).getDescription();
		} else if (element instanceof Category) {
			return ((Category)element).getDescription().isEmpty() ? text : ((Category)element).getDescription();
		} else if (element instanceof Player) {
			Player p = ((Player)element);
			StringBuffer sb = new StringBuffer(p.getFirstName());
			sb.append(" ");
			sb.append(p.getLastName());
			return  sb.toString();
		} else if (element instanceof Round) {
			Round round = ((Round)element);
			StringBuffer sb = new StringBuffer("Round ");
			sb.append(round.getNumber());
			return  sb.toString();
		}
		return text;
	}

	public Image getImage(Object element) {
		if (element instanceof Tournament) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/tournament_16x16.gif").createImage();
//			return PlatformUI.getWorkbench().getSharedImages()
//					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else if (element instanceof Category) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/category_16x16.gif").createImage();
//			return PlatformUI.getWorkbench().getSharedImages()
//					.getImage(ISharedImages.IMG_OBJ_FILE);
		} else if (element instanceof Player) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/player_16x16.png").createImage();
//			return PlatformUI.getWorkbench().getSharedImages()
//					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		} else if (element instanceof Round) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/round_16x16.gif").createImage();
		}
		return null;
	}
}
