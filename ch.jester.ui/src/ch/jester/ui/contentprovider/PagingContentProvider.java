package ch.jester.ui.contentprovider;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistencyevent.IPersistencyListener;
import ch.jester.commonservices.api.persistencyevent.PersistencyEvent;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.dao.ScrollableResultListJPA;
import ch.jester.model.Player;
import ch.jester.ui.Activator;

/**
 * ContentProvider der Paging beherrscht.
 *
 */
public class PagingContentProvider implements IStructuredContentProvider {
	private ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private IPlayerDao persister = su.getExclusiveService(IPlayerDao.class);
	private ScrollableResultListJPA<Player> jpaDBList;
	private TableViewer mViewer;
	int currentPage=0, mPageSize, mTotalEntries, mTotalPages=-1;
	private List<?> currentInput;
	private int jpaDBListSize;
	public PagingContentProvider(TableViewer pViewer, int pPageSize){
		mViewer = pViewer;
		jpaDBList = new ScrollableResultListJPA<Player>(persister, pPageSize);
		mPageSize=pPageSize;
		su.getService(IPersistencyEventQueue.class).addListener(new IPersistencyListener() {
			
			@Override
			public void persistencyEvent(PersistencyEvent event) {
				synchronized(jpaDBList){
					jpaDBListSize = jpaDBList.size();
					calculatePages();
				}
				
			}
		});
		jpaDBListSize = jpaDBList.size();
		calculatePages();
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		int inputSize = 0;
		if(newInput instanceof List && !((List<?>)newInput).isEmpty()){
			if(oldInput==newInput){
				return;
			}
			List<?> list = (List<?>) newInput;
			currentInput=list;
			if(list.size()<=mPageSize){
				inputSize = list.size();
			}else{
				inputSize = mPageSize;
			}
		}
		mLogger.debug("InputSize is: "+inputSize+" inputObject "+newInput);
		mViewer.setItemCount(inputSize);
		//calculatePages();

		
	}
	public void refresh(){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setItemCount(jpaDBListSize);;
				loadPage();
				
			}
		});
		
		
	}
	
	public int getTotalEntries(){
		return mPageSize;
	}
	
	public int getCurrentPage(){
		return currentPage;
	}
	
	public int gotoPage(int i){
		if(currentPage==mTotalPages){
			currentPage = mTotalPages;
		}else{
			currentPage=i;
		}
		loadPage();
		printSiteInfo();
		return currentPage;
	}
	public void nextPage(){
		if(currentPage==mTotalPages){
			return;
		}
		currentPage++;
		loadPage();
		printSiteInfo();
		
	}
	public boolean hasMorePages(){
		return currentPage!=mTotalPages;
	}
	
	private void loadPage() {
		BusyIndicator.showWhile(UIUtility.getActiveWorkbenchWindow().getShell().getDisplay(), new Runnable() {
			StopWatch watch = new StopWatch();
			@Override
			public void run() {
				watch.start();
				if(mViewer.getElementAt(0)!=null){
					mViewer.setSelection(new StructuredSelection(mViewer.getElementAt(0)), true);
				}
				syncedUI_setInput(null);
				System.out.println(currentPage*mPageSize + " " +currentPage*mPageSize+mPageSize);
				List<?> pagelist=null;
				synchronized(jpaDBList){
				pagelist = jpaDBList.getItems(currentPage*mPageSize, currentPage*mPageSize+mPageSize);
				}
				syncedUI_setInput(pagelist);
				mViewer.setInput(pagelist);
				mViewer.setSelection(new StructuredSelection(), true);
				watch.stop();
				mLogger.debug("loadPage took "+watch.getElapsedTime());
				
			}
		});

		
	}
	private void printSiteInfo(){
		mLogger.debug("Page "+currentPage+" / "+mTotalPages);
	}
	
	private void syncedUI_setInput(final Object pInput){
		UIUtility.syncExecInUIThread(new Runnable(){
			@Override
			public void run() {
				currentInput = (List<?>) pInput;
				if(pInput==null){
					
					mViewer.getControl().setRedraw(false);
				}
				mViewer.setInput(pInput);
				if(pInput!=null){
					mViewer.getControl().setRedraw(true);
				}
			}

		});
	}
	public void prevPage(){
		if(currentPage==0){
			return;
		}
		currentPage--;
		loadPage();
		printSiteInfo();
		
	}
	public int totalPages(){
		calculatePages();
		return mTotalPages;
	}
	
	private void calculatePages(){
		 mTotalEntries  =jpaDBListSize;
		 int rest = mTotalEntries % mPageSize;
		 mTotalPages = ((mTotalEntries-rest) / mPageSize);
		 
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(currentInput == null){
			return Collections.EMPTY_LIST.toArray();
		}
		return currentInput.toArray();
	}
};
