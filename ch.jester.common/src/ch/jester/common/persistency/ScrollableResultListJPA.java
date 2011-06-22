package ch.jester.common.persistency;

import java.util.AbstractList;
import java.util.List;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.persistency.IDaoService;



public class ScrollableResultListJPA<T extends IEntityObject> extends AbstractList<T> implements List<T> {
    private int startPosition;
    private List<T> cache = null;
    private IDaoService<T> persister;
    private int mCacheSize;
    public ScrollableResultListJPA(IDaoService<T> pPersister, int cacheSize) {
    	persister = pPersister;
        this.startPosition = 0;
        mCacheSize=cacheSize;
       // this.cache = getItems(startPosition, startPosition + mCacheSize);
       
    }

    public int size() {
    	return persister.count();
    }


    public T get(int rowIndex) {
        if (this.cache!=null && (rowIndex >= startPosition) && (rowIndex < (startPosition + mCacheSize)) && !cache.isEmpty()) {
        } else {
        	this.cache = null;
            this.cache = getItems(rowIndex, rowIndex + mCacheSize);
            this.startPosition = rowIndex;
        }
        T c = cache.get(rowIndex - startPosition);

        return c;
    }

    public List<T> getItems(int from, int to) {
       List<T> resultList = persister.getFromTo(from, to);
       return resultList;
    }
}