package ch.jester.common.ui.databinding;

import java.util.List;

import ch.jester.common.persistency.util.DaoMatchFilter;
import ch.jester.common.persistency.util.EventLoadMatchingFilter;
import ch.jester.common.persistency.util.PersistencyListener;
import ch.jester.common.persistency.util.ScrollableResultListJPA;
import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.ui.utility.UIUtility.IBusyRunnable;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IPersistencyEvent;
import ch.jester.commonservices.api.persistency.IPersistencyEventQueue;
import ch.jester.commonservices.util.ServiceUtility;

public class PageController<T extends IDaoObject> {
	public interface IPageControllerUIAccess{
		public Object getFirstElement();
		public void setSelection(Object pSelection, boolean reveal);
		public void setInput(Object pInput);
	}
	
	
	private ServiceUtility su = Activator.getDefault().getActivationContext().getServiceUtil();
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	private ScrollableResultListJPA<T> jpaDBList;
	private IPageControllerUIAccess mViewer;
	private int currentPage = 0, mPageSize, mTotalEntries, mTotalPages;
	private List<T> pagelist;
	private int jpaDBListSize;
	private IDaoService<?> mPersister;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageController(List pPageList, IPageControllerUIAccess pViewer,
			IDaoService pPersister, int cSize) {
		jpaDBList = new ScrollableResultListJPA<T>(pPersister, cSize);
		mViewer = pViewer;
		mPageSize = cSize;
		mPersister = pPersister;
		pagelist = pPageList;
		
		
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
		jpaDBListSize = jpaDBList.size();
		calculatePages();

	}

	public int getTotalEntries() {
		return mPageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<T> getPageContent() {
		return pagelist;
	}

	public int gotoPage(int i) {
		if (currentPage == mTotalPages) {
			currentPage = mTotalPages;
		} else {
			currentPage = i;
		}
		loadPage();
		printSiteInfo();
		return currentPage;
	}

	public void nextPage() {
		
		if (currentPage == mTotalPages) {
			return;
		}
		currentPage++;
		loadPage();
		printSiteInfo();

	}

	public boolean hasNextPage() {
		return currentPage <mTotalPages;
	}

	public boolean hasPreviousPage() {
		return currentPage>0;
	}
	
	private List<?> loadPage() {
		final StopWatch watch = new StopWatch();
		watch.start();
		UIUtility.busyIndicatorJob("Paging", new IBusyRunnable() {
		
			List<T> reloaded;
			@Override
			public void stepOne_InUIThread() {
				if (mViewer.getFirstElement() != null) {
					mViewer.setSelection(
							mViewer.getFirstElement(),
							true);
				}
				System.out.println(currentPage * mPageSize + " "
						+ (currentPage * mPageSize + mPageSize));

				mViewer.setInput(null);
				pagelist.clear();
				
			}
			
			
			@Override
			public void stepTwo_InJob() {
				System.out.println("cleared");
				reloaded = (List<T>) jpaDBList.getItems(currentPage
						* mPageSize, currentPage * mPageSize + mPageSize);
				
			}
			
			@Override
			public void finalStep_inUIThread() {
				pagelist.addAll(reloaded);
				mViewer.setInput(pagelist);
				
			}
		});
				
		watch.stop();
		mLogger.debug("loadPage took " + watch.getElapsedTime());
		reevaluate();
		return pagelist;
	}
	private void reevaluate(){
		UIUtility.reevaluateProperty("ch.jester.properties.controlled");
	}
	private void printSiteInfo() {
		mLogger.debug("Page " + currentPage + " / " + mTotalPages);
	}

	public void prevPage() {
		if (currentPage == 0) {
			return;
		}
		currentPage--;
		loadPage();
		printSiteInfo();

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
	
	class MutableBoolean{
		private boolean mB;
		public MutableBoolean(boolean b) {
			mB = b;
		}
		public void set(boolean b){
			mB=b;
		}
		public boolean get(){
			return mB;
		}
	}
	

}