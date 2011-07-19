package ch.jester.ui.importer;


import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import ch.jester.common.ui.utility.UIUtility;
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
	
	//Radio
	private Button rdZip;
	private Button rdWeb;
	private WebRadioSelectionListener mWebRadioListener = new WebRadioSelectionListener();
	private ZipRadioSelectionListener mZipRadioListener = new ZipRadioSelectionListener();
	private Button mBtnBrowse;
	private ComboViewer mComboProviderViewer;
	private Combo mProviderCombo;
	
	private ComboViewer mComboDownloadViewer;
	private Combo mDownloadCombo;
	
	private CheckboxTableViewer mFileTableViewer;
	private Table mFileTable;
	
	private ComboViewer mHandlerListViewer;

	private boolean enableWebOptions = false;
	
	private Controller mController;
	/**
	 * @wbp.parser.constructor
	 */
	public PlayerImportWizardPage(ISelection s, Controller controller) {
		super(Messages.PlayerImportWizardPage_lbl_import_player);
		setTitle(Messages.PlayerImportWizardPage_lbl_import); //NON-NLS-1
		setDescription(Messages.PlayerImportWizardPage_lbl_import_into_jester); //NON-NLS-1
		mController=controller;
	}
	
	
	/**
	 * Create the wizard.
	 */
	public PlayerImportWizardPage(Controller controller) {
		super(Messages.PlayerImportWizardPage_lbl_import_player); 
		setTitle(Messages.PlayerImportWizardPage_lbl_import); 
		setDescription(Messages.PlayerImportWizardPage_lbl_import_into_jester);
		mController=controller;
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
		        fd.setText(Messages.PlayerImportWizardPage_lbl_btn_open);
		        fd.setFilterPath("."); //$NON-NLS-1$
		        String[] filterExt = { "*.zip", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        if(selected==null){return;}
		        text.setText(selected);
		        mController.setZipFile(text.getText());
		        if(text.getText()!=null){
		        	mFileTableViewer.setInput(text.getText());
		        }
		        
			}


		});
		mBtnBrowse.setBounds(588, 13, 57, 23);
		mBtnBrowse.setText(Messages.PlayerImportWizardPage_lbl_btn_browse);
		
		text = new Text(container, SWT.BORDER);
		text.setMessage(Messages.PlayerImportWizardPage_lbl_select_zipfile);
		text.setBounds(175, 11, 407, 23);
		
		mFileTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		mFileTable = mFileTableViewer.getTable();
		
		mFileTable.addSelectionListener(new ZipEntrySelectionListener());
		
		mFileTable.setBounds(175, 128, 407, 112);
		
		mHandlerListViewer = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = mHandlerListViewer.getCombo();
		combo.setBounds(175, 265, 407, 23);
		mHandlerListViewer.getCombo().setBounds(175, 268, 407, 72);
		
	    mComboProviderViewer = new ComboViewer(container, SWT.READ_ONLY);
		mProviderCombo = mComboProviderViewer.getCombo();
		mProviderCombo.setBounds(252, 53, 330, 29);
		mComboProviderViewer.setContentProvider(new WebImportAdapterContentProvider(mController));
		mComboProviderViewer.addSelectionChangedListener(new WebProviderSelectionListener());
		mComboProviderViewer.setInput(null);
		
		
		Label lblProvider = new Label(container, SWT.NONE);
		lblProvider.setBounds(175, 56, 73, 21);
		lblProvider.setText(Messages.PlayerImportWizardPage_lbl_provider);
		
		mComboDownloadViewer = new ComboViewer(container, SWT.READ_ONLY);
		mDownloadCombo = mComboDownloadViewer.getCombo();
		mDownloadCombo.setBounds(252, 82, 330, 29);
		mComboDownloadViewer.setContentProvider(ArrayContentProvider.getInstance());
		mComboDownloadViewer.addSelectionChangedListener(new DownloadListener(mController, getContainer()));
		
		Label lblSource = new Label(container, SWT.NONE);
		lblSource.setBounds(10, 14, 65, 29);
		lblSource.setText(Messages.PlayerImportWizardPage_lbl_source);
		
		//Radios
		rdZip = new Button(container, SWT.RADIO);
		rdZip.setBounds(92, 10, 65, 29);
		rdZip.setText(Messages.PlayerImportWizardPage_lbl_zip);
		rdZip.addSelectionListener(mZipRadioListener);
		
	    rdWeb = new Button(container, SWT.RADIO);
		rdWeb.setText(Messages.PlayerImportWizardPage_lbl_web);
		rdWeb.setBounds(92, 52, 65, 29);
		rdWeb.addSelectionListener(mWebRadioListener);
		

		//

		Label lblDownload = new Label(container, SWT.NONE);
		lblDownload.setBounds(175, 85, 73, 21);
		lblDownload.setText(Messages.PlayerImportWizardPage_lbl_dl);
		
		Label lblImportHandler = new Label(container, SWT.NONE);
		lblImportHandler.setBounds(10, 128, 154, 21);
		lblImportHandler.setText(Messages.PlayerImportWizardPage_lbl_content);
		
		Label lblHandler = new Label(container, SWT.NONE);
		lblHandler.setText(Messages.PlayerImportWizardPage_lbl_handler);
		lblHandler.setBounds(10, 268, 154, 21);
		
		mHandlerListViewer.addSelectionChangedListener(new HandlerSelectionListener(mController));
		
		
		
		mHandlerListViewer.setContentProvider(new ImportHandlerContentProvider(mController));
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

		mFileTableViewer.setContentProvider(new ZipEntryContentProvider(mController));

		setPageComplete(false);
		enableWebOptions = mService.getService(IPingService.class).isConnected();
		rdWeb.setEnabled(enableWebOptions);
		if(!enableWebOptions){
			startConnectionChecker();
		}
		
		rdZip.setSelection(true);
	    mZipRadioListener.enable(true);
		
		//mHandlerListViewer.getControl().setEnabled(false);
	
	    mController.setFileEntryView(mFileTableViewer);
		mController.setDownloadView(mComboDownloadViewer);
		mController.setHandlerView(mHandlerListViewer);
		mController.setProviderView(mComboProviderViewer);
		
		mController.setZipMode();
		
}
	
	private void startConnectionChecker(){
		//connection poller
		//unschön... aber wir wollend dafür keinen listener implementieren.
		Job checker = new Job("concheck"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				boolean connected = mService.getService(IPingService.class).isConnected();
				if(!connected){
					schedule(500);
					return Status.OK_STATUS;
				}
				UIUtility.syncExecInUIThread(new Runnable() {
					@Override
					public void run() {
					 if(rdWeb.isDisposed()){return;}
						rdWeb.setEnabled(true);
						
					}
				});
				return Status.OK_STATUS;
			}
		};
		checker.setUser(false);
		checker.setSystem(true);
		checker.schedule();
	}
	
	public ImportData getData(){
		return mController.getImportData();
	}
	
	
	private class WebProviderSelectionListener implements ISelectionChangedListener{
		SelectionUtility su = new SelectionUtility(null);
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			su.setSelection(event.getSelection());
			IImportHandlerEntry entry = su.getFirstSelectedAs(IImportHandlerEntry.class);
			mController.setSelectedWebHandlerEntry(entry);
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
				mController.setZipMode();
				enable(true);
			}else{
				text.setText(""); //$NON-NLS-1$
				enable(false);
			}
			
		}
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return mController.getParseController().canDoMatching();
	}
	
	//Radio Listener
	private class WebRadioSelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			Button btn = (Button) e.getSource();
			boolean selected = btn.getSelection();
			if(btn == rdWeb && selected){
				mController.setWebMode();
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
					mController.setSelectedZipEntry(titem.getText());
				}else{
					mController.setSelectedZipEntry(null);
				}
			}

		}

		
	}


}
