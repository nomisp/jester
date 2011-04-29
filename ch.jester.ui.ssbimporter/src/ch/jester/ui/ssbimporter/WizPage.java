package ch.jester.ui.ssbimporter;


import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.util.ServiceUtility;

public class WizPage extends WizardPage {
	private Text text;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private ListViewer listViewer;

	private ServiceUtility mService = new ServiceUtility();
	
	private final Object NULL_INPUT = null;
	private ImportSelection mImportInput = new ImportSelection();
	/**
	 * @wbp.parser.constructor
	 */
	public WizPage(ISelection s) {
		super("wizardPage");
		System.out.println(s);
		setTitle("wizardPage"); //NON-NLS-1
		setDescription("Import a file from the local file system into the workspace"); //NON-NLS-1
	}
	
	
	/**
	 * Create the wizard.
	 */
	public WizPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Button mBtnBrowse = new Button(container, SWT.NONE);
		mBtnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.zip", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        text.setText(selected);
		        mImportInput.setSelectedZipFile(text.getText());
		        List<String> list = ZipUtility.getZipEntries(selected, false);
		        if(list!=null){
		        	checkboxTableViewer.setInput(list.toArray());
		        }
		        
			}


		});
		mBtnBrowse.setBounds(504, 20, 68, 23);
		mBtnBrowse.setText("Browse");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(10, 22, 468, 27);
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		
		table.addSelectionListener(new SelectionCountListener());
		
		table.setBounds(10, 66, 468, 157);
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list = listViewer.getList();
		list.setBounds(10, 250, 468, 85);
		
		listViewer.addSelectionChangedListener(new HandlerSelectionListener());
		
		
		
		listViewer.setContentProvider(new HandlerContentProvider());

		listViewer.setInput(NULL_INPUT);

		
		checkboxTableViewer.setContentProvider(new ZipEntryContentProvider());
		
		//initialer Status
		setPageComplete(false);
		
	}
	
	public ImportSelection getImportSelection(){
		return mImportInput;
	}
	
	private class HandlerContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {	

		}

		@Override
		public Object[] getElements(Object inputElement) {
			IImportManager manager = mService.getService(IImportManager.class);
			if(manager==null){
				return new Object[]{};
			}
			List<IImportHandlerEntry> handlers = null;
			if(text.getText().length()==0 || inputElement==null){
				handlers = manager.getRegistredEntries();
			}else if(inputElement instanceof String){
				String input = (String) inputElement;
				if(input.lastIndexOf(".")==-1){
					return new Object[]{};
				}
				String extension = input.substring(input.lastIndexOf(".")+1);
				
				handlers = manager.filter(manager.createMatchingExtension(extension));
			}else{
				handlers = manager.getRegistredEntries();
			}
			return handlers.toArray();
		}
	}

	private class HandlerSelectionListener implements ISelectionChangedListener {
		SelectionUtility su = new SelectionUtility(null);

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			mImportInput.setSelectedHandlerEntry(su.getFirstSelectedAs(IImportHandlerEntry.class));
		}
	}

	class ImportSelection{
		private IImportHandlerEntry entry;
		private String zipFile;
		private String zipEntry;
		
		private void setSelectedHandlerEntry(IImportHandlerEntry pEntry){
			entry=pEntry;
			checkState();
		}
		private void setSelectedZipFile(String pZipFile){
			zipFile=pZipFile;
			checkState();
		}
		private void setSelectedZipEntry(String pEntry){
			zipEntry = pEntry;
			checkState();
		}
		
		public IImportHandlerEntry getSelectedHandlerEntry(){
			return entry;
		}
		public String getSelectedZipFile(){
			return zipFile;
		}
		public String getSelectedZipEntry(){
			return zipEntry;
		}
		private void checkState(){
			if(entry!=null&&zipFile!=null&&zipEntry!=null){
				setPageComplete(true);
			}else{
				setPageComplete(false);
			}
		}
	}
	
	/**
	 * SelectionListener: Welche TableItems sind angegklickt
	 *
	 */
	private class SelectionCountListener implements SelectionListener {
		TableItem lastChecked = null;
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(e.item instanceof TableItem){
				TableItem titem = (TableItem) e.item;
				boolean checked = titem.getChecked();
				if(checked){
					if(lastChecked!=null && lastChecked!=titem){
						lastChecked.setChecked(false);
					}
					lastChecked=titem;
					mImportInput.setSelectedZipEntry(titem.getText());
					listViewer.setInput(titem.getText());
				
				}else{
					mImportInput.setSelectedZipEntry(null);
					mImportInput.setSelectedHandlerEntry(null);
					listViewer.setInput(NULL_INPUT);
				}
			}

		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}
	}
	
	private class ZipEntryContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ZipUtility.getZipEntries(text.getText(), false).toArray();
		}
	}
}
