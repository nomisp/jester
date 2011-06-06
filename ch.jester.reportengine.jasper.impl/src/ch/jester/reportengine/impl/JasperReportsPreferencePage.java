package ch.jester.reportengine.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.batik.gvt.event.SelectionAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

import ch.jester.common.ui.adapters.TableLabelProviderAdapter;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.jfree.ui.UIUtilities;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class JasperReportsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Table table;
	private IReport mSelected;

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
		final ServiceUtility su = new ServiceUtility();
		Composite container = new Composite(parent, SWT.NULL);
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setBounds(10, 31, 432, 272);
		
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
		tblclmnFile.setWidth(500);
		tblclmnFile.setText("File");
		
		Label lblTemplates = new Label(container, SWT.NONE);
		lblTemplates.setBounds(10, 10, 345, 15);
		lblTemplates.setText("Templates (double click to open in associated app)");
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(10, 305, 132, 25);
		btnNewButton.setText("Show in FileSystem");
		btnNewButton.addSelectionListener(new SelectionListener() {
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
		
		
		List<IReport> reports = su.getService(IReportEngine.class).getFactory().getReports();
		tableViewer.setInput(reports);
		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}
}
