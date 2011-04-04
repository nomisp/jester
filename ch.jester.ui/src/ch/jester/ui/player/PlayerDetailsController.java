package ch.jester.ui.player;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;


public class PlayerDetailsController {
	private PlayerDetails m_playerDetails;
	private DataBindingContext m_bindingContext;
	private ch.jester.model.Player player = new ch.jester.model.Player();

	public PlayerDetailsController(PlayerDetails playerDetails, ch.jester.model.Player newPlayer) {
		m_playerDetails = playerDetails;
		setPlayer(newPlayer);
	}

	public PlayerDetailsController(PlayerDetails playerDetails) {
		m_playerDetails = playerDetails;
		if (player != null) {
			m_bindingContext = initDataBindings();
		}
	}

	private DataBindingContext initDataBindings() {
		IObservableValue lastNameObserveWidget = SWTObservables.observeText(m_playerDetails.getLastNameText(), SWT.Modify);
		IObservableValue lastNameObserveValue = PojoObservables.observeValue(player, "lastName");
		IObservableValue firstNameObserveWidget = SWTObservables.observeText(m_playerDetails.getFirstNameText(), SWT.Modify);
		IObservableValue firstNameObserveValue = PojoObservables.observeValue(player, "firstName");
		IObservableValue cityObserveWidget = SWTObservables.observeText(m_playerDetails.getCityText(), SWT.Modify);
		IObservableValue cityObserveValue = PojoObservables.observeValue(player, "city");
		IObservableValue nationObserveWidget = SWTObservables.observeText(m_playerDetails.getNationText(), SWT.Modify);
		IObservableValue nationObserveValue = PojoObservables.observeValue(player, "nation");
		IObservableValue fideCodeObserveWidget = SWTObservables.observeText(m_playerDetails.getFideCodeText(), SWT.None);
		IObservableValue fideCodeObserveValue = PojoObservables.observeValue(player, "fideCode");
		IObservableValue nationalCodeObserveWidget = SWTObservables.observeText(m_playerDetails.getNationalCodeText(), SWT.None);
		IObservableValue nationalCodeObserveValue = PojoObservables.observeValue(player, "nationalCode");
		IObservableValue eloObserveWidget = SWTObservables.observeText(m_playerDetails.getEloText(), SWT.None);
		IObservableValue eloObserveValue = PojoObservables.observeValue(player, "elo");
		IObservableValue nationalEloObserveWidget = SWTObservables.observeText(m_playerDetails.getNationalEloText(), SWT.None);
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

	public ch.jester.model.Player getPlayer() {
		return player;
	}

	public void setPlayer(ch.jester.model.Player newPlayer) {
		setPlayer(newPlayer, true);
	}

	public void setPlayer(ch.jester.model.Player newPlayer, boolean update) {
		player = newPlayer;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (player != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}
}