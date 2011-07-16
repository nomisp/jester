package ch.jester.ui.tournament.cnf;

import messages.Messages;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.PlayerNamingUtility;

import ch.jester.ui.tournament.internal.Activator;

/**
 * LabelProvider für die Turnier-Navigation
 *
 */
public class TournamentLabelProvider extends LabelProvider implements ILabelProvider, IDescriptionProvider {
	String Label_Players = Messages.TournamentLabelProvider_lbl_player;
	String Label_Round = Messages.TournamentLabelProvider_lbl_round;
	public String getText(Object element) {
		if (element instanceof Tournament) {
			return ((Tournament)element).getName();
		} else if (element instanceof Category) {
			return ((Category)element).getDescription();
		} else if (element instanceof Player) {
			Player player = (Player)element;
			StringBuffer sb = new StringBuffer();
			sb.append(PlayerNamingUtility.createName(player));
			return  sb.toString();
		} else if (element instanceof Round) {
			Round round = (Round)element;
			StringBuffer sb = new StringBuffer(Label_Round);
			sb.append(round.getNumber());
			return  sb.toString();
		} else if (element instanceof Pairing) {
			Pairing pairing = ((Pairing)element);
			StringBuffer sb = new StringBuffer();
			sb.append(PlayerNamingUtility.createPairingName(pairing, " - ")); //$NON-NLS-1$
			if (pairing.getResult() != null) {
				sb.append(": "); //$NON-NLS-1$
				sb.append(pairing.getResult());
			}
			return  sb.toString();
		} else if (element instanceof PlayerFolder) {
			return Label_Players;
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
			sb.append(" "); //$NON-NLS-1$
			sb.append(p.getLastName());
			return  sb.toString();
		} else if (element instanceof Round) {
			Round round = ((Round)element);
			StringBuffer sb = new StringBuffer(Label_Round);
			sb.append(round.getNumber());
			return  sb.toString();
		} else if (element instanceof Pairing) {
			Pairing pairing = ((Pairing)element);
			StringBuffer sb = new StringBuffer();
			sb.append(PlayerNamingUtility.createPairingName(pairing, " - ")); //$NON-NLS-1$
			if (pairing.getResult() != null) {
				sb.append(": "); //$NON-NLS-1$
				sb.append(pairing.getResult());
			}
			return  sb.toString();
		} else if (element instanceof PlayerFolder) {
			return Label_Players;
		}
		return text;
	}

	public Image getImage(Object element) {
		if (element instanceof Tournament) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/tournament.png").createImage(); //$NON-NLS-1$
		} else if (element instanceof Category) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/category_16x16.gif").createImage(); //$NON-NLS-1$
		} else if (element instanceof PlayerFolder) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/folder_player.png").createImage(); //$NON-NLS-1$
		} else if (element instanceof Player) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/player.png").createImage(); //$NON-NLS-1$
		} else if (element instanceof Round) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/round_16x16.gif").createImage(); //$NON-NLS-1$
		} else if (element instanceof Pairing) {
			return UIUtility.getImageDescriptor(
					Activator.getDefault().getActivationContext().getPluginId(),
						"icons/pairing.png").createImage(); //$NON-NLS-1$
		}
		return null;
	}
}
