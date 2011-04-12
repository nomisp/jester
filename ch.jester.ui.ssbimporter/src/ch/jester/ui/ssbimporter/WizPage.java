package ch.jester.ui.ssbimporter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
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
import ch.jester.common.utility.ServiceUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import org.eclipse.jface.viewers.ListViewer;

public class WizPage extends WizardPage {
	private Text text;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private ListViewer listViewer;
	private List<String> mAllEntries = new ArrayList<String>();
	private List<String> mSelectedEntries = new ArrayList<String>();
	private IImportHandlerEntry selectedEntry;
	private ImportInput mImportInput = new ImportInput();
	ServiceUtility mService = new ServiceUtility();
	/**
	 * @wbp.parser.constructor
	 */
	public WizPage(String pageName) {
		super(pageName);
		
		setTitle(pageName); //NON-NLS-1
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
		        mImportInput.zipFile=text.getText();
		        List<String> list = ZipUtility.getZipEntries(selected, false);
		        if(list!=null){
		        	mAllEntries.clear();
		        	mAllEntries.addAll(list);
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
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				su.setSelection(event.getSelection());
				selectedEntry = su.getFirstSelectedAs(IImportHandlerEntry.class);
				mImportInput.entry=selectedEntry;
			}
		});
		
		
		listViewer.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				IImportManager manager = mService.getService(IImportManager.class);
				List<IImportHandlerEntry> handlers = null;
				if(text.getText().length()==0 || inputElement==null){
					handlers = manager.getRegistredImportHandlers();
				}else if(inputElement instanceof String){
					String input = (String) inputElement;
					if(input.lastIndexOf(".")==-1){
						return new Object[]{};
					}
					String extension = input.substring(input.lastIndexOf(".")+1);
					handlers = manager.filter(manager.createMatchingExtension(extension));
				}else{
					handlers = manager.getRegistredImportHandlers();
				}
				return handlers.toArray();
			}
			
			
		});
		listViewer.setInput(new Object());

		
		checkboxTableViewer.setContentProvider(new TableContentProvider());
		
		
		
		setPageComplete(false);
		
	}
	
	public ImportInput getInputToProcess(){
		return mImportInput;
	}
	
	public List<String> getZipFilesToImport(){
		return mSelectedEntries;
	}
	
	public IImportHandlerEntry getSelectedImportHandlerEntry(){
		return selectedEntry;
	}
	
	class ImportInput{
		IImportHandlerEntry entry;
		String zipFile;
		String zipEntry;
	}
	
	/**
	 * SelectionListener: Welche TableItems sind angegklickt
	 *
	 */
	class SelectionCountListener implements SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(e.item instanceof TableItem){
				TableItem titem = (TableItem) e.item;
				boolean checked = titem.getChecked();
				if(checked){
					mSelectedEntries.add(titem.getText());
					mImportInput.zipEntry=titem.getText();
					listViewer.setInput(titem.getText());
				}else{
					mSelectedEntries.remove(titem.getText());
					listViewer.setInput(new Object());
				}
			}
			setPageComplete(!(mSelectedEntries.isEmpty()));

		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}
		
		public List<String> getSelectedEntries(){
			return mSelectedEntries;
		}
	}
	
	class TableContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return mAllEntries.toArray();
		}
			
	
	
	}
}
