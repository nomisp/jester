package ch.jester.common.utility;

/**
 * Utility Klasse um die Caller Methode festzustellen
 *
 */
public class CallerUtility {
	/**
	 * Den Caller des entsprechenden Level suchen.
	 * @param pCallerLevel
	 * @return den Caller des Levels pCallerLevel
	 */
	public static Caller getCaller(int pCallerLevel) {
		Throwable throwable = new Throwable();
		StackTraceElement[] stacktrace = throwable.getStackTrace();
		return new Caller(stacktrace[pCallerLevel].getClassName(),
				stacktrace[pCallerLevel].getMethodName());
	}

	/**
	 * Caller des Levels 2
	 * @return den Caller des Levels 2
	 */
	public static Caller getCaller() {
		return getCaller(2);
	}

	/**
	 * Wrapper für Caller Class/Method
	 * 
	 */
	public static class Caller {
		private String mClass;
		private String mMethod;

		Caller(String pClass, String pMethod) {
			mClass = pClass;
			mMethod = pMethod;
		}

		/**
		 * @return Die Caller Classs
		 */
		public String getCallerClass() {
			return mClass;
		}

		/**
		 * @return Die Caller Methode
		 */
		public String getCallerMethod() {
			return mMethod;
		}
	}
}
