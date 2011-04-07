package ch.jester.common.ui.utility;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Image;

import ch.jester.common.ui.services.IExtendedStatusLineManager;

public class DefaultExtendedStatusLineManager implements IExtendedStatusLineManager{
	private IStatusLineManager mManager;
	public DefaultExtendedStatusLineManager(IStatusLineManager pManager){
		mManager=pManager;
	}
	
	@Override
	public IProgressMonitor getProgressMonitor() {
		return mManager.getProgressMonitor();
	}

	@Override
	public boolean isCancelEnabled() {
		return mManager.isCancelEnabled();
	}

	@Override
	public void setCancelEnabled(boolean enabled) {
		mManager.setCancelEnabled(enabled);
	}

	@Override
	public void setErrorMessage(String message) {
		mManager.setErrorMessage(message);
	}

	@Override
	public void setErrorMessage(Image image, String message) {
		mManager.setErrorMessage(image, message);
	}

	@Override
	public void setMessage(String message) {
		mManager.setMessage(message);
	}

	@Override
	public void setMessage(Image image, String message) {
		mManager.setMessage(image, message);
	}

	@Override
	public void add(IAction action) {
		mManager.add(action);
	}

	@Override
	public void add(IContributionItem item) {
		mManager.add(item);
	}

	@Override
	public void appendToGroup(String groupName, IAction action) {
		mManager.appendToGroup(groupName, action);
	}

	@Override
	public void appendToGroup(String groupName, IContributionItem item) {
		mManager.appendToGroup(groupName, item);
	}

	@Override
	public IContributionItem find(String id) {
		return mManager.find(id);
	}

	@Override
	public IContributionItem[] getItems() {
		return mManager.getItems();
	}

	@Override
	public IContributionManagerOverrides getOverrides() {
		return mManager.getOverrides();
	}

	@Override
	public void insertAfter(String id, IAction action) {
		 mManager.insertAfter(id, action);
	}

	@Override
	public void insertAfter(String id, IContributionItem item) {
		mManager.insertAfter(id, item);
	}

	@Override
	public void insertBefore(String id, IAction action) {
		mManager.insertBefore(id, action);
	}

	@Override
	public void insertBefore(String id, IContributionItem item) {
		mManager.insertBefore(id, item);
	}

	@Override
	public boolean isDirty() {
		return mManager.isDirty();
	}

	@Override
	public boolean isEmpty() {
		return mManager.isEmpty();
	}

	@Override
	public void markDirty() {
		mManager.markDirty();
	}

	@Override
	public void prependToGroup(String groupName, IAction action) {
		mManager.prependToGroup(groupName, action);
	}

	@Override
	public void prependToGroup(String groupName, IContributionItem item) {
		mManager.prependToGroup(groupName, item);
	}

	@Override
	public IContributionItem remove(String id) {
		return mManager.remove(id);
	}

	@Override
	public IContributionItem remove(IContributionItem item) {
		return mManager.remove(item);
	}

	@Override
	public void removeAll() {
		mManager.removeAll();
	}

	@Override
	public void update(boolean force) {
		mManager.update(force);
	}

	@Override
	public void setMessage(String pMessage, int pDisposesInMilis) {
		mManager.setMessage(pMessage);
		Job job = new Job("MessageDisposer"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if(!monitor.isCanceled()){
				UIUtility.syncExecInUIThread(new Runnable() {
					@Override
					public void run() {
						
						mManager.setMessage("");
						
					}
				});
				}
				return Status.OK_STATUS;
			}
			
		};
		
		job.schedule(pDisposesInMilis);
	}
}
