package ch.jester.commonservices.impl.io;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.io.ITempFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.exceptions.ProcessingException;

public class DefaultTempFileManager implements ITempFileManager {
	private ILogger mLogger;
	private File tmpRoot;
	private HashMap<String, File> mFileMap = new HashMap<String, File>();
	@Override
	public void start(ComponentContext pComponentContext) {
		Location workingDir = Platform.getInstanceLocation();
		String dir =  workingDir.getURL().getFile() + "/temp";
		setRootTempDirectory(dir);
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		clearTempDirectories();
	}

	@Override
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(getClass());
		mLogger.info("TempFileManager started");
	}

	@Override
	public void unbind(ILoggerFactory pT) {

	}

	@Override
	public File createTempFile() {
		return createTempFile(true);
	}

	@Override
	public File createTempFile(boolean deleteOnExit) {
		return createUUIDTmpFile(null, null, deleteOnExit);
	}

	@Override
	public File createTempFile(String pName) throws ProcessingException {
		return createTempFile(pName, true);
	}

	@Override
	public File createTempFile(String pName, boolean deleteOnExit)
			throws ProcessingException {
		return createUUIDTmpFile(pName, null, deleteOnExit);
	}
	@Override
	public File createTempFileWithExtension(String pExtension) {
		return createTempFileWithExtension(pExtension, true);
	}

	@Override
	public File createTempFileWithExtension(String pExtension,
			boolean deleteOnExit) {
		return createUUIDTmpFile(null, pExtension, deleteOnExit);
	}

	@Override
	public File getRootTempDirectory() {
		return tmpRoot;
	}

	@Override
	public void setRootTempDirectory(String pRootDirectory) {
		clearTempDirectories();		
		File newRoot = new File(pRootDirectory);
		if(!newRoot.exists()){
			newRoot.mkdir();
		}
		tmpRoot = newRoot;
		mLogger.debug("New tempfolder is: "+tmpRoot.getAbsolutePath());
	}

	@Override
	public void clearTempDirectories() {
		Iterator<String> it = mFileMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			File f = mFileMap.get(key);
			boolean result = f.delete();
			mLogger.debug("Deleting: "+f.getName()+" successful: "+result);
		}
		
	}

	private File createUUIDTmpFile(String pName, String pExtension, boolean deleteOnExit) {
		String name = null;
		String extension = null;
		String id = UUID.randomUUID().toString();
		if(pName!=null){
			name = pName;
		}else{
			name  = id;
		}
		if(pExtension!=null&&!name.contains(".")){
			extension="."+pExtension.replaceAll("\\.", "");
		}else{
			extension="";
		}
		
		File f = new File(tmpRoot+"/"+name+extension);
		if(deleteOnExit){
			mFileMap.put(id, f);
		}
		return f;
	}

}
