package ch.jester.ui.tournament;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

import ch.jester.common.ui.editor.IEditorDaoInputAccess;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

public class LinkHelper3 implements ILinkHelper {

	@Override
	public IStructuredSelection findSelection(IEditorInput anInput) {
		//System.out.println(anInput);
		return createSelection((IEditorDaoInputAccess<?>) anInput);
	}

	private IStructuredSelection createSelection(IEditorDaoInputAccess<?> anInput) {
		Object input = anInput.getInput();
		List<Object> path = new ArrayList<Object>();
		if(input instanceof Round){
			Round round = (Round) input;
			Category cat = round.getCategory();
			Tournament t = cat.getTournament();
			path.add(t);
			path.add(cat);
			path.add(round);
			return new StructuredSelection(path);
		}
		return null;
	}

	@Override
	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		//System.out.println(aPage);

	}

}
