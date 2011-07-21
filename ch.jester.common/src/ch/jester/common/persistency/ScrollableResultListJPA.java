package ch.jester.common.persistency;

import java.util.AbstractList;
import java.util.List;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IDaoService;

/**
 * Hilfsklasse mit Cachefunktion für Scrollable Results von der DB... zb. im UI verwendbar
 *
 * @param <T>
 */
public class ScrollableResultListJPA<T extends IEntityObject> extends AbstractList<T> implements List<T> {
    private int mStartPosition;
    private List<T> mCache = null;
    private IDaoService<T> mDao;
    private int mCacheSize;
    public ScrollableResultListJPA(IDaoService<T> pDao, int pCacheSize) {
    	mDao = pDao;
        this.mStartPosition = 0;
        mCacheSize=pCacheSize;
       
    }

    @Override
    public int size() {
    	return mDao.count();
    }

    @Override
    public T get(int rowIndex) {
        if (this.mCache!=null && (rowIndex >= mStartPosition) && (rowIndex < (mStartPosition + mCacheSize)) && !mCache.isEmpty()) {
        } else {
        	this.mCache = null;
            this.mCache = getItems(rowIndex, rowIndex + mCacheSize);
            this.mStartPosition = rowIndex;
        }
        T c = mCache.get(rowIndex - mStartPosition);

        return c;
    }

    /**
     * Rückgabe from to direkt von der DB
     * @param from
     * @param to
     * @return
     */
    public List<T> getItems(int from, int to) {
       List<T> resultList = mDao.getFromTo(from, to);
       return resultList;
    }
}