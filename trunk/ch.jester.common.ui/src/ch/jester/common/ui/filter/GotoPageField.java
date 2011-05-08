package ch.jester.common.ui.filter;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.eclipse.ui.services.IServiceLocator;

import ch.jester.common.ui.utility.FontUtil;
import ch.jester.common.utility.DefaultAdapterFactory;

public class GotoPageField extends
		WorkbenchWindowControlContribution implements IWorkbenchContribution {
	Font mFont = FontUtil.createSizedFont(8);
	Text text;
	public GotoPageField() {
		// TODO Auto-generated constructor stub
	}

	public GotoPageField(String id) {
		super(id);
	}

	@Override
	protected Control createControl(Composite pParent) {
		ToolBar bar = (ToolBar)pParent;
		text = new Text(bar,  SWT.BORDER | SWT.LEFT | SWT.TRAIL);
	//	GridData data = new GridData();
	//	data.widthHint = 20;
	//	text.setLayoutData(data);
		text.setMessage("goto");
		//text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setFont(mFont);
	//    ToolItem itemSeparator = new ToolItem((ToolBar) pParent,   SWT.SEPARATOR | SWT.TRAIL); 
	  //  text.setBounds(0, 0, h, height)
	  //  itemSeparator.setWidth(text.getBounds().width+20);  
       // itemSeparator.setControl(text); 
		return text;
	}
	public void dispose(){
		mFont.dispose();
		super.dispose();
	}
	public void setParent(IContributionManager m){
		super.setParent(m);
		
	}
	
	public void setWorkbenchWindow(IWorkbenchWindow wbw) {
		super.setWorkbenchWindow(wbw);
		
	}
	public String getText(){
		return text.getText();
	}
	
	
	@Override
	public void initialize(IServiceLocator serviceLocator) {
		IWorkbenchLocationService part = (IWorkbenchLocationService) serviceLocator.getService(IWorkbenchLocationService.class);
		Object view2 = part.getPartSite().getPart();
		DefaultAdapterFactory factory = new DefaultAdapterFactory(view2);
		factory.add(GotoPageField.class, this);
		factory.registerAtPlatform();
		//System.out.println(serviceLocator);
		
	}

}
