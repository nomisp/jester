package ch.jester.reportengine.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.common.ui.adapters.TableLabelProviderAdapter;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class JasperReportsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Table table;
	private IReport mSelected;
	private TableViewer tableViewer;
	private final ServiceUtility su = new ServiceUtility();
	/**
	 * Create the preference page.
	 */
	public JasperReportsPreferencePage() {
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(5, false));
		
		Label lblTemplates = new Label(container, SWT.NONE);
		lblTemplates.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1));
		lblTemplates.setText("Templates (double click to open in associated app)");
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_table.verticalIndent = 1;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn_2.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Source Name");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnFile = tableViewerColumn_1.getColumn();
		tblclmnFile.setWidth(200);
		tblclmnFile.setText("File");
		
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				su.setSelection(event.getSelection());
				mSelected = su.getFirstSelectedAs(IReport.class);
				
			}
		});
		tableViewer.setLabelProvider(new TableLabelProviderAdapter(){
			public String getColumnText(Object element, int columnIndex) {
				if(columnIndex==0){
					return ((IReport)element).getVisibleName();
				}
				if(columnIndex==1){
					return ((IReport)element).getInstalledFile().getName();
				}
				if(columnIndex==2){
					return ((IReport)element).getInstalledFile().getAbsolutePath();
				}
				return null;
			}
			
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void doubleClick(final DoubleClickEvent event) {
				SafeRunnable.run(new SafeMessageBoxRunner() {
					
					@Override
					public void run() throws Exception {
						su.setSelection(event.getSelection());
						IReport report = su.getFirstSelectedAs(IReport.class);

						try{
							Desktop.getDesktop().open(report.getInstalledFile());
						}
						catch(Exception e){
							String message = "Could not open: "+report.getInstalledFile().getName()+
							"\n\nPlease install IReport: http://sourceforge.net/projects/ireport/";
							MessageDialog.openInformation(UIUtility.getActiveWorkbenchWindow().getShell(), "Warning", message);
							
						}
		
						
					}
				});
				
				
			}
		});

		
		Button btnShowInFs = new Button(container, SWT.NONE);
		btnShowInFs.setText("Show in Filesystem");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Button btnImport = new Button(container, SWT.NONE);
		btnImport.setText("Import Report");
		btnImport.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportImportDialog dialog = new ReportImportDialog(Display.getDefault().getActiveShell());
				dialog.create();
				dialog.setBlockOnOpen(true);
				int i = dialog.open();
				if(i==Dialog.OK){
					List<IReport> allReports = importReport(dialog.getReportName(), dialog.getSelectedFile());
					tableViewer.setInput(allReports);
				}
		
			}
			


			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		btnShowInFs.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filePath = mSelected.getInstalledFile().getParent();
				try {
					Desktop.getDesktop().open(new File(filePath));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		List<IReport> reports = su.getService(IReportEngine.class).getFactory().getReports();
		tableViewer.setInput(reports);
		return container;
	}

	private List<IReport> importReport(String reportName, String selectedFile) {
		IReportEngine engine = su.getService(IReportEngine.class);
		engine.getFactory().createReport(null, UUID.randomUUID().toString(), reportName, null, selectedFile);
		return engine.getFactory().getReports();
	}
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}
}
