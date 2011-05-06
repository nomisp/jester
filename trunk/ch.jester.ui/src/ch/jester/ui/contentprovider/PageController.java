package ch.jester.ui.contentprovider;

import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistencyevent.DaoMatchFilter;
import ch.jester.commonservices.api.persistencyevent.EventLoadMatchingFilter;
import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.api.persistencyevent.PersistencyEvent;
import ch.jester.commonservices.api.persistencyevent.PersistencyListener;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IDaoService;
import ch.jester.dao.ScrollableResultListJPA;
import ch.jester.model.Player;
import ch.jester.ui.Activator;

public class PageController<T> {
	private ServiceUtility su = Activator.getDefault().getActivationContext()
			.getServiceUtil();
	private ILogger mLogger = Activator.getDefault().getActivationContext()
			.getLogger();
	private ScrollableResultListJPA<Player> jpaDBList;
	private TableViewer mViewer;
	private int currentPage = 0, mPageSize, mTotalEntries, mTotalPages = -1;
	private List<T> pagelist;
	private int jpaDBListSize;
	private IContributionItem gotoField;
	private IDaoService<?> mPersister;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageController(List pPageList, TableViewer pViewer,
			IDaoService pPersister, int cSize) {
		jpaDBList = new ScrollableResultListJPA<Player>(pPersister, cSize);
		mViewer = pViewer;
		mPageSize = cSize;
		mPersister = pPersister;
		pagelist = pPageList;
		
		
		su.getService(IPersistencyEventQueue.class).addListener(
				new PersistencyListener(
						new EventLoadMatchingFilter(Player.class, 
								new DaoMatchFilter(mPersister))) {
			@Override
			public void persistencyEvent(PersistencyEvent pEvent) {
				synchronized (jpaDBList) {
					jpaDBListSize = jpaDBList.size();
					calculatePages();
					UIUtility.syncExecInUIThread(new Runnable() {

						@Override
						public void run() {

							loadPage();

						}
					});

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

	public boolean hasMorePages() {
		return currentPage != mTotalPages;
	}

	private List<?> loadPage() {
		final StopWatch watch = new StopWatch();

		watch.start();
		UIUtility.syncExecInUIThread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				if (mViewer.getElementAt(0) != null) {
					mViewer.setSelection(
							new StructuredSelection(mViewer.getElementAt(0)),
							true);
				}
				System.out.println(currentPage * mPageSize + " "
						+ (currentPage * mPageSize + mPageSize));

				mViewer.setInput(null);
				pagelist.clear();

				System.out.println("cleared");
				pagelist.addAll((List<T>) jpaDBList.getItems(currentPage
						* mPageSize, currentPage * mPageSize + mPageSize));
				mViewer.setInput(pagelist);
			}
		});
		watch.stop();
		mLogger.debug("loadPage took " + watch.getElapsedTime());

		return pagelist;
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

	public void bindGotoField(IContributionItem find) {
		gotoField = find;
	}
	public int getGotoNr(){
		//gotoField.
		return 1;
	}
}
