package ch.jester.common.ui.internal;

import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.EditorAccessor;
import ch.jester.common.ui.editor.IEditorInputAccess;
import ch.jester.common.ui.editor.EditorAccessor.EditorAccess;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.UIUtility;

public class EditorService implements IEditorService{
	private HashMap<Class<?>, RegistryEntry> mMap = new HashMap<Class<?>, RegistryEntry>();
	private EditorAccessor accessor = null;
	@Override
	public EditorAccessor.EditorAccess openEditor(Object pInputObject) {
		check();
		RegistryEntry entry = mMap.get(pInputObject.getClass());
		return openEditor(pInputObject, entry.mEditorId);		
	}
	@Override
	public boolean isEditorRegistred(Object pInputObject) {
		return  mMap.get(pInputObject.getClass())!=null;
	}
	private void check() {
		if(accessor==null){
			accessor = new EditorAccessor(UIUtility.getActiveWorkbenchWindow().getActivePage(), this);

		}
		
	}

	@Override
	public EditorAccessor.EditorAccess  openEditor(Object pInputObject, String pEditorId) {
		check();
		
		try {
			EditorAccess access = accessor.new EditorAccess(null, pInputObject, null);
			Assert.isNotNull(access, "Access was null!!!");
			/*if(access.isAlreadyOpen()){
				access.activate();
			}else{*/
				Class<?> editorInputClass = mMap.get(pInputObject.getClass()).mEditorInputClass;
				@SuppressWarnings("unchecked")
				IEditorInputAccess<Object> editorInput = (IEditorInputAccess<Object>) editorInputClass.newInstance();
				editorInput.setInput(pInputObject);
				
				access.openEditor(editorInput, pEditorId);
				
			//}
			return access;
		} catch (PartInitException e1) {

			e1.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		return null;
		
		
		

	}

	@Override
	public void register(Class<?> pObjectClass,
			Class<? extends IEditorInputAccess<?>> pEditorInputClass,
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
				UIUtility.syncExecInUIThread(new Runnable(){

					@Override
					public void run() {
						access.close();
						
					}
					
				});
			
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}




}
