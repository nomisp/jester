package ch.jester.ui.ssbimporter;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.adapters.StructuredContentProviderAdapter;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ZipUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.ILink;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.util.ServiceUtility;

public class PlayerImportWizardPage extends WizardPage {
	private Text text;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private ListViewer listViewer;

	private ServiceUtility mService = new ServiceUtility();
	
	private final Object NULL_INPUT = null;
	private ImportSelection mImportInput = new ImportSelection();

	
	//Radio
	private Button rdZip;
	private Button rdWeb;
	private WebRadioSelectionListener mWebRadioListener = new WebRadioSelectionListener();
	private ZipRadioSelectionListener mZipRadioListener = new ZipRadioSelectionListener();
	private Combo comboProvider;
	private Combo comboDownload;
	
	private Button mBtnBrowse;
	private ComboViewer comboProviderViewer;
	private ComboViewer comboDownloadViewer;
	
	private ImportHandlerManager ihm = new ImportHandlerManager();
	
	private boolean enableWebOptions = false;
	/**
	 * @wbp.parser.constructor
	 */
	public PlayerImportWizardPage(ISelection s) {
		super("Import Player");
		System.out.println(s);
		setTitle("Import"); //NON-NLS-1
		setDescription("Import Players into jester"); //NON-NLS-1
	}
	
	
	/**
	 * Create the wizard.
	 */
	public PlayerImportWizardPage() {
		super("Import Player");
		setTitle("Import"); //NON-NLS-1
		setDescription("Import Players into jester"); //NON-NLS-1
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		mBtnBrowse = new Button(container, SWT.NONE);
		mBtnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.zip", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        if(selected==null){return;}
		        text.setText(selected);
		        mImportInput.setSelectedZipFile(text.getText());
		        //List<String> list = ZipUtility.getZipEntries(selected, false);
		        if(text.getText()!=null){
		        	checkboxTableViewer.setInput(text.getText());
		        }
		        
			}


		});
		mBtnBrowse.setBounds(588, 13, 57, 23);
		mBtnBrowse.setText("Browse");
		
		text = new Text(container, SWT.BORDER);
		text.setMessage("Select a Zip file");
		text.setBounds(175, 11, 407, 23);
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		
		table.addSelectionListener(new SelectionCountListener());
		
		table.setBounds(175, 128, 407, 112);
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list = listViewer.getList();
		list.setBounds(175, 268, 407, 72);
		//webCombo.set
		

		
	    comboProviderViewer = new ComboViewer(container, SWT.NONE);
		comboProvider = comboProviderViewer.getCombo();
		comboProvider.setBounds(252, 53, 330, 29);
		comboProviderViewer.setContentProvider(new WebImportAdapterProvider());
		comboProviderViewer.addSelectionChangedListener(new WebProviderSelectionListener());
		comboProviderViewer.setInput(null);
		
		
		Label lblProvider = new Label(container, SWT.NONE);
		lblProvider.setBounds(175, 56, 73, 21);
		lblProvider.setText("Provider");
		
		comboDownloadViewer = new ComboViewer(container, SWT.NONE);
		comboDownload = comboDownloadViewer.getCombo();
		comboDownload.setBounds(252, 82, 330, 29);
		comboDownloadViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboDownloadViewer.addSelectionChangedListener(new DownloadListener());
		
		Label lblSource = new Label(container, SWT.NONE);
		lblSource.setBounds(10, 14, 65, 29);
		lblSource.setText("Source");
		
		//Radios
		rdZip = new Button(container, SWT.RADIO);
		rdZip.setBounds(92, 10, 65, 29);
		rdZip.setText("Zip");
		rdZip.addSelectionListener(mZipRadioListener);
		
	    rdWeb = new Button(container, SWT.RADIO);
		rdWeb.setText("Web");
		rdWeb.setBounds(92, 52, 65, 29);
		rdWeb.addSelectionListener(mWebRadioListener);
		
		rdZip.setSelection(true);
		mZipRadioListener.enable(true);
		
		mWebRadioListener.enable(false);
		//
		
		
		
		Label lblDownload = new Label(container, SWT.NONE);
		lblDownload.setBounds(175, 85, 73, 21);
		lblDownload.setText("Download");
		
		Label lblImportHandler = new Label(container, SWT.NONE);
		lblImportHandler.setBounds(10, 128, 154, 21);
		lblImportHandler.setText("Content");
		
		Label lblHandler = new Label(container, SWT.NONE);
		lblHandler.setText("Handler");
		lblHandler.setBounds(10, 268, 154, 21);
		
		listViewer.addSelectionChangedListener(new HandlerSelectionListener());
		
		
		
		listViewer.setContentProvider(new ImportHandlerProvider());
		listViewer.setLabelProvider(new LabelProvider(){
			
			@Override
			public String getText(Object element) {
				if(element instanceof IWebImportHandlerEntry){
					IWebImportHandlerEntry entry= (IWebImportHandlerEntry) element;
					return entry.getDescription();
				}
				return element.toString();
			}
		});

		listViewer.setInput(NULL_INPUT);

		
		checkboxTableViewer.setContentProvider(new ZipEntryContentProvider());
		
		//initialer Status
		setPageComplete(false);
		
		enableWebOptions = mService.getService(IPingService.class).isConnected();
		rdWeb.setEnabled(enableWebOptions);
	}
	
	public ImportSelection getImportSelection(){
		return mImportInput;
	}
	
	private class DownloadListener implements ISelectionChangedListener{
		SelectionUtility su = new SelectionUtility(null);
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			final ILink link = su.getFirstSelectedAs(ILink.class);
			Location workingDir = Platform.getInstanceLocation();
			String tmp = workingDir.getURL().getFile() + "tmp";
			File tmpDir = new File(tmp);
			if(!tmpDir.exists()){
				tmpDir.mkdir();
			}
			final String newFile = tmp+"/"+link.getText().replace("/", "_")+".zip";
			try {
				
				getContainer().run(true, false, new IRunnableWithProgress() {
					
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						try {
							
							monitor.beginTask("Downloading: "+link.getText(), IProgressMonitor.UNKNOWN);
							if(!new File(newFile).exists()){
								link.download(newFile);
							}
							
							monitor.done();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						UIUtility.syncExecInUIThread(new Runnable(){
							public void run(){
						     List<String> list = ZipUtility.getZipEntries(newFile, false);
						        if(list!=null){
						        	mImportInput.setSelectedZipFile(newFile);
						        	checkboxTableViewer.setInput(newFile);
						        }
							}
								
						});
				     
				   
					}
				});

			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	private class WebProviderSelectionListener implements ISelectionChangedListener{
		SelectionUtility su = new SelectionUtility(null);
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			IImportHandlerEntry entry = su.getFirstSelectedAs(IImportHandlerEntry.class);
			IWebImportAdapter adapter = (IWebImportAdapter)entry.getService();
			try {
				comboDownloadViewer.setInput(adapter.getLinks());
				//System.out.println(adapter.getLinks());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private class WebImportAdapterProvider extends StructuredContentProviderAdapter{
		@Override
		public Object[] getElements(Object inputElement) {
			IImportManager manager = mService.getService(IImportManager.class);
			if(manager==null){
				return new Object[]{};
			}
			return ihm.getAvailableHandlers().toArray();
		}
		
	}
	
	private class ImportHandlerProvider extends StructuredContentProviderAdapter {
		@Override
		public Object[] getElements(Object inputElement) {
			IImportManager manager = mService.getService(IImportManager.class);
			if(manager==null||inputElement==null){
				return new Object[]{};
			}
			List<? extends IImportHandlerEntry> handlers = ihm.getAvailableHandlers();
			if(inputElement instanceof String){
				String input = (String) inputElement;
				if(input.lastIndexOf(".")==-1){
					return new Object[]{};
				}
				String extension = input.substring(input.lastIndexOf(".")+1);
				
				List<IImportHandlerEntry> allWithExtensions =  manager.filter(manager.createMatchingExtension(extension));
				allWithExtensions.retainAll(handlers);
				return allWithExtensions.toArray();
			}else{
				handlers = manager.getRegistredEntries();
			}
			return handlers.toArray();
		}
	}
	

	private class ImportHandlerManager{
		private List<IImportHandlerEntry> entries;
		private List<IWebImportHandlerEntry> webentries;
		private int mode = 2;
		public ImportHandlerManager(){
			IImportManager manager = mService.getService(IImportManager.class);
			List<IImportHandlerEntry> handlers = manager.getRegistredEntries();
			webentries = new ArrayList<IWebImportHandlerEntry>();
			entries = new ArrayList<IImportHandlerEntry>();
			for(IImportHandlerEntry e:handlers){
				if(e instanceof IWebImportHandlerEntry){
					webentries.add((IWebImportHandlerEntry)e);
				}else{
					entries.add(e);
				}
			}
		}
		void mode_Web(){
			mode = 1;
			System.out.println(mode);
		}
		
		void mode_Zip(){
			mode = 2;
			System.out.println(mode);
		}
		
		public List<? extends IImportHandlerEntry> getAvailableHandlers(){
			return mode==2?entries:webentries;
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
	
	//Radio Listener
	private class ZipRadioSelectionListener extends SelectionAdapter{
		public void enable(boolean b){
			text.setEnabled(b);
			mBtnBrowse.setEnabled(b);
		}
		
		public void widgetSelected(SelectionEvent e) {
			Button btn = (Button) e.getSource();
			System.out.println(btn);
			boolean selected = btn.getSelection();
			if(btn == rdZip&&selected){
				ihm.mode_Zip();
				enable(true);
				
			}else{
				text.setText("");
				enable(false);
				listViewer.setInput(null);
				checkboxTableViewer.setInput(null);
			}
			
		}
	}
	
	//Radio Listener
	private class WebRadioSelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			Button btn = (Button) e.getSource();
			System.out.println(btn);
			boolean selected = btn.getSelection();
			if(btn == rdWeb && selected){
				ihm.mode_Web();
				comboProviderViewer.setInput(1);
				enable(true);
				
			}else{
				comboProviderViewer.setInput(null);
				comboDownloadViewer.setInput(null);
				listViewer.setInput(null);
				checkboxTableViewer.setInput(null);
				
				enable(false);
			}
			
		}

		public void enable(boolean b) {
			comboDownload.setEnabled(b);
			comboProvider.setEnabled(b);
			
		}
	}
	
	/**
	 * SelectionListener: Welche TableItems wurden angegklickt
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
					if(lastChecked!=null && !lastChecked.isDisposed() && lastChecked!=titem){
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
		String pFileInput;
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
			if(newInput!=null){
				pFileInput=newInput.toString();
			}
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ZipUtility.getZipEntries(pFileInput, false).toArray();
		}
	}
}
