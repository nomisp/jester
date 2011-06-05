package ch.jester.reportengine.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.batik.gvt.event.SelectionAdapter;
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

import ch.jester.common.ui.adapters.TableLabelProviderAdapter;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class JasperReportsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Table table;

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
		table.setBounds(10, 31, 345, 272);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnFile = tableViewerColumn_1.getColumn();
		tblclmnFile.setWidth(100);
		tblclmnFile.setText("File");
		
		Label lblTemplates = new Label(container, SWT.NONE);
		lblTemplates.setBounds(10, 10, 345, 15);
		lblTemplates.setText("Templates (double click to open in associated app)");
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(10, 305, 132, 25);
		btnNewButton.setText("Show in FileSystem");
		btnNewButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				File f = su.getService(IReportEngine.class).getFactory().getInstallationDir();
				try {
					Desktop.getDesktop().open(f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new TableLabelProviderAdapter(){
			public String getColumnText(Object element, int columnIndex) {
				if(columnIndex==0){
					return ((IReport)element).getVisibleName();
				}
				if(columnIndex==1){
					return ((IReport)element).getBundleFileName();
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
						try {
							Desktop.getDesktop().open(report.getInstalledFile());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
