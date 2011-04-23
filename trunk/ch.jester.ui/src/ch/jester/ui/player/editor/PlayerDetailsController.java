package ch.jester.ui.player.editor;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.model.Player;
import org.eclipse.core.databinding.beans.BeansObservables;



public class PlayerDetailsController {
	private PlayerDetails mPlayerDetail;
	private DataBindingContext mBindingContext;
	private Player player = new Player();
	private DirtyManager mDm = new DirtyManager();
	public PlayerDetailsController(PlayerDetails playerDetails) {
		mPlayerDetail = playerDetails;
		if (player != null) {
			mBindingContext = initDataBindings();
		}
	}
	
	public DirtyManager getDirtyManager(){
		return mDm;
	}
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player newPlayer) {
		setPlayer(newPlayer, true);
	}

	public void setPlayer(Player newPlayer, boolean update) {
		if(player!=null){
			player.removePropertyChangeListener(mDm);
		}
		player = newPlayer;
		player.addPropertyChangeListener(mDm);
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

	public void dispose() {
		player.removePropertyChangeListener(mDm);
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue lastNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getLastNameText(), SWT.Modify);
		IObservableValue lastNameObserveValue = BeansObservables.observeValue(player, "lastName");
		bindingContext.bindValue(lastNameObserveWidget, lastNameObserveValue, null, null);
		//
		IObservableValue firstNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getFirstNameText(), SWT.Modify);
		IObservableValue firstNameObserveValue = BeansObservables.observeValue(player, "firstName");
		bindingContext.bindValue(firstNameObserveWidget, firstNameObserveValue, null, null);
		//
		IObservableValue cityObserveWidget = SWTObservables.observeText(mPlayerDetail.getCityText(), SWT.Modify);
		IObservableValue cityObserveValue = BeansObservables.observeValue(player, "city");
		bindingContext.bindValue(cityObserveWidget, cityObserveValue, null, null);
		//
		IObservableValue nationObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationText(), SWT.Modify);
		IObservableValue nationObserveValue = BeansObservables.observeValue(player, "nation");
		bindingContext.bindValue(nationObserveWidget, nationObserveValue, null, null);
		//
		IObservableValue fideCodeObserveWidget = SWTObservables.observeText(mPlayerDetail.getFideCodeText(), SWT.Modify);
		IObservableValue fideCodeObserveValue = BeansObservables.observeValue(player, "fideCode");
		bindingContext.bindValue(fideCodeObserveWidget, fideCodeObserveValue, null, null);
		//
		IObservableValue nationalCodeObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalCodeText(), SWT.Modify);
		IObservableValue nationalCodeObserveValue = BeansObservables.observeValue(player, "nationalCode");
		bindingContext.bindValue(nationalCodeObserveWidget, nationalCodeObserveValue, null, null);
		//
		IObservableValue eloObserveWidget = SWTObservables.observeText(mPlayerDetail.getEloText(), SWT.Modify);
		IObservableValue eloObserveValue = BeansObservables.observeValue(player, "elo");
		bindingContext.bindValue(eloObserveWidget, eloObserveValue, null, null);
		//
		IObservableValue nationalEloObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalEloText(), SWT.Modify);
		IObservableValue nationalEloObserveValue = BeansObservables.observeValue(player, "nationalElo");
		bindingContext.bindValue(nationalEloObserveWidget, nationalEloObserveValue, null, null);
		//
		return bindingContext;
	}
}