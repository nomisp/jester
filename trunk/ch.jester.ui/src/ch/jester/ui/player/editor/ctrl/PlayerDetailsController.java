package ch.jester.ui.player.editor.ctrl;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.editorutilities.DirtyManager;
import ch.jester.common.ui.utility.UIFieldConstraints;
import ch.jester.model.Club;
import ch.jester.model.Player;
import ch.jester.model.Title;
import ch.jester.ui.forms.PlayerFormPage;



public class PlayerDetailsController {
	private PlayerFormPage mPlayerDetail;
	private DataBindingContext mBindingContext;
	private Player player = new Player();
	private DirtyManager mDm;
	private UIFieldConstraints fc = new UIFieldConstraints(Player.class);
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
	
	public Title[] getTitles(){
		return Title.values();
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
		UpdateValueStrategy strategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST);
		UpdateValueStrategy strategy_1 = new UpdateValueStrategy();
		bindingContext.bindValue(mPlayerDetailgetTxtAgeObserveTextObserveWidget, playerAgeObserveValue, strategy, strategy_1);
		//
		IObservableValue cityObserveWidget = SWTObservables.observeText(mPlayerDetail.getCityText(), SWT.Modify);
		IObservableValue cityObserveValue = BeansObservables.observeValue(player, "city");
		bindingContext.bindValue(cityObserveWidget, cityObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue nationObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationText(), SWT.Modify);
		IObservableValue nationObserveValue = BeansObservables.observeValue(player, "nation");
		bindingContext.bindValue(nationObserveWidget, nationObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetFideCodeTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getFideCodeText(), SWT.Modify);
		IObservableValue playerFideCodeObserveValue = BeansObservables.observeValue(player, "fideCode");
		bindingContext.bindValue(mPlayerDetailgetFideCodeTextObserveTextObserveWidget, playerFideCodeObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetNationalCodeTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalCodeText(), SWT.Modify);
		IObservableValue playerNationalCodeObserveValue = BeansObservables.observeValue(player, "nationalCode");
		bindingContext.bindValue(mPlayerDetailgetNationalCodeTextObserveTextObserveWidget, playerNationalCodeObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetEloTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getEloText(), SWT.Modify);
		IObservableValue playerEloObserveValue = BeansObservables.observeValue(player, "elo");
		bindingContext.bindValue(mPlayerDetailgetEloTextObserveTextObserveWidget, playerEloObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetNationalEloTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getNationalEloText(), SWT.Modify);
		IObservableValue playerNationalEloObserveValue = BeansObservables.observeValue(player, "nationalElo");
		bindingContext.bindValue(mPlayerDetailgetNationalEloTextObserveTextObserveWidget, playerNationalEloObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetTextObserveTextObserveWidget = SWTObservables.observeText(mPlayerDetail.getText(), SWT.Modify);
		IObservableValue playerEstimatedEloObserveValue = BeansObservables.observeValue(player, "estimatedElo");
		bindingContext.bindValue(mPlayerDetailgetTextObserveTextObserveWidget, playerEstimatedEloObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		IObservableValue mPlayerDetailgetTxtTitleObserveSingleSelection = ViewersObservables.observeSingleSelection(mPlayerDetail.getTxtTitle());
		IObservableValue playerTitleObserveValue = BeansObservables.observeValue(player, "title");
		bindingContext.bindValue(mPlayerDetailgetTxtTitleObserveSingleSelection, playerTitleObserveValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST), null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		mPlayerDetail.getListViewer().setContentProvider(listContentProvider);
		//
		IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), Club.class, new String[]{"name", "id"});
		mPlayerDetail.getListViewer().setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		//
		WritableList writableList = new WritableList(player.getClubs(), Club.class);
		mPlayerDetail.getListViewer().setInput(writableList);
		//
		return bindingContext;
	}

	public void addConstraint(Text pText, String pProperty) {
		fc.addConstraint(pText, pProperty);
	}
	
	public boolean isValid(){
		return !fc.hasErrors();
	}
}