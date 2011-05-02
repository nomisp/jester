package ch.jester.ui.ssbimporter;


import java.io.IOException;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IContentProvider;
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
import ch.jester.common.web.ExtensionFilter;
import ch.jester.common.web.Link;
import ch.jester.common.web.LinkFilter;
import ch.jester.common.web.PageReader;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.util.ServiceUtility;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

public class ZipPlayerImporter extends WizardPage {
	private Text text;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private ListViewer listViewer;

	private ServiceUtility mService = new ServiceUtility();
	
	private final Object NULL_INPUT = null;
	private ImportSelection mImportInput = new ImportSelection();
	
	private final WebAddress fide = new WebAddress();
	private final WebAddress ssb = new WebAddress();
	
	private void init(){
		fide.mText = "FIDE";
		fide.mURL = "http://ratings.fide.com/download.phtml";
		ssb.mText = "SSB";
		ssb.mURL = "http://www.schachbund.ch/schachsport/fldownload.php";
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public ZipPlayerImporter(ISelection s) {
		super("Import Zip");
		System.out.println(s);
		setTitle("ziiiip"); //NON-NLS-1
		setDescription("Import Players from a ZIP into jester"); //NON-NLS-1
		init();
	}
	
	
	/**
	 * Create the wizard.
	 */
	public ZipPlayerImporter() {
		super("Import Zip");
		setTitle("ziiiip");
		setDescription("Import Players from a ZIP into jester"); //NON-NLS-1
		init();
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
		mBtnBrowse.setBounds(484, 62, 57, 23);
		mBtnBrowse.setText("Browse");
		
		text = new Text(container, SWT.BORDER);
		text.setMessage("Select a Zip file");
		text.setBounds(9, 64, 469, 20);
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		
		table.addSelectionListener(new SelectionCountListener());
		
		table.setBounds(10, 204, 468, 112);
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list = listViewer.getList();
		list.setBounds(10, 340, 469, 72);
		//webCombo.set
		
		Label lblSource = new Label(container, SWT.NONE);
		lblSource.setBounds(10, 10, 49, 13);
		lblSource.setText("Source");
		
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
	
	private List<Link> populate(WebAddress pwa) throws IOException {
		if(pwa==fide){
			PageReader reader = new PageReader();
			LinkFilter linkfilter;
			reader.setFilter(new ExtensionFilter(".zip", linkfilter=LinkFilter.createFIDEFilter()));
			reader.readPage(pwa.mURL);
			List<Link> links = linkfilter.getLinks();
			return links;
		
		}else{
			PageReader reader = new PageReader();
			reader.setDownloadRoot("http://www.schachbund.ch/schachsport");
			LinkFilter linkfilter = LinkFilter.createSSBFilter();
			reader.setFilter(new ExtensionFilter( ".zip", linkfilter));
			reader.readPage(pwa.mURL);
			List<Link> links = linkfilter.getLinks();
			return links;
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
	
	
	
	
	private class WebAddress{
		String mText;
		String mURL;
		public String toString(){
			return mText;
		}
	}
}
