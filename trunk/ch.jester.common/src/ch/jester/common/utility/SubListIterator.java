package ch.jester.common.utility;

import java.util.Iterator;
import java.util.List;



	
/**
 * Iteriert über Sublisten
 *
 * @param <T>
 */
public class SubListIterator<T> implements Iterator<List<T>>{
	private List<T> mOrigList;
	private int mLoad;
	private int mTotalIterations;
	private int mCurrentIteration = 0;
	/**
	 * Anhand von <code>pLoad</code> werden die Sublisten erstellt.<br>
	 * Es wird kein Balancing der Einträge gemacht, so dass die letzte Subliste
	 * kleiner sein kann.
	 * @param pTList die Originalliste
	 * @param load die Grösse der einzelnen Sublisten
	 */
	public SubListIterator(List<T> pTList, int pLoad) {
		mOrigList=pTList;
		mLoad = pLoad;
		int rest = pTList.size() % mLoad;
		int reps = (pTList.size() - rest) / pLoad;
		if(rest>0){
			reps++;
		}
		mTotalIterations = reps;
	}
	
		@Override
		public boolean hasNext() {
			return mCurrentIteration<mTotalIterations;
		}

		@Override
		public List<T> next() {
			int start = mCurrentIteration * mLoad;
			int end = start + mLoad;
			if(mCurrentIteration+1==mTotalIterations){
				end = mOrigList.size();
			}
			mCurrentIteration++;
			return mOrigList.subList(start, end);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
		
		
	}

