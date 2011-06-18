package ch.jester.common.ui.handlers;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.core.expressions.PropertyTester;

public class BeanStringPropertyTester extends PropertyTester {
	public final static String ID ="ch.jester.properties.beantest";
	public BeanStringPropertyTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		try {
			if(args.length==0){
				return false;
			}
			String xx = BeanUtils.getProperty(receiver, args[0].toString());
			boolean result = xx.equals(expectedValue.toString());
			return result;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
