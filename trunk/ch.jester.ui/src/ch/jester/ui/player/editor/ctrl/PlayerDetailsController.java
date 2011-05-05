package ch.jester.ui.player.editor.ctrl;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.view.PlayerDetailsView;

import org.eclipse.core.databinding.UpdateValueStrategy;



public class PlayerDetailsController {
	private PlayerDetailsView mPlayerDetail;
	private DataBindingContext mBindingContext;
	private Player player = new Player();
	private DirtyManager mDm;
	public PlayerDetailsController(PlayerDetailsView playerDetails) {
		mPlayerDetail = playerDetails;
		mDm = playerDetails.getDirtyManager();
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

	public void updateModel(){
		mBindingContext.updateModels();
	}
	public void updateUI(){
		mBindingContext.updateTargets();
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

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue lastNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getLastNameText(), SWT.Modify);
		IObservableValue lastNameObserveValue = BeansObservables.observeValue(player, "lastName");
		bindingContext.bindValue(lastNameObserveWidget, lastNameObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue firstNameObserveWidget = SWTObservables.observeText(mPlayerDetail.getFirstNameText(), SWT.Modify);
		IObservableValue firstNameObserveValue = BeansObservables.observeValue(player, "firstName");
		bindingContext.bindValue(firstNameObserveWidget, firstNameObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
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