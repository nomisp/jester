package ch.jester.ui.tournament.cnf;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.IDescriptionProvider;

import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Tournament;

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
			return player.getFirstName() + " " + player.getLastName();
		}
		return null;
	}

	@Override
	public String getDescription(Object element) {
		String text = getText(element);
		return "This is a description of " + text;
	}

	public Image getImage(Object element) {
		if (element instanceof Tournament) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else if (element instanceof Category) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE);
		} else if (element instanceof Player) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
}
		return null;
	}
}
