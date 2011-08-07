package ch.jester.common.ui.databinding;

import java.util.List;

import ch.jester.common.persistency.DaoMatchFilter;
import ch.jester.common.persistency.EventLoadMatchingFilter;
import ch.jester.common.persistency.PersistencyListener;
import ch.jester.common.persistency.ScrollableResultListJPA;
import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.ui.utility.UIUtility.IBusyRunnable;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDatabaseStateService;
import ch.jester.commonservices.api.persistency.IDatabaseStateService.State;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.util.ServiceUtility;

public class PageController<T extends IEntityObject>{
	public interface IPageControllerUIAccess{
		public Object getFirstElement();
		public void setSelection(Object pSelection, boolean reveal);
		public void setInput(Object pInput);
	}
	
	
	private ServiceUtility su = CommonUIActivator.getDefault().getActivationContext().getServiceUtil();
	private ILogger mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	private ScrollableResultListJPA<T> jpaDBList;
	private IPageControllerUIAccess mViewer;
	private int currentInternalPage = 0, mPageSize, mTotalEntries, mTotalPages;
	private List<T> pagelist;
	private int jpaDBListSize;
	private boolean mPagingEnabled = true;
	private IDaoService<?> mPersister;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageController(List pPageList, IPageControllerUIAccess pViewer,
			IDaoService pPersister, int cSize) {
		jpaDBList = new ScrollableResultListJPA<T>(pPersister, cSize);
		mViewer = pViewer;
		mPageSize = cSize;
		mPersister = pPersister;
		pagelist = pPageList;
		new ServiceUtility().getService(IDatabaseStateService.class).executeOnStateChange(State.RUN, new Runnable() {
			
			@Override
			public void run() {
				initialize();
			}
		});
		
		su.getService(IPersistencyEventQueue.class).addListener(
				new PersistencyListener(
						new EventLoadMatchingFilter(mPersister.getDaoClass(), 
								new DaoMatchFilter(mPersister))) {
			@Override
			public void persistencyEvent(IPersistencyEvent pEvent) {
				synchronized (jpaDBList) {
					jpaDBListSize = jpaDBList.size();
					calculatePages();
					loadPage();
				}
				
			}
		});	
		

	}

	public void initialize(){
		jpaDBListSize = jpaDBList.size();
		calculatePages();
		loadPage();
		reevaluate();
	}
	
	public void enablePaging(boolean b){
		mPagingEnabled=b;
		reevaluate();
	}
	
	public int getTotalEntries() {
		return mPageSize;
	}

	public int getCurrentPage() {
		return currentInternalPage;
	}

	public List<T> getPageContent() {
		return pagelist;
	}

	public int gotoPage(int i) {
		i = i-1;
		if (currentInternalPage == mTotalPages ) {
			currentInternalPage = mTotalPages;
		} else if(i>mTotalPages){
			currentInternalPage = mTotalPages;
		}else if(i<1){
			currentInternalPage = 0;
		}
		else{
			currentInternalPage = i;
		}
		loadPage();
		printSiteInfo();
		//reevaluate();
		return currentInternalPage;
	}

	public void nextPage() {
		
		if (currentInternalPage == mTotalPages) {
			return;
		}
		currentInternalPage++;
		loadPage();
		printSiteInfo();
		//reevaluate();

	}

	public boolean hasNextPage() {
		if(!mPagingEnabled){return false;}
		return currentInternalPage <mTotalPages;
	}

	public boolean hasPreviousPage() {
		if(!mPagingEnabled){return false;}
		return currentInternalPage>0;
	}
	
	private List<?> loadPage() {
		final StopWatch watch = new StopWatch();
		watch.start();
		UIUtility.busyIndicatorJob("Paging", new IBusyRunnable() {
		
			List<T> reloaded;
			@Override
			public void stepOne_InUIThread() {
				PageController.this.enablePaging(false);
				if (mViewer.getFirstElement() != null) {
					mViewer.setSelection(
							mViewer.getFirstElement(),
							true);
				}

				mViewer.setInput(null);
				pagelist.clear();
				
			}
			
			
			@Override
			public void stepTwo_InJob() {
				reloaded = (List<T>) jpaDBList.getItems(currentInternalPage
						* mPageSize, currentInternalPage * mPageSize + mPageSize);
				
			}
			
			@Override
			public void finalStep_inUIThread() {
				pagelist.addAll(reloaded);
				mViewer.setInput(pagelist);
				PageController.this.enablePaging(true);
				
			}
		});
				
		reevaluate();
		watch.stop();
		mLogger.debug("loadPage took " + watch.getElapsedTime());
		
		return pagelist;
	}
	private void reevaluate(){
		UIUtility.reevaluateProperty("ch.jester.properties.controlled");
	}
	private void printSiteInfo() {
		mLogger.debug("Page " + currentInternalPage + " / " + mTotalPages);
	}

	public void prevPage() {
		if (currentInternalPage == 0) {
			return;
		}
		currentInternalPage--;
		loadPage();
		printSiteInfo();
		//reevaluate();
	}

	public int totalPages() {
		calculatePages();
		return mTotalPages;
	}

	private void calculatePages() {
		if (jpaDBListSize == 0) {
			return;
		}
		mTotalEntries = jpaDBListSize;
		int rest = mTotalEntries % mPageSize;
		mTotalPages = ((mTotalEntries - rest) / mPageSize);

	}
	

}
