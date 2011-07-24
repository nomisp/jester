package ch.jester.common.ui.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

			if(args.length==0){
				return false;
			}
			try{
			String xx = BeanUtils.getProperty(receiver, args[0].toString());
			boolean result = xx.equals(expectedValue.toString());
			return result;
			}catch(Exception e){	
			}

			try {
				Method method = 	receiver.getClass().getMethod(args[0].toString(), new Class[]{});
				Object result = method.invoke(receiver, new Object[]{});
				if(result == expectedValue){
					return true;
				}
				boolean res = result.toString().equals(expectedValue.toString());
				return res;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return false;
		
	}

}
