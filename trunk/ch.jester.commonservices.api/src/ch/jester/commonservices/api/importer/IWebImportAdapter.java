package ch.jester.commonservices.api.importer;

import java.io.IOException;
import java.util.List;

public interface IWebImportAdapter extends IImportHandler<Object> {
	public List<ILink> getLinks()  throws IOException ;
} 
