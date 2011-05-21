package ch.jester.ui.tournament.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.jester.common.ui.editor.IEditorDaoInputAccess;
import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.model.Category;
import ch.jester.ui.tournament.editors.WirePlayerEditor;

public class AddPlayerHandler extends AbstractCommandHandler implements IHandler {

	@Override
	public Object executeInternal(ExecutionEvent event) {
		//alternative(event);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		ISelection selection = getSelection();
		if (!selection.isEmpty()) {
			IStructuredSelection sSelection = (IStructuredSelection)selection;
			if (sSelection.size() == 1 && sSelection.getFirstElement() instanceof Category) {
				MessageDialog.openInformation(
						HandlerUtil.getActiveWorkbenchWindow(event).getShell(), 
						"AddPlayer", "Adding Player to " + ((Category)sSelection.getFirstElement()).getDescription());
				Category cat = (Category)sSelection.getFirstElement();
				CategoryEditorInput input = new CategoryEditorInput(cat.getId());
				try {
					page.openEditor(input, WirePlayerEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**Alternative zu obigen Aufruf, da ja der EditorService im Activator schon an die
	 * Cat gebunden ist, gehts auch einfacher
	 * @param event
	 */
	private void alternative(ExecutionEvent event) {
		Category cat = mSelUtility.getFirstSelectedAs(Category.class);
		if(cat!=null){
			getServiceUtil().getService(IEditorService.class).openEditor(cat);
		}
		
	}

	public class CategoryEditorInput implements IEditorDaoInputAccess {
		
		private final Integer id;
		
		public CategoryEditorInput(Integer id) {
			this.id = id;
		}
	    
		public int getId() {
	        return id;
	    }
	    
		@Override
		public Object getAdapter(Class adapter) {
			return null;
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return null;
		}

		@Override
		public String getName() {
			return String.valueOf(id);
		}

		@Override
		public IPersistableElement getPersistable() {
			return null;
		}

		@Override
		public String getToolTipText() {
			return "Adds players to a Category";
		}
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + id;
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        CategoryEditorInput other = (CategoryEditorInput) obj;
	        if (id != other.id)
	            return false;
	        return true;
	    }

		@Override
		public Object getInput() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setInput(Object pT) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isAlreadyDirty() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
