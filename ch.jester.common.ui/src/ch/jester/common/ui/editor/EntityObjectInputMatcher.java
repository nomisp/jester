package ch.jester.common.ui.editor;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import ch.jester.commonservices.api.persistency.IEntityObject;

public class EntityObjectInputMatcher implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		try {
			IEntityObject newInputEntity = tryGetEntityObject(input);
			IEntityObject existingEntityObject = tryGetEntityObject(editorRef.getEditorInput());
			if(newInputEntity.equals(existingEntityObject)){
				if(newInputEntity.isUnsafed()){
					return false;
				}
				return true;
			}else{
				return false;
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

	
	public IEntityObject tryGetEntityObject(IEditorInput input){
		if(input instanceof IEditorInputAccess<?>){
			Object object = ((IEditorInputAccess)input).getInput();
			if(!(object instanceof IEntityObject)){
				return null;
			}else{
				return (IEntityObject) object;
			}
		}
		return null;
	}
}
