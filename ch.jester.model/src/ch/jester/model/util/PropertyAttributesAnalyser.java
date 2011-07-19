package ch.jester.model.util;

import java.lang.reflect.Method;

import javax.persistence.Column;

import ch.jester.common.utility.Reflections;



public class PropertyAttributesAnalyser extends AnnotationReader<Column>{

	public PropertyAttributesAnalyser(Class<?> pClass) {
		super(pClass);
		analyze(Column.class);
		
	}
	public int getLength(String pProperty){
		Column col = super.getAnnotation(pProperty);
		if(col==null){throw new IllegalArgumentException("PropertyInvalid: "+pProperty);}
		return col.length();
	}
	
	public Class<?> getType(String pProperty){
		Method m = Reflections.getGetterMethod(mClass, pProperty);
		if(m==null){throw new IllegalArgumentException("Method not definied");}
		return m.getReturnType();
	}

}
