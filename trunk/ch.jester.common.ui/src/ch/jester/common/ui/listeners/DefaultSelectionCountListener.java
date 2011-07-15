package ch.jester.common.ui.listeners;

import messages.Messages;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.osgi.framework.FrameworkUtil;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.util.ServiceUtility;


/**
 * SelectionListener welcher<br>
 * 'x Item(s) selected' <br>
 * dem StatusLineManager übergibt
 *
 */
public class DefaultSelectionCountListener implements ISelectionChangedListener{
		private ServiceUtility mServices = new ServiceUtility(FrameworkUtil.getBundle(
				DefaultSelectionCountListener.this.getClass()).getBundleContext());
		private SelectionUtility mSelectionUtil = new SelectionUtility(null);
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStatusLineManager lm = mServices.getService(IStatusLineManager.class);
			mSelectionUtil.setSelection(event.getSelection());
			if(lm!=null){
				if(mSelectionUtil.isEmpty()){lm.setMessage(""); return;} //$NON-NLS-1$
				int i = mSelectionUtil.getSelectionCount();
				lm.setMessage(i+Messages.DefaultSelectionCountListener_items_selected);
			}
		}
	}
