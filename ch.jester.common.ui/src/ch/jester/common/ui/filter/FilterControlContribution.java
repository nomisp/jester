package ch.jester.common.ui.filter;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import ch.jester.common.ui.utility.FontUtil;

public class FilterControlContribution extends WorkbenchWindowControlContribution{
	Font mFont = FontUtil.createSizedFont(8);
	@Override
	protected Control createControl(Composite parent) {
		ToolBar bar = (ToolBar)parent;
		Text sf = new FilterField(bar, getId()).getField();
		sf.setFont(mFont);
		return sf;
	}
	
	@Override
	public void dispose() {
		mFont.dispose();
		super.dispose();
	}

}
