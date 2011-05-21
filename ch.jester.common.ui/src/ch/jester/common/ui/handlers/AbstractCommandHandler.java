package ch.jester.common.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Handler für UI Events, welcher diverse Hilfsmethoden implementiert
 *
 */
public abstract class AbstractCommandHandler extends AbstractHandler {
	protected ExecutionEvent mEvent;
	protected SelectionUtility mSelUtility;
	protected ServiceUtility mServiceUtility;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		mEvent = event;
		mSelUtility = new SelectionUtility(getSelection());
		mServiceUtility = new ServiceUtility(FrameworkUtil.getBundle(
				AbstractCommandHandler.this.getClass()).getBundleContext());
		return executeInternal(event);
	}

	/**
	 * Implementiert die eigentliche Logik in der Subklasse.
	 * <br>
	 * {@link AbstractCommandHandler#mEvent}
	 * {@link AbstractCommandHandler#mSelUtility},
	 * {@link AbstractCommandHandler#mServiceUtility},
	 * sind initialisiert (mit dem aktuellen event) und von Subklassen benutzbar
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
		setSelection(getActivePart(),pObject);
	}
	
	public void setSelection(IWorkbenchPart pWb, Object pObject){
		pWb.getSite().getSelectionProvider().setSelection(new StructuredSelection(pObject));
	}
	
	private void setSelection0(IWorkbenchPart pWb, ISelection pSelection){
		pWb.getSite().getSelectionProvider().setSelection(pSelection);
	}
	
	public IViewPart getView(String pViewId){
		return UIUtility.getActiveWorkbenchWindow().getActivePage().findView(pViewId);
	}
	
	public void setSelection(String pPartId, Object pObject){
		
		IViewPart viewpart = getView(pPartId);
		if(pObject instanceof ISelection){
			setSelection0(viewpart, (ISelection)pObject);
		}else{
			setSelection(viewpart, pObject);
		}

	}

	public void openEditor(Object pObject){
		getServiceUtil().getService(IEditorService.class).openEditor(pObject);
	}
	public void openEditor(Object pObject, String pEditorId){
		getServiceUtil().getService(IEditorService.class).openEditor(pObject,pEditorId);
	}
	
	
	public int getSelectionCount(){
		return mSelUtility.getSelectionCount();
	}
}
