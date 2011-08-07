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

/**
 * Implementiert ein Paging für die DB.
 *
 * @param <T>
 */
public class PageController<T extends IEntityObject>{
	/**
	 * Benötigt für die Kappselung eines Viewers. (Unterschiedliche Methoden je Viewer von Eclipse aus)
	 *
	 */
	public interface IPageControllerUIAccess{
		/**
		 * Das erste Element der Selektion.
		 * @return das erste Element.
		 */
		public Object getFirstElement();
		/**
		 * Setzen der Selektion
		 * @param pSelection
		 * @param reveal
		 */
		public void setSelection(Object pSelection, boolean reveal);
		/**
		 * Setzen des Inputs
		 * @param pInput der neue Input
		 */
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

	/**
	 * Init.
	 */
	public void initialize(){
		jpaDBListSize = jpaDBList.size();
		calculatePages();
		loadPage();
		reevaluate();
	}
	
	/**
	 * Paging kann ein und ausgeschaltet werden
	 * @param b (true|false)
	 */
	public void enablePaging(boolean b){
		mPagingEnabled=b;
		reevaluate();
	}
	
	/**
	 * Gibt die Page Size zurück.
	 * @return pageSizing.
	 */
	public int getTotalEntries() {
		return mPageSize;
	}

	/**
	 * Gibt die aktuelle Seite zurück.
	 * @return die aktuelle Seite
	 */
	public int getCurrentPage() {
		return currentInternalPage;
	}

	/**
	 * Gibt den Content der aktuellen Seite zurück.
	 * @return die Content Objekte
	 */
	public List<T> getPageContent() {
		return pagelist;
	}

	/**
	 * Springt zur angegebenen Seite (i).
	 * Bzw zu Seite 1 oder max wenn ein ungültige Seite angeben wurde(i<0 oder i> max)
	 * @param i
	 * @return
	 */
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

	/**
	 * Versucht zur nächsten Seite zu springen.
	 */
	public void nextPage() {
		
		if (currentInternalPage == mTotalPages) {
			return;
		}
		currentInternalPage++;
		loadPage();
		printSiteInfo();
		//reevaluate();

	}

	/**
	 * Gibt es eine nächste Seite
	 * @return true|false
	 */
	public boolean hasNextPage() {
		if(!mPagingEnabled){return false;}
		return currentInternalPage <mTotalPages;
	}

	/**
	 * Gibt es eine vorgängie Seite
	 * @return true | false
	 */
	public boolean hasPreviousPage() {
		if(!mPagingEnabled){return false;}
		return currentInternalPage>0;
	}
	
	/**
	 * laden der Seite
	 * @return
	 */
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
	/**
	 * properties reevaluiieren.
	 */
	private void reevaluate(){
		UIUtility.reevaluateProperty("ch.jester.properties.controlled");
	}
	private void printSiteInfo() {
		mLogger.debug("Page " + currentInternalPage + " / " + mTotalPages);
	}

	/**
	 * geht zur vorgängingen Seite
	 */
	public void prevPage() {
		if (currentInternalPage == 0) {
			return;
		}
		currentInternalPage--;
		loadPage();
		printSiteInfo();
		//reevaluate();
	}

	/**
	 * Total Anzahl Seiten
	 * @return total Anzahl Seiten
	 */
	public int totalPages() {
		calculatePages();
		return mTotalPages;
	}

	/**
	 * Seiten berechnen.
	 */
	private void calculatePages() {
		if (jpaDBListSize == 0) {
			return;
		}
		mTotalEntries = jpaDBListSize;
		int rest = mTotalEntries % mPageSize;
		mTotalPages = ((mTotalEntries - rest) / mPageSize);

	}
	

}
