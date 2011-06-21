package ch.jester.commonservices.impl.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.UUID;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.service.component.ComponentContext;

import ch.jester.common.components.ComponentAdapter;
import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILoggerFactory;
import ch.jester.commonservices.exceptions.ProcessingException;

public class DefaultFileManager extends InjectedLogFactoryComponentAdapter<Void> implements IFileManager {
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

/*	@Override
	public void bind(ILoggerFactory pT) {
		mLogger = pT.getLogger(getClass());
		mLogger.info("TempFileManager started");
	}*/

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
		getLogger().debug("New tempfolder is: "+tmpRoot.getAbsolutePath());
	}

	@Override
	public void clearTempDirectories() {
		Iterator<String> it = mFileMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			File f = mFileMap.get(key);
			boolean result = f.delete();
			getLogger().debug("Deleting: "+f.getName()+" successful: "+result);
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

	@Override
	public void copyFile(File f1, File f2) {
		try {
			toFile(new FileInputStream(f1), f2);
		} catch (ProcessingException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new ProcessingException(e);
		}

		 
}

	@Override
	public void toFile(InputStream inStream, File dest)
			throws ProcessingException {
		  InputStream in;
			try {
				  in = new BufferedInputStream(inStream);
				  OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

				  byte[] buf = new byte[1024*1024*8];
				  int len;
				  while ((len = in.read(buf)) > 0){
				  out.write(buf, 0, len);
				  }
				  in.close();
				  out.close();
			} catch (Exception e) {
				throw new ProcessingException(e);
			}
		
	}

	@Override
	public File getWorkingDirectory() {
		Location workingDir = Platform.getInstanceLocation();
		String dir =  workingDir.getURL().getFile();
		return new File(dir);
	}


	@Override
	public File getFolderInWorkingDirectory(String dirName) {
		File newFolder = new File(getWorkingDirectory()+"/"+dirName);
		if(!newFolder.exists()){
			boolean success = newFolder.mkdir();
			if(!success){
				StringTokenizer tokenizer = new StringTokenizer(dirName,"/");
				String path ="";
				while(tokenizer.hasMoreElements()){
					String subdir = tokenizer.nextToken();
					path=path+subdir+"/";
					new File(getWorkingDirectory()+"/"+path).mkdir();
					
				}
				
				
				
			}
			
		}
		return newFolder;
	}

	}

