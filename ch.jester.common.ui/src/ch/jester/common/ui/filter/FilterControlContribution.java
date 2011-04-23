package ch.jester.common.ui.filter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class FilterControlContribution extends WorkbenchWindowControlContribution{

	@Override
	protected Control createControl(Composite parent) {
		Font fdata = new Font(Display.getDefault(),"Arial", 9, SWT.NORMAL);
		ToolBar bar = (ToolBar)parent;
		Text sf = new FilterField(bar, getId()).getField();
		sf.setFont(fdata);
		return sf;
	}

}
