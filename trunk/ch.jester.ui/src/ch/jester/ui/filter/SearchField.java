package ch.jester.ui.filter;


import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.job.StackJob;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerListController;

public class SearchField {
	private Stack<String> eventStack = new Stack<String>();
	SearchJob job = new SearchJob();
	Text mText;
	StructuredViewer mViewer;
	String oldSearchValue="";
	public SearchField(Composite pParent, StructuredViewer pViewer){
		mViewer = pViewer;
		mText = new Text(pParent, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
		mText.setMessage("Search");
		mText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(mText.getText().equals(oldSearchValue)){
					return;
				}
				oldSearchValue=mText.getText();
				eventStack.push(oldSearchValue);
				
				job.reschedule(150);
				
			}
			
		});
		Listener l = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				
			}};
		
		mText.addListener(SWT.DefaultSelection, l);
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
	
	class SearchJob extends StackJob<String> 
	{
		public SearchJob() {
			super("searching",eventStack);
		}



		@Override
		protected IStatus runInternal(IProgressMonitor monitor,
				String event) {
			monitor.beginTask("searching for: "+event, IProgressMonitor.UNKNOWN);
			System.out.println("executing search: "+event);
			ServiceUtility su = new ServiceUtility();
			List<Player> players = su.getExclusiveService(IPlayerPersister.class).findByName(event);
			su.getExclusiveService(PlayerListController.class).setPlayers(players);
			return Status.OK_STATUS;
		}
	};
	
	
}
