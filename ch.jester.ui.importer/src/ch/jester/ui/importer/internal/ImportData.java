package ch.jester.ui.importer.internal;

import ch.jester.commonservices.api.importer.IImportHandlerEntry;

public class ImportData {
	IImportHandlerEntry handlerEntry, webEntry;
	String zipFile;
	String zipEntry;
	
	void reset(){
		handlerEntry = null;
		webEntry=null;
		zipFile=null;
		zipEntry=null;
	}

	public String getSelectedZipFile() {
		return zipFile;
	}

	public IImportHandlerEntry getSelectedHandlerEntry() {
		return handlerEntry;
	}

	public String getSelectedZipEntry() {
		return zipEntry;
	}
}
