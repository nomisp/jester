package ch.jester.common.utility;

public class CallerUtility {
	/**
	 * 
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
		private String mClass, mMethod;

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
