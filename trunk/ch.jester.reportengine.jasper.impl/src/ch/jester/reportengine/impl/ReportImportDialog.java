package ch.jester.reportengine.impl;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.jester.model.factories.ModelFactory;

public class ReportImportDialog extends Dialog {
	private Text txtFileName;
	private Text txtReportName;
	private String fName;
	private String rName;
	private ComboViewer comboViewer;
	private Class<?> selectedClass;
	private String mPath;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ReportImportDialog(Shell parentShell) {
		super(parentShell);
	
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Report Importer");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 4;
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Report File");
		
		txtFileName = new Text(container, SWT.BORDER);
		GridData gd_txtFileName = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txtFileName.widthHint = 353;
		txtFileName.setLayoutData(gd_txtFileName);
		
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.setText("browse");
		btnBrowse.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				
		        fd.setText("Open");
		        if(mPath!=null){
		        	fd.setFilterPath(mPath);
		        }else{
		        	fd.setFilterPath(".");
		        }
		        String[] filterExt = { "*.jrxml", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String mFile = fd.open();
		        if(mFile!=null){
		        	txtFileName.setText(mFile);
		        }
	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		

		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Report Name");
		
		txtReportName = new Text(container, SWT.BORDER);
		txtReportName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txtReportName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text txt = (Text)e.widget;
				rName=txt.getText();
				checkOk();
			}
		});
		new Label(container, SWT.NONE);
		
		Label lblInputBean = new Label(container, SWT.NONE);
		lblInputBean.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInputBean.setText("Input Bean");
		
		comboViewer = new ComboViewer(container, SWT.NONE | SWT.READ_ONLY);
		comboViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				Class<?> c = (Class<?>) element;
				return c.getCanonicalName();
			}
		});
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				selectedClass = (Class<?>) ((IStructuredSelection)comboViewer.getSelection()).getFirstElement();
				
			}
		});
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		txtFileName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text txt = (Text)e.widget;
				fName=txt.getText();
				checkOk();
			}
		});
		
		comboViewer.setInput(ModelFactory.getInstance().getAllExportableClasses());
		return container;
	}
	private void checkOk(){
		if(rName!=null&&fName!=null&rName.length()>0&&fName.length()>0){
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}else{
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}
	
	public String getSelectedFile(){
		return fName;
	}
	public String getReportName(){
		return rName;
		
	}
	public Class<?> getInputBeanClass(){
		return selectedClass;
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
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public void setInputPath(String string) {
		mPath = string;
		
	}

}
