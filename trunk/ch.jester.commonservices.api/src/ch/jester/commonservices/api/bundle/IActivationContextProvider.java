package ch.jester.commonservices.api.bundle;

/**
 * Interface für eine Klasse, welche einen Context zur Verfügung stellen will
 * 
 * @param <T>
 */
public interface IActivationContextProvider<T> {
	/**
	 * @return den assoziierten IActivationContext
	 */
	public IActivationContext<T> getActivationContext();
}