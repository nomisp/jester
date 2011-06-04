package ch.jester.common.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Hilfsklasse für das ExceptionHandling, da Exceptions möglicherweise gewrapped werden.
 * @author Peter
 *
 */
public class ExceptionUtility {

	/**
	 * Liefert die echte Exception aus einer <code>UndeclaredThrowableException</code>
	 * @param e geworfene Exception
	 * @return echte Exception falls es sich bei der übergebenen Exception um eine <code>UndeclaredThrowableException</code>
	 * sonst wird die übergebene Exception wieder zurückgegeben.
	 */
	public static Throwable getRealException(Exception e) {
		if (e instanceof UndeclaredThrowableException) {
			UndeclaredThrowableException undeclaredThrowableException = (UndeclaredThrowableException)e;
			InvocationTargetException undeclaredThrowable = (InvocationTargetException)undeclaredThrowableException.getUndeclaredThrowable();
			Throwable targetException = undeclaredThrowable.getTargetException();
			return targetException;
		}
		return e;
	}
}
