package ch.jester.ui.ssbimporter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
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

import ch.jester.common.utility.ZipUtility;

public class WizPage extends WizardPage {
	private Text text;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private List<String> mAllEntries = new ArrayList<String>();
	private List<String> mSelectedEntries = new ArrayList<String>();

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
		        
		        List<String> list = ZipUtility.getZipEntries(selected);
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
		text.setBounds(10, 22, 468, 19);
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		
		table.addSelectionListener(new SelectionCountListener());
		
		table.setBounds(10, 66, 386, 211);
		checkboxTableViewer.setContentProvider(new TableContentProvider());
		
		
		
		setPageComplete(false);
		
	}
	
	public List<String> getZipFilesToImport(){
		return mSelectedEntries;
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
				}else{
					mSelectedEntries.remove(titem.getText());
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
