package ch.jester.ui.forms;


import org.eclipse.core.databinding.conversion.Converter;

import ch.jester.model.util.Title;


public class Title2StringConverter extends Converter{

	public Title2StringConverter() {
		super(String.class, Title.class);
	}

	@Override
	public Object convert(Object fromObject) {
		if(fromObject==null){return "";}
		if(fromObject instanceof Title){
			return fromObject.toString();
		}
		if(fromObject instanceof String){
			return Title.valueOf(fromObject.toString());
		}
		return null;
	}

}
