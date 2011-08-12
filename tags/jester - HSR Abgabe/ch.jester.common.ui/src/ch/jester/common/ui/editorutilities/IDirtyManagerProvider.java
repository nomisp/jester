package ch.jester.common.ui.editorutilities;

/**
 * Zugriff auf einen DirtyManager
 *
 */
public interface IDirtyManagerProvider {
	/**
	 * Zugriff auf den DirtyManager
	 * @return den DirtyManager
	 */
	public DirtyManager getDirtyManager();
}
