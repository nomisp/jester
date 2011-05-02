package ch.jester.commonservices.api.importer;

import java.io.IOException;

public interface ILink {

	public abstract String getText();

	public abstract String getURL();

	public abstract void setRoot(String pAddress);

	public abstract void download(String pTargetFile) throws IOException;

}