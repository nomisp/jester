package ch.jester.commonservices.api.importer;

import java.io.IOException;
import java.util.List;

public interface IWebImportAdapter extends IImportHandler {
	public List<ILink> getLinks()  throws IOException ;
} 
