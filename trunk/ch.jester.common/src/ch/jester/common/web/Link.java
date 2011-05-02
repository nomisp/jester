package ch.jester.common.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Link{
	private String mText, mURL, mRoot;
	public Link(String pText, String pURL){
		mText=pText;
		mURL=pURL;
	}
	public String getText(){
		return mText;
	}
	public String getURL(){
		if(isRelative()){
			return mRoot+mURL;
		}
		return mURL;
	}
	public String toString(){
		return mText;
	}
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
	public void download(String pTargetFile) throws IOException{
		HttpURLConnection uc = HTTPFactory.connect(getURL());
		uc.connect();
		FileOutputStream out = new FileOutputStream(pTargetFile);
		InputStream in = uc.getInputStream();
	    byte[] buf = new byte[1024 * 1024 * 4]; 
	    int bytesRead;
	    while ((bytesRead = in.read(buf)) != -1) {
	      out.write(buf, 0, bytesRead);
	      out.flush();
	    }
	    out.close();
		in.close();
		uc.disconnect();
	}
}