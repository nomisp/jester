package ch.jester.ui.filter;


import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;

public class SearchField {
	private Stack<String> eventStack = new Stack<String>();
	Text mText;
	IFilter mFilter;
	StructuredViewer mViewer;
	//private IWorkbenchWindow window;
	public SearchField(Composite pParent, IFilter pFilter, StructuredViewer pViewer){
		mViewer = pViewer;
		mFilter=pFilter;
		//mViewer.addFilter(mFilter);
		
		mText = new Text(pParent, SWT.SEARCH);
		mFilter = pFilter;
		mText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				eventStack.push(mText.getText());

				Job job = new Job("refresh"){

					@Override
					public IStatus run(IProgressMonitor monitor) {
						String search = null;
						synchronized(eventStack){
							if(eventStack.isEmpty()){
								return Status.OK_STATUS;
							}
							search = eventStack.pop();
							eventStack.clear();
						}
						System.out.println("executing search: "+search);
						ServiceUtility su = new ServiceUtility();
						List<Player> players = su.getExclusiveService(IPlayerPersister.class).findByName(search);
						su.getExclusiveService(PlayerListController.class).setPlayers(players);
						return Status.OK_STATUS;
					}
					
				};
				
				job.schedule();
				
			}
			
		});
	}
	public Text getField(){
		return mText;
	}
}
