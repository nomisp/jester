package ch.jester.common.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import ch.jester.commonservices.api.persistency.IEntityObject;

public class GenericDaoInputAccess implements IEditorDaoInputAccess<IEntityObject>{
	IEntityObject mInput;
	@Override
	public IEntityObject getInput() {
		return mInput;
	}

	@Override
	public void setInput(IEntityObject pT) {
		mInput = pT;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "name-from-input";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Details";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAlreadyDirty() {
		return mInput.isUnsafed();
	}

}
