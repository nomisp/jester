package ch.jester.model.util;

import java.lang.reflect.Method;

import javax.persistence.Column;

import ch.jester.common.utility.Reflections;



/**
 * UtilityKlasse welche Column Annotations liest.
 *
 */
public class PropertyAttributesAnalyser extends AnnotationReader<Column>{

	public PropertyAttributesAnalyser(Class<?> pClass) {
		super(pClass);
		analyze(Column.class);
		
	}
	/**
	 * Gibt die Länge der Column zurück
	 * @param pProperty
	 * @return
	 */
	public int getLength(String pProperty){
		Column col = super.getAnnotation(pProperty);
		if(col==null){throw new IllegalArgumentException("PropertyInvalid: "+pProperty);}
		return col.length();
	}
	
	/**
	 * Gibt den Return Werttyp zurück
	 * @param pProperty
	 * @return
	 */
	public Class<?> getType(String pProperty){
		Method m = Reflections.getGetterMethod(mClass, pProperty);
		if(m==null){throw new IllegalArgumentException("Method not definied");}
		return m.getReturnType();
	}

}
