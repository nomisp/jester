package ch.jester.common.ui.utility;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.utility.ServiceUtility;

/**
 * Handler für UI Events, welcher diverse Hilfsmethoden implementiert
 *
 */
public abstract class AbstractEventHandler extends AbstractHandler {
	protected ExecutionEvent mEvent;
	protected SelectionUtility mSelUtility;
	protected ServiceUtility mServiceUtility;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		mEvent = event;
		mSelUtility = new SelectionUtility(getSelection());
		mServiceUtility = new ServiceUtility(FrameworkUtil.getBundle(
				AbstractEventHandler.this.getClass()).getBundleContext());
		return executeInternal(event);
	}

	/**
	 * Implementiert die eigentliche Logik in der Subklasse
	 * 
	 * @param event
	 * @return
	 */
	public abstract Object executeInternal(ExecutionEvent event);

	/**
	 * Gibt die aktuelle Selection falls vorhanden zurück
	 * 
	 * @return die Selektion oder null
	 */
	public ISelection getSelection() {
		return HandlerUtil.getCurrentSelection(mEvent);
		/*return HandlerUtil.getActiveWorkbenchWindow(mEvent).getActivePage()
				.getSelection();*/
	}

	/**
	 * Die Shell
	 * 
	 * @return
	 */
	public Shell getShell() {
		return HandlerUtil.getActiveWorkbenchWindow(mEvent).getShell();
	}

	/**
	 * @see SelectionUtility#isStructuredSelection()
	 * @return
	 */
	public boolean isIStructuredSelection() {
		return mSelUtility.isIStructuredSelection();
	}

	/**
	 * @see SelectionUtility#getFirstSelected()
	 * @return
	 */
	public Object getFirstSelected() {
		return mSelUtility.getFirstSelected();
	}

	/** @see org.eclipse.ui.IPartService#getActivePart()
	 * @return
	 */
	public IWorkbenchPart getActivePart() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart();
	}

	/**
	 * @see org.eclipse.ui.handlers.HandlerUtil#getActivePart(ExecutionEvent)
	 * @return
	 */
	public IWorkbenchPart getActivePartFromEvent() {
		return HandlerUtil.getActivePart(mEvent);
	}

	/**
	 * @see SelectionUtility.getFirstSelectedAs
	 * @return
	 */
	public <T> T getFirstSelectedAs(Class<T> clz) {
		return mSelUtility.getFirstSelectedAs(clz);
	}

	/**
	 * Gibt das ServiceUtility - mit dem BundleContext des aktuellen Bundles -
	 * zurück
	 * @see ServiceUtility
	 * @return Das ServiceUtility
	 */
	public ServiceUtility getServiceUtil() {
		return mServiceUtility;
	}
	
	/**
	 * Setzt das übergebene Objekt als Selection beim SelectionProvider
	 * @param pObject
	 */
	public void setSelection(Object pObject){
		getActivePart().getSite().getSelectionProvider().setSelection(new StructuredSelection(pObject));
	}
	
	public int getSelectionCount(){
		return mSelUtility.getSelectionCount();
	}
}
