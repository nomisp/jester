package ch.jester.ui.player.editor.ctrl;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.model.Player;
import ch.jester.ui.forms.PlayerFormPage;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import ch.jester.model.Club;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.core.databinding.observable.list.WritableList;



public class PlayerDetailsController {
	private PlayerFormPage mPlayerDetail;
	private DataBindingContext mBindingContext;
	private Player player = new Player();
	private DirtyManager mDm;
	public PlayerDetailsController(PlayerFormPage playerDetails) {
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
		IObservableValue mPlayerDetailgetTxtAgeObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getTxtAge(), SWT.Modify);
		IObservableValue playerAgeObserveValue = BeansObservables.observeValue(player, "age");
		bindingContext.bindValue(mPlayerDetailgetTxtAgeObserveTextObserveWidget, playerAgeObserveValue, null, null);
		//
		IObservableValue cityObserveWidget = SWTObservables.observeText(mPlayerDetail.getCityText(), SWT.Modify);
		IObservableValue cityObserveValue = BeansObservables.observeValue(player, "city");
		bindingContext.bindValue(cityObserveWidget, cityObserveValue, null, null);
		//
		IObservableValue nationObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationText(), SWT.Modify);
		IObservableValue nationObserveValue = BeansObservables.observeValue(player, "nation");
		bindingContext.bindValue(nationObserveWidget, nationObserveValue, null, null);
		//
		IObservableValue mPlayerDetailgetFideCodeTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getFideCodeText(), SWT.Modify);
		IObservableValue playerFideCodeObserveValue = BeansObservables.observeValue(player, "fideCode");
		bindingContext.bindValue(mPlayerDetailgetFideCodeTextObserveTextObserveWidget, playerFideCodeObserveValue, null, null);
		//
		IObservableValue mPlayerDetailgetNationalCodeTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalCodeText(), SWT.Modify);
		IObservableValue playerNationalCodeObserveValue = BeansObservables.observeValue(player, "nationalCode");
		bindingContext.bindValue(mPlayerDetailgetNationalCodeTextObserveTextObserveWidget, playerNationalCodeObserveValue, null, null);
		//
		IObservableValue mPlayerDetailgetEloTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getEloText(), SWT.Modify);
		IObservableValue playerEloObserveValue = BeansObservables.observeValue(player, "elo");
		bindingContext.bindValue(mPlayerDetailgetEloTextObserveTextObserveWidget, playerEloObserveValue, null, null);
		//
		IObservableValue mPlayerDetailgetNationalEloTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalEloText(), SWT.Modify);
		IObservableValue playerNationalEloObserveValue = BeansObservables.observeValue(player, "nationalElo");
		bindingContext.bindValue(mPlayerDetailgetNationalEloTextObserveTextObserveWidget, playerNationalEloObserveValue, null, null);
		//
		IObservableValue mPlayerDetailgetTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getText(), SWT.Modify);
		IObservableValue playerEstimatedEloObserveValue = BeansObservables.observeValue(player, "estimatedElo");
		bindingContext.bindValue(mPlayerDetailgetTextObserveTextObserveWidget, playerEstimatedEloObserveValue, null, null);
		//
		return bindingContext;
	}
}