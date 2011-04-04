package ch.jester.ui.player;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

import ch.jester.model.Player;


public class PlayerDetailsController {
	private PlayerDetails mPlayerDetail;
	private DataBindingContext mBindingContext;
	private Player player = new Player();

	public PlayerDetailsController(PlayerDetails playerDetails) {
		mPlayerDetail = playerDetails;
		if (player != null) {
			mBindingContext = initDataBindings();
		}
	}

	private DataBindingContext initDataBindings() {
		IObservableValue lastNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getLastNameText(), SWT.Modify);
		IObservableValue lastNameObserveValue = PojoObservables.observeValue(player, "lastName");
		IObservableValue firstNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getFirstNameText(), SWT.Modify);
		IObservableValue firstNameObserveValue = PojoObservables.observeValue(player, "firstName");
		IObservableValue cityObserveWidget = SWTObservables.observeText(mPlayerDetail.getCityText(), SWT.Modify);
		IObservableValue cityObserveValue = PojoObservables.observeValue(player, "city");
		IObservableValue nationObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationText(), SWT.Modify);
		IObservableValue nationObserveValue = PojoObservables.observeValue(player, "nation");
		IObservableValue fideCodeObserveWidget = SWTObservables.observeText(mPlayerDetail.getFideCodeText(), SWT.None);
		IObservableValue fideCodeObserveValue = PojoObservables.observeValue(player, "fideCode");
		IObservableValue nationalCodeObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalCodeText(), SWT.None);
		IObservableValue nationalCodeObserveValue = PojoObservables.observeValue(player, "nationalCode");
		IObservableValue eloObserveWidget = SWTObservables.observeText(mPlayerDetail.getEloText(), SWT.None);
		IObservableValue eloObserveValue = PojoObservables.observeValue(player, "elo");
		IObservableValue nationalEloObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalEloText(), SWT.None);
		IObservableValue nationalEloObserveValue = PojoObservables.observeValue(player, "nationalElo");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(lastNameObserveWidget, lastNameObserveValue, null, null);
		bindingContext.bindValue(firstNameObserveWidget, firstNameObserveValue, null, null);
		bindingContext.bindValue(cityObserveWidget, cityObserveValue, null, null);
		bindingContext.bindValue(nationObserveWidget, nationObserveValue, null, null);
		bindingContext.bindValue(fideCodeObserveWidget, fideCodeObserveValue, null, null);
		bindingContext.bindValue(nationalCodeObserveWidget, nationalCodeObserveValue, null, null);
		bindingContext.bindValue(eloObserveWidget, eloObserveValue, null, null);
		bindingContext.bindValue(nationalEloObserveWidget, nationalEloObserveValue, null, null);
		//
		return bindingContext;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player newPlayer) {
		setPlayer(newPlayer, true);
	}

	public void setPlayer(Player newPlayer, boolean update) {
		player = newPlayer;
		if (update) {
			if (mBindingContext != null) {
				mBindingContext.dispose();
				mBindingContext = null;
			}
			if (player != null) {
				mBindingContext = initDataBindings();
			}
		}
	}
}