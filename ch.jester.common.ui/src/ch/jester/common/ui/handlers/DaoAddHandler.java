package ch.jester.common.ui.handlers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IViewPart;

import ch.jester.common.ui.handlers.api.IHandlerAdd;
import ch.jester.common.ui.handlers.api.IHandlerEditor;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;

/**
 * Abstrakte Klasse um ein Objekt hinzuzufügen.<br>
 * <br>
 */
public abstract class DaoAddHandler extends AbstractCommandHandler {
	/**
	 * Regeln: <br>
	 * 1) falls es keine Aktive View gibt, wird das Objekt direkt via
	 * {@link IDaoService} auf die DB geschrieben <br>
	 * 2) fallse es eine aktive View gibt: <br>
	 * 2.1) wird die View nach einem {@link IAdaptable} für {@link IHandlerAdd}
	 * und {@link IHandlerEditor} gefragt <br>
	 * 2.2) das Objekt wird dem {@link IHandlerAdd} übergeben (muss zwingend
	 * regsitriert worden sein)<br>
	 * 2.3) gibt es ein {@link IHandlerEditor} wird darauf die
	 * {@link IHandlerEditor#handleOpenEditor(Object)} aufgerufen
	 */
	public Object executeInternal(org.eclipse.core.commands.ExecutionEvent event) {
		IEntityObject object = getNewEntityObject();

		if (object == null) {
			return null;
		}

		// holen der Target View, um an einen allfälligen controller zu gelangen
		IViewPart view = getView(getTargetViewId());

		// View noch nicht initialisiert?
		// dann benutzen wir eben direkt einen neuen DaoService, und machen
		// sonst nichts
		if (view == null) {
			@SuppressWarnings("unchecked")
			IDaoService<IEntityObject> service = (IDaoService<IEntityObject>) mServiceUtility
					.getDaoServiceByEntity(object.getClass());
			service.save(object);
			return null;
		}

		// Wir gehen davon aus, dass sich die View an der Platform mit einem
		// Controller angemeldet hat,
		// und holen diesen
		@SuppressWarnings("unchecked")
		IHandlerAdd<IEntityObject> addCtrl = AdapterUtility.getAdaptedObject(
				view, IHandlerAdd.class);
		@SuppressWarnings("unchecked")
		IHandlerEditor<IEntityObject> editorCtrl = AdapterUtility
				.getAdaptedObject(view, IHandlerEditor.class);

		// hinzufügen vom Objekt
		addCtrl.handleAdd(object);

		// selektiert im UI
		setSelection(view, object);

		if (editorCtrl != null) {
			// öffne Editor
			editorCtrl.handleOpenEditor(object);
		}

		return null;

	};

	/**
	 * @return einen String welche die TargetView identifiziert.
	 */
	public abstract String getTargetViewId();


	/**
	 * @return ein neues einzufügendes Objekt
	 */
	public abstract IEntityObject getNewEntityObject();

}
