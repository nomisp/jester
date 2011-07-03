package ch.jester.ui.importer;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.api.web.IPingService;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.ui.importer.internal.Controller;
import ch.jester.ui.importer.internal.DownloadListener;
import ch.jester.ui.importer.internal.HandlerSelectionListener;
import ch.jester.ui.importer.internal.ImportData;
import ch.jester.ui.importer.internal.ImportHandlerContentProvider;
import ch.jester.ui.importer.internal.WebImportAdapterContentProvider;
import ch.jester.ui.importer.internal.ZipEntryContentProvider;

public class PlayerImportWizardPage extends WizardPage {
	private Text text;
	

	
	

	private ServiceUtility mService = new ServiceUtility();
	
	private final Object NULL_INPUT = null;
	
	//Radio
	private Button rdZip;
	private Button rdWeb;
	private WebRadioSelectionListener mWebRadioListener = new WebRadioSelectionListener();
	private ZipRadioSelectionListener mZipRadioListener = new ZipRadioSelectionListener();
	private ZipEntrySelectionListener zes = null;

	
	private Button mBtnBrowse;
	private ComboViewer mComboProviderViewer;
	private Combo mProviderCombo;
	
	private ComboViewer mComboDownloadViewer;
	private Combo mDownloadCombo;
	
	private CheckboxTableViewer mFileTableViewer;
	private Table mFileTable;
	
	private Controller mController;
	private ComboViewer mHandlerListViewer;

	private boolean enableWebOptions = false;
	/**
	 * @wbp.parser.constructor
	 */
	public PlayerImportWizardPage(ISelection s) {
		super("Import Player");
		setTitle("Import"); //NON-NLS-1
		setDescription("Import Players into jester"); //NON-NLS-1
		mController = Controller.createController(this);
	}
	
	
	/**
	 * Create the wizard.
	 */
	public PlayerImportWizardPage() {
		super("Import Player");
		setTitle("Import"); //NON-NLS-1
		setDescription("Import Players into jester"); //NON-NLS-1
		mController = Controller.createController(this);
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
		        Controller.getController().setZipFile(text.getText());
		        if(text.getText()!=null){
		        	mFileTableViewer.setInput(text.getText());
		        }
		        
			}


		});
		mBtnBrowse.setBounds(588, 13, 57, 23);
		mBtnBrowse.setText("Browse");
		
		text = new Text(container, SWT.BORDER);
		text.setMessage("Select a Zip file");
		text.setBounds(175, 11, 407, 23);
		
		mFileTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		mFileTable = mFileTableViewer.getTable();
		
		mFileTable.addSelectionListener(zes = new ZipEntrySelectionListener());
		
		mFileTable.setBounds(175, 128, 407, 112);
		
		mHandlerListViewer = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = mHandlerListViewer.getCombo();
		combo.setBounds(175, 265, 407, 23);
		mHandlerListViewer.getCombo().setBounds(175, 268, 407, 72);
		
	    mComboProviderViewer = new ComboViewer(container, SWT.READ_ONLY);
		mProviderCombo = mComboProviderViewer.getCombo();
		mProviderCombo.setBounds(252, 53, 330, 29);
		mComboProviderViewer.setContentProvider(new WebImportAdapterContentProvider());
		mComboProviderViewer.addSelectionChangedListener(new WebProviderSelectionListener());
		mComboProviderViewer.setInput(null);
		
		
		Label lblProvider = new Label(container, SWT.NONE);
		lblProvider.setBounds(175, 56, 73, 21);
		lblProvider.setText("Provider");
		
		mComboDownloadViewer = new ComboViewer(container, SWT.READ_ONLY);
		mDownloadCombo = mComboDownloadViewer.getCombo();
		mDownloadCombo.setBounds(252, 82, 330, 29);
		mComboDownloadViewer.setContentProvider(ArrayContentProvider.getInstance());
		mComboDownloadViewer.addSelectionChangedListener(new DownloadListener( getContainer()));
		
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
		
		mHandlerListViewer.addSelectionChangedListener(new HandlerSelectionListener());
		
		
		
		mHandlerListViewer.setContentProvider(new ImportHandlerContentProvider());
		mHandlerListViewer.setLabelProvider(new LabelProvider(){
			
			@Override
			public String getText(Object element) {
				if(element instanceof IWebImportHandlerEntry){
					IWebImportHandlerEntry entry= (IWebImportHandlerEntry) element;
					return entry.getDescription();
				}
				return element.toString();
			}
		});

		mFileTableViewer.setContentProvider(new ZipEntryContentProvider());

		setPageComplete(false);
		
		enableWebOptions = mService.getService(IPingService.class).isConnected();
		rdWeb.setEnabled(enableWebOptions);
		
		rdZip.setSelection(true);
	    mZipRadioListener.enable(true);
		
		//mHandlerListViewer.getControl().setEnabled(false);
	
		Controller.getController().setFileEntryView(mFileTableViewer);
		Controller.getController().setDownloadView(mComboDownloadViewer);
		Controller.getController().setHandlerView(mHandlerListViewer);
		Controller.getController().setProviderView(mComboProviderViewer);
		
		Controller.getController().setZipMode();
		
}
	
	
	public ImportData getData(){
		return Controller.getController().getImportData();
	}
	
	
	private class WebProviderSelectionListener implements ISelectionChangedListener{
		SelectionUtility su = new SelectionUtility(null);
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			IImportHandlerEntry entry = su.getFirstSelectedAs(IImportHandlerEntry.class);
			Controller.getController().setSelectedWebHandlerEntry(entry);
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
			//System.out.println(btn);
			boolean selected = btn.getSelection();
			if(btn == rdZip&&selected){
				Controller.getController().setZipMode();
				enable(true);
			}else{
				text.setText("");
				enable(false);
			}
			
		}
	}
	
	//Radio Listener
	private class WebRadioSelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			Button btn = (Button) e.getSource();
			boolean selected = btn.getSelection();
			if(btn == rdWeb && selected){
				Controller.getController().setWebMode();
			}
		}

	}
	
	/**
	 * SelectionListener: Welche TableItems wurden angegklickt
	 *
	 */
	private class ZipEntrySelectionListener extends SelectionAdapter  {
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
					Controller.getController().setSelectedZipEntry(titem.getText());
				}else{
					Controller.getController().setSelectedZipEntry(null);
				}
			}

		}

		
	}


}
