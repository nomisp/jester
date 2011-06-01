package ch.jester.ui.ssbimporter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
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

import ch.jester.common.utility.AdapterUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportAttributeMatcher;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.ITestableImportHandler;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import ch.jester.ui.ssbimporter.PlayerImportWizardPage.ImportSelection;

public class PropertyChooserWizardPage extends WizardPage{
	Button btnAddCol;
	private ImportSelection mSelection;
	private Table table_2;
	private Table table_3;
	TableViewer mInputTableViewer; 
	TableViewer mMatchingTableViewer ;
	String[] values;
	List<TableColumn> inputCols = new ArrayList<TableColumn>();
	List<String[]> inputContent = new ArrayList<String[]>();
	HashMap<String, String> mLinking;
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
				values = getInputAttributes();
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
				dialog.setInputText(PropertyChooserWizardPage.this.getVirtualTableProvider().getDynamicInput(20));
				dialog.setCell(PropertyChooserWizardPage.this.getVirtualTableProvider().getCells());
				int ret = dialog.open();
				if(IDialogConstants.OK_ID!=ret){return;}
				List<IVirtualCell> cell = dialog.getCell();
				getVirtualTableProvider().clearCells();
				for(IVirtualCell c:cell){
					getVirtualTableProvider().addCell(c);
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
	

	public void setInput(ImportSelection importSelection) {
		mSelection = importSelection;
	}
	private void parse() {
		String[] headers = getInputAttributes();
		for(TableColumn c:inputCols){
			c.dispose();
		}
		
		for(String h:headers){
			TableViewerColumn tableViewerColumn = new TableViewerColumn(mInputTableViewer, SWT.NONE);
			inputCols.add(tableViewerColumn.getColumn());
			TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
			tblclmnNewColumn.setWidth(70);
			tblclmnNewColumn.setText(h);
			
		}

		mInputTableViewer.setInput(null);
		mInputTableViewer.setLabelProvider(new InputTableViewerLabelProvider());
		mInputTableViewer.setInput(getContent(headers.length));
		
		mLinking = (HashMap<String, String>) getPredefiniedLinking().clone();
		mMatchingTableViewer.setInput(getDomainAttributes());
		btnAddCol.setEnabled(canAddColumns());

	}
	@SuppressWarnings("rawtypes")
	public List<String[]> getContent(int pLength){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable access = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		
		inputContent.clear();
		for(int i=1;i<20;i++){
			Object o = access.getRow(i);
			if(o!=null){
				String[] rowcontent = access.processRow(o, pLength);
				inputContent.add(rowcontent);
			}
		}
		
		
		return inputContent;

	}
	
	public HashMap<String, String> getLinking(){
		return mLinking;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean canAddColumns(){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable virtual = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return virtual.canAddCells();
	}
	@SuppressWarnings("rawtypes")
	private IVirtualTable getVirtualTableProvider(){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IVirtualTable virtual = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return virtual;
	}
	
	@SuppressWarnings("rawtypes")
	private String[] getInputAttributes(){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		ITestableImportHandler testableHandler = AdapterUtility.getAdaptedObject(handler, ITestableImportHandler.class);
		final InputStream instream = ZipUtility.getZipEntry(mSelection.getSelectedZipFile(), mSelection.getSelectedZipEntry());
		testableHandler.handleImport(instream,20, new NullProgressMonitor());
		IVirtualTable access = AdapterUtility.getAdaptedObject(handler, IVirtualTable.class);
		return access.getHeaderEntries();
	}
	@SuppressWarnings("rawtypes")
	private String[] getDomainAttributes(){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IImportAttributeMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportAttributeMatcher.class);
		return attributehandler.getDomainObjectAttributes();
	}
	@SuppressWarnings("rawtypes")
	private HashMap<String, String> getPredefiniedLinking(){
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IImportAttributeMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportAttributeMatcher.class);
		return attributehandler.getInputLinking();
		
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

	@SuppressWarnings("rawtypes")
	public void applyChanges() {
		if(mLinking==null){return;}
		IImportHandler handler = mSelection.getSelectedHandlerEntry().getService();
		IImportAttributeMatcher attributehandler = AdapterUtility.getAdaptedObject(handler, IImportAttributeMatcher.class);
		attributehandler.setInputLinking(mLinking);
		
	}

	public void init() {
		parse();
	}
}
