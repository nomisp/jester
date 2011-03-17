package ch.jester.common.utility;

public class ThreadContext {
	ClassLoader oldL;
	public void save(){
		oldL = Thread.currentThread().getContextClassLoader();
	}
	public void set(ClassLoader cl){
		save();
		Thread.currentThread().setContextClassLoader(cl);
	}
	public void restore(){
		Thread.currentThread().setContextClassLoader(oldL);
	}
}
