package ch.jester.dao;

import java.util.AbstractList;
import java.util.List;


import ch.jester.common.utility.StopWatch;



public class ScrollableResultListJPA<T extends IDAO> extends AbstractList<T> implements List<T> {
    private int startPosition;
    private int counter=0;
    private List<T> cache = null;
    private IPersister<T> persister;
    private int mCacheSize;
    public ScrollableResultListJPA(IPersister<T> pPersister, int cacheSize) {
    	persister = pPersister;
        this.startPosition = 0;
        mCacheSize=cacheSize;
        this.cache = getItems(startPosition, startPosition + mCacheSize);
    }

    public int size() {
    	return persister.count();
    }


    public T get(int rowIndex) {
        if ((rowIndex >= startPosition) && (rowIndex < (startPosition + mCacheSize)) && !cache.isEmpty()) {
        } else {
        	this.cache = null;
            this.cache = getItems(rowIndex, rowIndex + mCacheSize);
            this.startPosition = rowIndex;
        }
        T c = cache.get(rowIndex - startPosition);

        return c;
    }

    private List<T> getItems(int from, int to) {
        System.out.println("numer of requests to the database " + counter++);
        StopWatch watch = new StopWatch();
        watch.start();
        List<T> resultList = persister.getFromTo(from, to);
        watch.stop();
        System.out.println("getFromTo Duration: "+watch.getElapsedTime());
        return resultList;
    }
}