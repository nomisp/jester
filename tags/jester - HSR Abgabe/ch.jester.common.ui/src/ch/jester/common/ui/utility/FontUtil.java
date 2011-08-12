package ch.jester.common.ui.utility;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * FontManipulationsklasse
 *
 */
public class FontUtil {
	/**
	 * @return den Default JFace Font
	 */
	public static Font getDefaultFont(){
		return JFaceResources.getDefaultFont();
	}
	/**
	 * @return den Default Font in Bold
	 */
	public static Font createDefaultBoldFont(){
		Font def = JFaceResources.getDefaultFont();
		FontData[] df = def.getFontData();
		return new Font(null, df[0].getName(), df[0].getHeight(), SWT.BOLD);
	}
	/**
	 * Kreiert den Defaultfont +/- der resize Zahl
	 * @param resize
	 * @return
	 */
	public static Font createResizedeFont(int resize){
		Font def = JFaceResources.getDefaultFont();
		FontData[] df = def.getFontData();
		return new Font(null, df[0].getName(), df[0].getHeight()+resize, SWT.NORMAL);
	}
	/**Kreiert den Defaulfont in der angegeben Grösse
	 * @param size
	 * @return
	 */
	public static Font createSizedFont(int size){
		Font def = JFaceResources.getDefaultFont();
		FontData[] df = def.getFontData();
		return new Font(null, df[0].getName(), size, SWT.NORMAL);
	}
}
