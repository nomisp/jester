package ch.jester.commonservices.api.io;

import java.io.File;

import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.exceptions.ProcessingException;

public interface ITempFileManager extends IComponentService<ILoggerFactory>{
	public File createTempFile();
	public File createTempFile(boolean deleteOnExit);
	public File createTempFile(String pName) throws ProcessingException;
	public File createTempFile(String pName, boolean deleteOnExit) throws ProcessingException;
	public File createTempFileWithExtension(String pExtension);
	public File createTempFileWithExtension(String pExtension, boolean deleteOnExit);
	public File getRootTempDirectory();
	public void setRootTempDirectory(String pRootDirectory);
	public void clearTempDirectories();
}
