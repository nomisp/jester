package ch.jester.common.ui.utility;


import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Utilityklasse für UI-Tasks
 *
 */
public class MenuManagerUtility {
	/**Installiert den übergebenen MenuManager beim Control und registriert ihn bei der PartSite.
	 * @param pSite die IWorkbenchPartSite
	 * @param pViewer der entsprechende Viewer
	 * @param pGroup der Groupmarker welcher installiert wird (Deklaration)
	 * @param pMenuManager
	 * @return
	 */
	public static MenuManager installPopUpMenuManager(IWorkbenchPartSite pSite, Viewer pViewer, String pGroup,MenuManager pMenuManager ){
		if(pGroup!=null){
			pMenuManager.add(new GroupMarker(pGroup));
		}
		pSite.registerContextMenu(pMenuManager, pViewer);
		Control control = pViewer.getControl();
		Menu menu0 = pMenuManager.createContextMenu(control);
		control.setMenu(menu0);	
		return pMenuManager;
	}


	/**
	 * Installiert den übergebenen MenuManager beim Control und registriert ihn bei der PartSite.
	 * @see installPopUpMenuManager(IWorkbenchPartSite pSite, Viewer pViewer, String pGroup,MenuManager pMenuManager );
	 * @param pSite
	 * @param pViewer
	 * @param pGroup
	 * @return
	 */
	public static MenuManager installPopUpMenuManager(IWorkbenchPartSite pSite, Viewer pViewer, String pGroup ){
		return MenuManagerUtility.installPopUpMenuManager(pSite, pViewer, pGroup, new MenuManager());
	}
	
	/**
	 * Installiert den übergebenen MenuManager beim Control und registriert ihn bei der PartSite.
	 * @param pSite
	 * @param pViewer
	 * @return
	 */
	public static MenuManager installPopUpMenuManager(IWorkbenchPartSite pSite, Viewer pViewer){
		return MenuManagerUtility.installPopUpMenuManager(pSite, pViewer, null);
	}
}
