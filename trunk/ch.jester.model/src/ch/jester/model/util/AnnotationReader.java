package ch.jester.model.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.jester.common.utility.Reflections;



/**
 * UtilityKlasse um Annotiations zu lesen.
 *
 * @param <T>
 */
public class AnnotationReader< T extends Annotation> {
	protected Class<?> mClass;
	protected Class<T> mAnnotationClass;
	protected Map<String, T> mAnnotatedFields = new HashMap<String, T>();
	public AnnotationReader(Class<?> pClass){
		mClass = pClass;
	}
	public void analyze(Class<T> c){
		mAnnotationClass =  c;
		index();
	}
	private void index() {
		List<Field> fields = Reflections.getFields(mClass, mAnnotationClass);
		for(Field f:fields){
			mAnnotatedFields.put(f.getName(), f.getAnnotation(mAnnotationClass));
		}
	}
	protected T getAnnotation(String pProperty){
		return mAnnotatedFields.get(pProperty);
	}
}
