package ch.jester.common.ui.utility;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class FontUtil {
	public static Font getDefaultFont(){
		return JFaceResources.getDefaultFont();
	}
	public static Font createDefaultBoldFont(){
		Font def = JFaceResources.getDefaultFont();
		FontData[] df = def.getFontData();
		return new Font(null, df[0].getName(), df[0].getHeight(), SWT.BOLD);
	}
}
