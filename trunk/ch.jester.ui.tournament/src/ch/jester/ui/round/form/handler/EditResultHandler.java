package ch.jester.ui.round.form.handler;

import org.eclipse.core.commands.ExecutionEvent;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.RoundEditor;

public class EditResultHandler extends AbstractCommandHandler {
	
	@Override
	public Object executeInternal(ExecutionEvent event) {
		Object selected = mSelUtility.getFirstSelectedAs(Round.class);
		System.out.println(selected);
		if(selected!=null){
			openEditor(selected, RoundEditor.ID);
			return null;
		}
		selected = mSelUtility.getFirstSelectedAs(Category.class);
		if(selected!=null){
			openEditor(selected, RoundEditor.ID);
			return null;
		}
		return null;
	}

}
