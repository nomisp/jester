package ch.jester.commonservices.exceptions;

/**
 * Standard Exception
 *
 */
public class ProcessingException extends RuntimeException{
	private static final long serialVersionUID = -2554670025482626928L;
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
