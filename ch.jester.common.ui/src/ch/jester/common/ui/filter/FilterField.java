package ch.jester.common.ui.filter;


import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.internal.Activator;
import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.filter.IFilter;
import ch.jester.job.StackJob;

public class FilterField {
	private Stack<String> mEventStack = new Stack<String>();
	private FilterJob mJob = new FilterJob();
	private Text mText;
	private String mOldSearchValue="", mId;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public FilterField(Composite pParent, String pId){
		mId=pId;
		mText = new Text(pParent, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
		mText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mText.setMessage("filter                          ");
		GridData data = new GridData();
		data.widthHint = 100;
		mText.setLayoutData(data);
		mText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(mText.getText().equals(mOldSearchValue)){
					return;
				}
				mOldSearchValue=mText.getText();
				mEventStack.push(mOldSearchValue);
				mJob.reschedule(150);
				
			}
			
		});
	}
	
	public Text getField(){
		return mText;
	}
	
	class FilterJob extends StackJob<String> 
	{
		IFilter NULL_FILTER = new ErrorFilter();
		IFilter mFilter;
		public FilterJob() {
			super("searching",mEventStack);
		}



		@Override
		protected IStatus runInternal(IProgressMonitor monitor,
				String event) {
			if(mFilter==null){
				IConfigurationElement element = ExtensionPointUtil
				.getExtensionPointElement("ch.jester.commonservices.api","Filter", "Id",mId);
				if(element!=null){
					try {
						mFilter = (IFilter) element.createExecutableExtension("class");
						mLogger.debug("Created Filter: "+mFilter.getClass()+" for id: "+mId);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}else{
					mFilter = NULL_FILTER;
				}
			
			}
			return mFilter.filter(event, monitor);

		}
	};
	
	class ErrorFilter implements IFilter{

		@Override
		public IStatus filter(String pSearch, IProgressMonitor pMonitor) {
			return new Status(IStatus.ERROR, "ch.jester.common.ui", "No Filter installed");
		}
		
	}
	
}
