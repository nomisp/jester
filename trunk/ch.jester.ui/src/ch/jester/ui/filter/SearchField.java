package ch.jester.ui.filter;


import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
	Text mText;
	StructuredViewer mViewer;
	public SearchField(Composite pParent, StructuredViewer pViewer){
		mViewer = pViewer;
		mText = new Text(pParent, SWT.SEARCH);
	
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
								return Status.CANCEL_STATUS;
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
	
	public static ControlContribution createSearchFieldControlContribution(final StructuredViewer viewer){
		return new ControlContribution("searchField") {

			@Override
			protected Control createControl(Composite parent) {
				SearchField mSearch = new SearchField(parent, viewer);
				return mSearch.getField();
			}
			
		};
	}
}
