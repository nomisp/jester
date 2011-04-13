package ch.jester.importmanagerservice.impl.internal;

import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.common.importer.AbstractImportHandler;
import ch.jester.commonservices.api.importer.IImportHandler;

/**
 *  Agiert als Proxy für Services, welche am ExensionPoint registriert wurden.
 *  --> LazyLoading des Services
 *
 */
public class ExtensionPointImportHandler extends AbstractImportHandler {
	IConfigurationElement mElement;
	IImportHandler mImportHandler;
	public ExtensionPointImportHandler(IConfigurationElement iConfigurationElement) {
		mElement=iConfigurationElement;
	}

	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		if(mImportHandler==null){
			try {
				mImportHandler = (IImportHandler) mElement.createExecutableExtension("class");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mImportHandler.handleImport(pInputStream, pMonitor);
	}

	@Override
	public String getProperty(String pPropertyKey) {
		return mElement.getAttribute(pPropertyKey);
	}

}
