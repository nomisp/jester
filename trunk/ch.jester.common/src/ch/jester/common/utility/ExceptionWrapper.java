package ch.jester.common.utility;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper um Exception<br>
 * - Dirkter Zugriff auf RootCause<br>
 * - oder auf die im Konstruktor übergebene Exception Klasse
 *
 */
public class ExceptionWrapper{
	Throwable mEx;
	Throwable mRoot;
	Throwable mT;
	Class<?> mClz;
	 ExceptionWrapper(Throwable pException, Class<?> pExceptionClass){
		mEx = pException;
		mClz=pExceptionClass;
	}
	 ExceptionWrapper(Throwable pException){
		mEx=pException;
	}
	/**
	 * Versucht die im Konstruktor übergebene Klasse zu finden und zurückzugeben.
	 * Sonst gibt sie den RootCause zurück
	 */
	public Throwable getThrowable() {
		Throwable current = mEx;
		List<Exception> exList = new ArrayList<Exception>();
		if(mClz==null){
			return getRootThrowable();
		}
		for(;;){
			if(mClz.isAssignableFrom(current.getClass())){
				exList.add((Exception) current);
			}
			if(current.getCause()==null){
				break;
			}
			current = current.getCause();
		}
		mT = exList.get(exList.size()-1);
		return mT;
	}

	public String getThrowableMessage(){
		return getMessage(getThrowable());
	}
	public String getRootThrowableMessage(){
		return getMessage(getRootThrowable());
	}
	private String getMessage(Throwable t){
		if(t.getLocalizedMessage()!=null&&t.getLocalizedMessage().length()>0){
			return t.getLocalizedMessage();
		}
		return t.getMessage();
	}
	/**
	 * @return den Root Cause
	 */
	public Throwable getRootThrowable(){
		Throwable current = mEx;
		for(;;){
			if(current instanceof InvocationTargetException){
				current =((InvocationTargetException)current).getTargetException();
			}
			if(current.getCause()==null){
				mRoot = current;
				break;
			}
			current = current.getCause();
			
		}
		return mRoot;
		
	}
}