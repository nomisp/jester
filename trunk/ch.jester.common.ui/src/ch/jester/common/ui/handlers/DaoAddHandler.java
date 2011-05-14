package ch.jester.common.ui.handlers;

import org.eclipse.ui.IViewPart;

import ch.jester.common.ui.databinding.DaoController;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;


public abstract class DaoAddHandler extends AbstractCommandHandler{
	public Object executeInternal(org.eclipse.core.commands.ExecutionEvent event) {
		IDaoObject object = getNewDaoObject();
		
		if(object == null) {return null;}
		
		//holen der Target View, um an einen allfälligen controller zu gelangen
		IViewPart view = getView(getTargetViewId());
		
		//View noch nicht initialisiert?
		//dann benutzen wir eben direkt den Persister, und machen sonst nichts
		if(view==null){
			IDaoServiceFactory factory = this.mServiceUtility.getService(IDaoServiceFactory.class);
			@SuppressWarnings("unchecked")
			IDaoService<IDaoObject> service = (IDaoService<IDaoObject>) factory.getDaoService(object.getClass());
			service.save(object);
			return null;
		}
		
		//Wir gehen davon aus, dass sich die View an der Platform mit einem Controller angemeldet hat,
		//und holen diesen
		@SuppressWarnings("unchecked")
		DaoController<IDaoObject> ctrl =  AdapterUtility.getAdaptedObject(view, DaoController.class);
		
		//hinzufügen vom Objekt
		ctrl.addDaoObject(object);
	
		//selektiert im UI
		setSelection(view, object);

		//öffne Editor
		ctrl.openEditor(object);

		
		return null;
		
		
	};
			
	public abstract String getTargetViewId();
	
	public abstract IDaoObject getNewDaoObject();

}
