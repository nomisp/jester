package ch.jester.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import ch.jester.common.ui.utility.FontUtil;

public class WorkbenchWindowControlContribution1 extends
		WorkbenchWindowControlContribution {
	Font mFont = FontUtil.createSizedFont(8);
	public WorkbenchWindowControlContribution1() {
		// TODO Auto-generated constructor stub
	}

	public WorkbenchWindowControlContribution1(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createControl(Composite pParent) {
		ToolBar bar = (ToolBar)pParent;
		Text text = new Text(bar,  SWT.BORDER| SWT.CENTER | SWT.LEFT);
		GridData data = new GridData();
		data.widthHint = 20;
		text.setLayoutData(data);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setFont(mFont);
		return text;
	}
	public void dispose(){
		mFont.dispose();
		super.dispose();
	}

}
