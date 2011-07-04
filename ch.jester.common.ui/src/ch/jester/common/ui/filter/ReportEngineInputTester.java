package ch.jester.common.ui.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

public class ReportEngineInputTester {

	public static Collection<?> getInputCollectionForReport(Class<?> reportBeanClass, List<?> selectedBeans){
		if(selectedBeans.isEmpty()){return selectedBeans;}
		if(selectedBeans.size()==1){
			return testSingleEntry(reportBeanClass, selectedBeans.get(0));
		}
		return testListEntries(reportBeanClass,selectedBeans);
	}

	private static Collection<?> testListEntries(Class<?> reportBeanClass,
			List<?> selectedBeans) {
		for(Object entry:selectedBeans){
			if(!reportBeanClass.isAssignableFrom(entry.getClass())){
				return null;
			}
		}
		return selectedBeans;
	}

	private static Collection<?> testSingleEntry(Class<?> reportBeanClass,
			Object object) {
		if(reportBeanClass.isAssignableFrom(object.getClass())){
			return returnAsList(object);
		}
		if(object instanceof IAdaptable){
			return testAdaptable( reportBeanClass, (IAdaptable)object);
		}
		return null;
	}
	
	private static Collection<?> testAdaptable(Class<?> reportBeanClass,
			IAdaptable object) {
			Object adapted = object.getAdapter(reportBeanClass);
			if(adapted!=null){
				returnAsList(adapted);
			}
		return null;
	}

	private static List<Object> returnAsList(Object o){
		List<Object> alist = new ArrayList<Object>();
		alist.add(o);
		return alist;
	}
}
