package ch.jester.common.ui.internal;

import java.util.HashMap;

import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.EditorAccessor;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.ui.utility.EditorAccessor.EditorAccess;
import ch.jester.common.ui.utility.IEditorInputAccess;

public class EditorService implements IEditorService{
	private HashMap<Class<?>, RegistryEntry> mMap = new HashMap<Class<?>, RegistryEntry>();
	private EditorAccessor accessor = null;
	@Override
	public void openEditor(Object pInputObject) {
		check();
		RegistryEntry entry = mMap.get(pInputObject.getClass());
		openEditor(pInputObject, entry.mEditorId);		
	}

	private void check() {
		if(accessor==null){
			accessor = new EditorAccessor(UIUtility.getActiveWorkbenchWindow().getActivePage());

		}
		
	}

	@Override
	public void openEditor(Object pInputObject, String pEditorId) {
		check();
		try {
			EditorAccess access = accessor.lookup(pInputObject);
			if(access.isAlreadyOpen()){
				access.activate();
			}else{
				Class<?> editorInputClass = mMap.get(pInputObject.getClass()).mEditorInputClass;
				IEditorInputAccess editorInput = (IEditorInputAccess) editorInputClass.newInstance();
				editorInput.setInput(pInputObject);
				
				access.openEditor(editorInput, pEditorId);
				
			}
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

	@Override
	public void register(Class<?> pObjectClass, Class<? extends IEditorInputAccess<?>> pEditorInputClass,
			String pEditorId) {
		RegistryEntry entry = new RegistryEntry(pObjectClass, pEditorInputClass, pEditorId);
		mMap.put(pObjectClass, entry);
	}

	class RegistryEntry{
		protected Class<?> mObjectClass;
		protected Class<? extends IEditorInputAccess<?>> mEditorInputClass;
		protected String mEditorId;
		public RegistryEntry(Class<?> c1, Class<? extends IEditorInputAccess<?>> c2, String pEditorId) {
			mObjectClass=c1;
			mEditorInputClass=c2;
			mEditorId=pEditorId;
		}
	}

	@Override
	public void closeEditor(Object pInputObject) {
		check();
		try {
			final EditorAccess access = accessor.lookup(pInputObject);
			if(access.isAlreadyOpen()){
				UIUtility.executeInUIThread(new Runnable(){

					@Override
					public void run() {
						access.close();
						
					}
					
				});
			
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
