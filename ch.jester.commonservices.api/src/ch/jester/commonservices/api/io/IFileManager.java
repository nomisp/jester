package ch.jester.commonservices.api.io;

import java.io.File;
import java.io.InputStream;

import ch.jester.commonservices.exceptions.ProcessingException;

/**
 * Eine ServiceComponente welche die Erzeugung und Verwaltung von Files und Temp-Files im Eclipse WorkingDirectory unterstützt.<br>
 * Temporäre Files, welche mit dem Parameter <code>deleteOnExit =  true</code> erzeugt wurden, werden beim Herunterfahren
 * der Komponente gelöscht.
 */
public interface IFileManager{
	/**
	 * Erzeugt ein File mit einem zufälligen UUID Namen im TempFolder.
	 * @return
	 */
	public File createTempFile();
	/**
	 * Erzeugt ein File mit einem zufälligen UUID Namen im TempFolder.
	 * Falls <code>deleteOnExit == true</code> so wird das File zur Löschung markiert.
	 * @param deleteOnExit
	 * @return
	 */
	public File createTempFile(boolean deleteOnExit);
	/**
	 * Erzeugt ein File mit dem übergebenen Parameter im TempFolder.
	 * @param pName
	 * @return
	 * @throws ProcessingException
	 */
	public File createTempFile(String pName) throws ProcessingException;
	/**
	 * Erzeugt ein File mit dem übergebenen Parameter im TempFolder.
	 * Falls <code>deleteOnExit == true</code> so wird das File zur Löschung markiert.
	 * @param pName
	 * @return
	 * @throws ProcessingException
	 */
	public File createTempFile(String pName, boolean deleteOnExit) throws ProcessingException;
	/**
	 * Erzeugt ein File mit der übergebenen Extension und zufälligem UUID Name im TempFolder.
	 * @param pName
	 * @return
	 * @throws ProcessingException
	 */
	public File createTempFileWithExtension(String pExtension);
	/**
	 * Erzeugt ein File mit der übergebenen Extension und zufälligem UUID Name im TempFolder.
	 * Falls <code>deleteOnExit == true</code> so wird das File zur Löschung markiert.
	 * @param pName
	 * @return
	 * @throws ProcessingException
	 */
	public File createTempFileWithExtension(String pExtension, boolean deleteOnExit);
	/**
	 * Gibt TMP Directory züruck
	 * @return
	 */
	public File getRootTempDirectory();
	
	/**
	 * Setzt TMP Directory
	 * @return
	 */
	public void setRootTempDirectory(String pRootDirectory);
	/**
	 * Wird beim Herunterfahren der Komponente ausgeführt.
	 * Clients sollen diese Methode nicht aufrufen!
	 */
	public void clearTempDirectories();
	/**
	 * Simples File Copy
	 * @param src
	 * @param dest
	 * @throws ProcessingException
	 */
	public void copyFile(File src, File dest) throws ProcessingException;
	/**
	 * Schreibt den Inputstream in das <code>dest<code> File
	 * @param inStream
	 * @param dest
	 * @throws ProcessingException
	 */
	public void toFile(InputStream inStream, File dest) throws ProcessingException;
	/**
	 * @return Das Eclipse Working Directory
	 */
	public File getWorkingDirectory();
	/**
	 * Gibt den übergebenen Subfolder im WorkingDirectory zurück. Falls der Subfolder nicht existiert
	 * wird er stillschweigend angelegt.
	 * @param folderName
	 * @return den Subfolder im WorkingDirectory
	 */
	public File getFolderInWorkingDirectory(String folderName);
	public File createTempFolder();
	public void deleteDirectory(File binDir);
}
