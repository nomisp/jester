package ch.jester.ui.filter;


import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;

public class SearchField {
	private Stack<String> eventStack = new Stack<String>();
	private ISchedulingRule mutex = new MutexSchedulingRule();
	Text mText;
	StructuredViewer mViewer;
	String oldSearchValue="";
	public SearchField(Composite pParent, StructuredViewer pViewer){
		mViewer = pViewer;
		mText = new Text(pParent, SWT.SEARCH);
	
		mText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(mText.getText().equals(oldSearchValue)){
					return;
				}
				oldSearchValue=mText.getText();
				eventStack.push(oldSearchValue);

				Job job = new Job("searching"){

					@Override
					public IStatus run(IProgressMonitor monitor) {
						String search = null;
						synchronized(eventStack){
							
							if(eventStack.isEmpty()){
								return Status.CANCEL_STATUS;
							}
							search = eventStack.pop();
							eventStack.clear();
						}
						try{
							if(!eventStack.isEmpty()){
								System.out.println("Ending Job");
								return Status.CANCEL_STATUS;
							}
							
							monitor.beginTask("searching for: "+search, IProgressMonitor.UNKNOWN);
							System.out.println("executing search: "+search);
							ServiceUtility su = new ServiceUtility();
							List<Player> players = su.getExclusiveService(IPlayerPersister.class).findByName(search);
							su.getExclusiveService(PlayerListController.class).setPlayers(players);
						}finally{
							monitor.done();
						}
						return Status.OK_STATUS;
					}
					
				};
				job.setRule(mutex);
				
				job.schedule(250);
				
			}
			
		});
	}
	public Text getField(){
		return mText;
	}
	
	public static ControlContribution createSearchFieldControlContribution(final StructuredViewer viewer){
		return new ControlContribution("searchField") {

			@Override
			protected Control createControl(Composite parent) {
				SearchField mSearch = new SearchField(parent, viewer);
				return mSearch.getField();
			}
			
		};
	}
	
	class MutexSchedulingRule implements ISchedulingRule {
		
		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			if(rule==this){return true;}
			return false;
		}
		
		@Override
		public boolean contains(ISchedulingRule rule) {
			if(rule==this){return true;}
			return false;
		}
	}
}
