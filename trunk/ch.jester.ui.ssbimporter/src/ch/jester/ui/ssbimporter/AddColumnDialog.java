package ch.jester.ui.ssbimporter;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.MCHITTESTINFO;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.importer.IVirtualTable;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.core.databinding.beans.PojoObservables;

public class AddColumnDialog extends Dialog implements SelectionListener {
	private DataBindingContext m_bindingContext;
	private Text text;
	private List<IVirtualCell> cells = new ArrayList<IVirtualCell>();
	private Text txtAttributeName;
	private Table table;
	TableViewer tableViewer;
	private Text txtDelim;
	private IVirtualCell selectedCell;
	private Text txtSeq;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddColumnDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		
		txtAttributeName = new Text(container, SWT.BORDER);
		txtAttributeName.setBounds(128, 7, 373, 21);
		
		Label lblAttributeName = new Label(container, SWT.NONE);
		lblAttributeName.setBounds(10, 10, 101, 15);
		lblAttributeName.setText("Attribute Name");
		
		Label lblInputSelection = new Label(container, SWT.NONE);
		lblInputSelection.setBounds(10, 38, 418, 15);
		lblInputSelection.setText("Input Selection (mark Text and right click to create Attribute)");
		
		text = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setFont(SWTResourceManager.getFont("Courier", 9, SWT.NORMAL));
		text.setBounds(10, 59, 493, 124);
		
		Menu menu = new Menu(text);
		text.setMenu(menu);
		
		MenuItem mntmAddColumn = new MenuItem(menu, SWT.NONE);
		mntmAddColumn.setText("new Attribute");
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setBounds(10, 221, 200, 146);
		
	
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnAttribute = tableViewerColumn.getColumn();
		tblclmnAttribute.setWidth(100);
		tblclmnAttribute.setText("Attribute");
		
		Menu menu_1 = new Menu(table);
		table.setMenu(menu_1);
		
		
		MenuItem mntmDelete = new MenuItem(menu_1, SWT.NONE);
		mntmDelete.setText("Delete");
		mntmDelete.addSelectionListener(new SelectionListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = tableViewer.getSelection();
				if(selection==null){return;}
				su.setSelection(selection);
				IVirtualCell c = su.getFirstSelectedAs(IVirtualCell.class);
				cells.remove(c);
				tableViewer.setInput(cells);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		Label lblDefinedAttributes = new Label(container, SWT.NONE);
		lblDefinedAttributes.setBounds(10, 200, 135, 15);
		lblDefinedAttributes.setText("Defined Attributes");
		
		txtDelim = new Text(container, SWT.BORDER);
		txtDelim.setBounds(291, 218, 76, 21);
		txtDelim.setEnabled(false);
		txtDelim.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				getSelectedCell().setDelimiter(txtDelim.getText());
				
			}
		});
		
		Label lblDelimiter = new Label(container, SWT.NONE);
		lblDelimiter.setBounds(230, 221, 55, 15);
		lblDelimiter.setText("Delimiter");
		
		Label lblMatch = new Label(container, SWT.NONE);
		lblMatch.setBounds(230, 248, 55, 15);
		lblMatch.setText("Sequence");
		
		txtSeq = new Text(container, SWT.BORDER);
		txtSeq.setBounds(291, 245, 76, 21);
		txtSeq.setEnabled(false);
		txtSeq.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				int i  = 0;
				try{
					i =  Integer.parseInt(txtSeq.getText());
				}catch(NumberFormatException e0){
					return;
				}
				getSelectedCell().setDelimiterSequence(i);
				
			}
		});
		
		mntmAddColumn.addSelectionListener(this);

		tableViewer.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				return ((IVirtualCell)element).getName();
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				su.setSelection(event.getSelection());
				IVirtualCell c = su.getFirstSelectedAs(IVirtualCell.class);
				selectedCell=c;
				if(c==null){
					txtDelim.setEnabled(false);
					txtSeq.setEnabled(false);
					return;
				}
				txtDelim.setEnabled(true);
				txtSeq.setEnabled(true);
				if(c.getDelimiter()!=null){
					txtDelim.setText(c.getDelimiter());
				}else{
					txtDelim.setText("");
				}
				if(c.getDelimiterSequence()!=-1){
					txtSeq.setText(c.getDelimiterSequence()+"");
				}else{
					txtSeq.setText("");
				}
				
			}
		});
		
		
		return container;
	}

	public IVirtualCell getSelectedCell(){
		return selectedCell;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(566, 472);
	}
	
	public void setInputText(String[] pInput){
		for(String s:pInput){
			text.append(s);
			text.append("\n");
		}
		//text.setText(pInput);
	}
	
	public String getAttributeName(){
		return txtAttributeName.getText();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		String selection  = text.getSelectionText();
		System.out.println(selection);
		String entireText = text.getText();
		int start = entireText.indexOf(selection);
		int i = start;
		for(;i>=0;i--){
			if(entireText.charAt(i)=='\n'){
				break;
			}
		}
		int lineStart = i;
		final int cellStart = start - lineStart-1;
		final int cellStop = cellStart + selection.length();
		final String name = txtAttributeName.getText();
		//System.out.println("cellStart = "+cellStart+"; cellStop = "+cellStop+" --> ");
		if(name.equals("")){
			throw new RuntimeException("Name eintragen!!!! Noch keine korrektes Handling ohne Name implementiert");
		}
		IVirtualCell c = new VirtualCell(name, cellStart, cellStop);
		cells.add(c);
		AddColumnDialog.this.tableViewer.setInput(cells);
	}
	
	public List<IVirtualCell> getCell(){
		return cells;
	}
	
	public void setCell(List<IVirtualCell> cells){
		this.cells.addAll(cells);
		this.tableViewer.setInput(cells);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		//System.out.println(e);
		
	}
	
	class VirtualCell implements IVirtualCell {
		String delim, mName;
		int seq = -1, cellStart, cellStop;
		public VirtualCell(String name, int pCellStart, int pCellStop) {
			cellStart=pCellStart;
			cellStop=pCellStop;
			mName = name;
		}
		
		@Override
		public void createCellContent(List<String> detailList, String pInput) {
			String src = pInput.substring(cellStart, cellStop);
			if(delim==null){
				detailList.add(src);
			}else{
				if(src.startsWith(delim)){
					src = " "+src;
				}
				StringTokenizer token = new StringTokenizer(src, delim);
				for(int i=0;i<seq;i++){
					if(token.hasMoreTokens()){
						token.nextToken();
					}
				}
				if(token.hasMoreTokens()){
					detailList.add(token.nextToken());
				}else{
					detailList.add("");
				}
			}
		}

		@Override
		public String getName() {
			return mName;
			
		}

		@Override
		public void setDelimiter(String pDelim) {
			delim=pDelim;
			
		}

		@Override
		public String getDelimiter() {
			return delim;
		}

		@Override
		public int getDelimiterSequence() {
			return seq;
		}

		@Override
		public void setDelimiterSequence(int i) {
			seq=i;
			
		}
	};
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
