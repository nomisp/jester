package ch.jester.common.ui.filter;


import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewPart;

import ch.jester.common.job.StackJob;
import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;

/**
 * Ein FilterFeld.
 *
 */
public class FilterField {
	private Stack<String> mEventStack = new Stack<String>();
	private FilterJob mJob = new FilterJob();
	private Text mText;
	private String mOldSearchValue="", mId;
	private IViewPart mPart;
	ControlDecoration decoration;
	public FilterField(Composite pParent, String pId){
		mId=pId;

		//GridData data = new GridData();
		//data.widthHint = 100;
	    ToolItem itemSeparator = new ToolItem((ToolBar) pParent, SWT.SEPARATOR); 

		mText = new Text(pParent, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH  | SWT.LEFT);


		//GridData d = new GridData(SWT.RIGHT,SWT.RIGHT,false, false);
		//mText.setLayoutData(d);
		mText.setMessage("filter                          ");
	
		int w = 10;
		//new
		String os = System.getProperty("os.name").toLowerCase();
		boolean mac = os.indexOf("mac") >= 0;
		if(mac){
			w = 90;
		}
		
	    itemSeparator.setWidth(mText.getBounds().width+w);  
	    itemSeparator.setControl(mText);
	    
		
		Image image = FieldDecorationRegistry.getDefault()
        .getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();
		decoration = new ControlDecoration(mText, SWT.RIGHT | SWT.CENTER);
		decoration.setImage(image);
		decoration.setDescriptionText("Enter min. 2 characters");
		decoration.hide();
		
	//	mText.setSize(500, 200);
	//	mText.setLayoutData(data);

		mText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				resetSearch();
			}
			
		});
		mText.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if(e.detail == SWT.ICON_CANCEL){
					resetSearch();
				}
				
			}
		});
	}
	
	private void resetSearch(){
		String newText = mText.getText();
		if(newText.length()==0){
			if(newText.equals(mOldSearchValue)){return;}
			mJob.clear();
			return;
		}
		if(newText.equals(mOldSearchValue)||newText.length()<2){
			decoration.show();
			return;}
		
	/*	if(mText.getText().equals(mOldSearchValue)||mText.getText().length()<2){
			return;
		}*/
		decoration.hide();
		mOldSearchValue=mText.getText();
		mEventStack.push(mOldSearchValue);
		mJob.reschedule(150);
	}
	
	public Text getField(){
		return mText;
	}
	
	class FilterJob extends StackJob<String> {
	ILogger mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	
		IUIFilter NULL_FILTER = new ErrorFilter();
		IUIFilter mFilter;
		public FilterJob() {
			super("searching",mEventStack);
			mLogger.debug("new SearchJob");
		}



		public void clear() {
			mFilter.clear();
		}



		@Override
		protected IStatus runInternal(IProgressMonitor monitor,
				String event) {
			if(mFilter==null){
				IConfigurationElement element = ExtensionPointUtil
				.getExtensionPointElement("ch.jester.commonservices.api","Filter", "Id",mId);
				if(element!=null){
					try {
						mFilter = (IUIFilter) element.createExecutableExtension("class");
						String mPartId = element.getAttribute("TargetViewId");
						mPart = UIUtility.getActiveWorkbenchWindow().getActivePage().findView(mPartId);
						mLogger.debug("Created Filter: "+mFilter.getClass()+" for id: "+mId);
						
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}else{
					mFilter = NULL_FILTER;
				}
			
			}
			return mFilter.filter(event, mPart, monitor);

		}
	};
	
	class ErrorFilter implements IUIFilter{

		@Override
		public IStatus filter(String pSearch, IViewPart pPart, IProgressMonitor pMonitor) {
			return new Status(IStatus.ERROR, "ch.jester.common.ui", "No Filter installed");
		}

		@Override
		public void clear() {
		}
		
	}
	
}
