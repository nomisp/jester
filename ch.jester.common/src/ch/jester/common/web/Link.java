package ch.jester.common.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import ch.jester.commonservices.api.importer.ILink;

/**
 * Defaultimplementation eines Links
 *
 */
public class Link implements ILink{
	private String mText;
	private String mURL;
	private String mRoot;
	public Link(String pText, String pURL){
		mText=pText;
		mURL=pURL;
	}
	@Override
	public String getText(){
		return mText;
	}
	@Override
	public String getURL(){
		if(isRelative()){
			return mRoot+mURL;
		}
		return mURL;
	}
	public String toString(){
		return mText;
	}
	@Override
	public void setRoot(String pAddress){
		mRoot=pAddress;
		
		if(mRoot!=null&&!mRoot.endsWith("/")){
			mRoot = mRoot+"/";
		}
	}
	private boolean isRelative(){
		if(mURL.toLowerCase().startsWith("http:")){
			return false;
		}
		return true;
	}
	@Override
	public void download(String pTargetFile) throws IOException{
		FileOutputStream out = new FileOutputStream(pTargetFile);
		HttpURLConnection uc = null;
		InputStream in = null;
		try{
		uc= HTTPFactory.connect(getURL());
		uc.connect();
		
		in = uc.getInputStream();
	    byte[] buf = new byte[1024 * 1024 * 4]; 
	    int bytesRead;
	    while ((bytesRead = in.read(buf)) != -1) {
	      out.write(buf, 0, bytesRead);
	      out.flush();
	    }
    //	out.close();
    //	in.close();
    //	uc.disconnect();
	    
		}catch(IOException e){
	    	throw e;


		}finally{
			if(uc!=null){
				uc.disconnect();
			}
			if(in!=null){
				in.close();
			}
			if(out!=null){
				out.flush();
				out.close();
			}
		}
	}
}