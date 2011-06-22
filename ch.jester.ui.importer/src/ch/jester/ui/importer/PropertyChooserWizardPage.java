package ch.jester.ui.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.jester.common.utility.ExceptionUtility;
import ch.jester.common.utility.ExceptionWrapper;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.ui.importer.internal.Controller;
import ch.jester.ui.importer.internal.ImportData;
import ch.jester.ui.importer.internal.ParseController;



public class PropertyChooserWizardPage extends WizardPage{
	Button btnAddCol;
	private ImportData mSelection;
	private Table table_2;
	private Table table_3;
	TableViewer mInputTableViewer; 
	TableViewer mMatchingTableViewer ;
	String[] values;
	List<TableColumn> inputCols = new ArrayList<TableColumn>();
	//List<String[]> inputContent = new ArrayList<String[]>();
	HashMap<String, String> mLinking;
	private ParseController mParseController = ParseController.getController();
	
	protected PropertyChooserWizardPage() {
		super("Property Page");
		setTitle("Property Matching"); //NON-NLS-1
		setDescription("Match Columns to player attributes"); //NON-NLS-1
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(null);
		
		Label lblInput = new Label(container, SWT.NONE);
		lblInput.setBounds(5, 5, 122, 15);
		lblInput.setText("Input Preview");
		
		mInputTableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		
		table_2 = mInputTableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		table_2.setBounds(5, 26, 559, 95);
		
		
	
		
		mMatchingTableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table_3 = mMatchingTableViewer.getTable();
		table_3.setLinesVisible(true);
		table_3.setHeaderVisible(true);
		table_3.setBounds(5, 187, 301, 220);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(mMatchingTableViewer, SWT.NONE);
		TableColumn tblClInputAttribute = tableViewerColumn_1.getColumn();
		tblClInputAttribute.setWidth(100);
		tblClInputAttribute.setText("Player Attribute");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(mMatchingTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("Input Columns");
		
	
		
		tableViewerColumn_2.setEditingSupport(new EditingSupport(mMatchingTableViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				Integer integer = (Integer) value;
				String newvalue = values[integer];
				mLinking.put(element.toString(), newvalue);
				mMatchingTableViewer.refresh();
				
			}
			
			@Override
			protected Object getValue(Object element) {
				String match = mLinking.get(element);
				for(int i=0;i<values.length;i++){
					if(values[i].equals(match)){
						return i;
					}
				}
				
				return 0;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				values =mParseController.getInputAttributes();
				String[] val = new String[values.length+1];
				System.arraycopy(values, 0, val, 1, values.length);
				val[0]="";
				values = val;
				return new ComboBoxCellEditor(mMatchingTableViewer.getTable(), values);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
		btnAddCol = new Button(container, SWT.NONE);
		btnAddCol.setBounds(5, 125, 109, 25);
		btnAddCol.setText("Modify Columns");
		btnAddCol.setEnabled(false);
		
	this.btnAddCol.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			

			@Override
			public void mouseDown(MouseEvent e) {
				AddColumnDialog dialog = new AddColumnDialog(Display.getDefault().getActiveShell());
				dialog.create();
				dialog.setBlockOnOpen(true);
				dialog.setInputText(mParseController.getVirtualTableProvider().getDynamicInput(20));
				dialog.setCell(mParseController.getVirtualTableProvider().getCells());
				int ret = dialog.open();
				if(IDialogConstants.OK_ID!=ret){return;}
				List<IVirtualCell> cell = dialog.getCell();
				mParseController.getVirtualTableProvider().clearCells();
				for(IVirtualCell c:cell){
					mParseController.getVirtualTableProvider().addCell(c);
				}
				parse();
			
				
				
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

		});
		
		mInputTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		mMatchingTableViewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				return (String[])inputElement;
			}
		});
	
		mMatchingTableViewer.setLabelProvider(new MatchingTableViewerLabelProvider());
	
	
	}
	

	public void setInput(ImportData importSelection) {
		mSelection = importSelection;
		mParseController.setData(mSelection);
	}
	
	
	@SuppressWarnings("unchecked")
	private void parse() {
		String[] headers = mParseController.getInputAttributes();
		if(headers.length==0){return;}
		resetTableColumns();
		
		for(String h:headers){
			TableViewerColumn tableViewerColumn = new TableViewerColumn(mInputTableViewer, SWT.NONE);
			inputCols.add(tableViewerColumn.getColumn());
			TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
			tblclmnNewColumn.setWidth(70);
			tblclmnNewColumn.setText(h);
			
		}

		mLinking = (HashMap<String, String>) mParseController.getPredefiniedLinking().clone();
		mInputTableViewer.setInput(null);
		mInputTableViewer.setLabelProvider(new InputTableViewerLabelProvider());
		mInputTableViewer.setInput(mParseController.getContent(headers.length));
		mMatchingTableViewer.setInput(mParseController.getDomainAttributes());
		btnAddCol.setEnabled(mParseController.canAddColumns());

	}
	private void resetTableColumns(){
		for(TableColumn c:inputCols){
			c.dispose();
		}
	}
	private void reset(){
		resetTableColumns();
		mInputTableViewer.setInput(null);
		mMatchingTableViewer.setInput(null);
		btnAddCol.setEnabled(false);
	}	


	
	class MatchingTableViewerLabelProvider implements ITableLabelProvider{

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if(columnIndex==0){
				return element.toString();
			}
			Object o =  mLinking.get(element);
			if(o==null){return "";};
			return o.toString();
		}
		
	}
	
	class InputTableViewerLabelProvider implements ITableLabelProvider {
		
		@Override
		public void removeListener(ILabelProviderListener listener) {
		}
		
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		
		@Override
		public void dispose() {
		}
		
		@Override
		public void addListener(ILabelProviderListener listener) {
		}
		
		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((String[])element)[columnIndex];
		}
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	public void applyChanges() {
		if(mLinking==null){return;}
		mParseController.getImportAttributeMatcher().setInputLinking(mLinking);
	}
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible){
			init();
		}
	}
	
	public void init() {
		SafeRunner.run(new ISafeRunnable() {
			
			@Override
			public void run() throws Exception {
				Controller.getController().setImportPossible(false);
				setPageComplete(false);
				PropertyChooserWizardPage.this.getContainer().updateButtons();
				setErrorMessage(null);
				reset();
				parse();
				Controller.getController().setImportPossible(true);
				setPageComplete(true);
				PropertyChooserWizardPage.this.getContainer().updateButtons();
			}
			
			@Override
			public void handleException(Throwable exception) {
				Controller.getController().setImportPossible(false);
				setPageComplete(false);
				PropertyChooserWizardPage.this.getContainer().updateButtons();
				ExceptionWrapper ew = ExceptionUtility.wrap(exception, ProcessingException.class);
				setErrorMessage(ew.getThrowableMessage());
			}
		});
	}
	
	
}
