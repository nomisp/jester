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

import ch.jester.common.utility.ExtensionPointUtil;
import ch.jester.commonservices.filter.IFilter;
import ch.jester.job.StackJob;

public class FilterField {
	private Stack<String> eventStack = new Stack<String>();
	FilterJob job = new FilterJob();
	Text mText;
	String oldSearchValue="", mId;
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
				System.out.println(mId);
				if(mText.getText().equals(oldSearchValue)){
					return;
				}
				oldSearchValue=mText.getText();
				eventStack.push(oldSearchValue);
				job.reschedule(150);
				
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
			super("searching",eventStack);
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
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}else{
					mFilter = NULL_FILTER;
				}
			
			}
			if(event.equals("")){
				int k=0;
				k++;
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
