package ch.jester.ui.forms;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class TestValidator implements IValidator {

	@Override
	public IStatus validate(Object value) {
		if(value!=null && value.getClass()!=Integer.class){
			return new Status(Status.ERROR, "", "Not a Number");
		}
		return Status.OK_STATUS;
	}

}
