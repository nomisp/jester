package ch.jester.common.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import ch.jester.commonservices.api.importer.ILink;

public class Link implements ILink{
	private String mText;
	private String mURL;
	private String mRoot;
	public Link(String pText, String pURL){
		mText=pText;
		mURL=pURL;
	}
	/* (non-Javadoc)
	 * @see ch.jester.common.web.ILink#getText()
	 */
	@Override
	public String getText(){
		return mText;
	}
	/* (non-Javadoc)
	 * @see ch.jester.common.web.ILink#getURL()
	 */
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
	/* (non-Javadoc)
	 * @see ch.jester.common.web.ILink#setRoot(java.lang.String)
	 */
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
	/* (non-Javadoc)
	 * @see ch.jester.common.web.ILink#download(java.lang.String)
	 */
	@Override
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