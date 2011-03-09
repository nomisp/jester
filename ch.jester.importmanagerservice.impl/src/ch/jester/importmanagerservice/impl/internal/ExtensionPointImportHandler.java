package ch.jester.importmanagerservice.impl.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import ch.jester.common.importer.AbstractImportHandler;
import ch.jester.commonservices.api.importer.IImportHandler;

public class ExtensionPointImportHandler extends AbstractImportHandler {
	IConfigurationElement mElement;
	IImportHandler mImportHandler;
	public ExtensionPointImportHandler(IConfigurationElement iConfigurationElement) {
		mElement=iConfigurationElement;
	}

	@Override
	public Object handleImport(Object o) {
		if(mImportHandler==null){
			try {
				mImportHandler = (IImportHandler) mElement.createExecutableExtension("class");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mImportHandler.handleImport(o);
	}

}
