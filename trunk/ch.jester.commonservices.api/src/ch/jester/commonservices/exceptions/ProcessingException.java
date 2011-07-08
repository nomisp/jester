package ch.jester.commonservices.exceptions;

public class ProcessingException extends RuntimeException{
	public ProcessingException(String pMsg){
		super(pMsg);
	}
	public ProcessingException(Throwable pThrow){
		super(pThrow);
	}
	public ProcessingException(String pMessage, Throwable pThrow){
		super(pMessage, pThrow);
	}

}
