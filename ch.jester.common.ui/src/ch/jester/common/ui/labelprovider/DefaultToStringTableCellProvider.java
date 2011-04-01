package ch.jester.common.ui.labelprovider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class DefaultToStringTableCellProvider extends CellLabelProvider{
	@Override
	public void update(ViewerCell cell) {
		cell.setText(cell.getElement().toString());
	}

}
