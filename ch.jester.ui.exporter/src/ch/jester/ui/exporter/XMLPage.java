package ch.jester.ui.exporter;

import java.util.ArrayList;
import java.util.HashMap;

import messages.Messages;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.model.Tournament;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * Die WizardPage für den XML Exporter
 *
 */
public class XMLPage extends WizardPage {
	private Text text;
	private HashMap<String, Class<?>> mMap = new HashMap<String, Class<?>>();
	private Class<?>[] mSelectedClasses = new Class[]{};
	private ListViewer listViewer;
	private XMLExporter exporter;
	/**
	 * Create the wizard.
	 * @param playerXMLExporter 
	 */
	public XMLPage(XMLExporter playerXMLExporter) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.PlayerXMLPage_lbl_export);
		setDescription(Messages.PlayerXMLPage_lbl_export_data);
		//mMap.put("Players", Player.class);
		mMap.put(Messages.PlayerXMLPage_lbl_tournament, Tournament.class);
		exporter = playerXMLExporter;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(Messages.PlayerXMLPage_lbl_btn_export);
		new Label(container, SWT.NONE);
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		listViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		listViewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				return mMap.keySet().toArray();
			}
		});
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			SelectionUtility su = new SelectionUtility(null);
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				su.setSelection(event.getSelection());
				Object[] keys =  su.getAsStructuredSelection().toArray();
				java.util.List<Class<?>> values = new ArrayList<Class<?>>();
				for(Object key:keys){
					values.add(mMap.get(key.toString()));
				}
				mSelectedClasses=values.toArray(new Class[values.size()]);
				exporter.getContainer().updateButtons();
				
			}
		});
		
		listViewer.setInput(1);
		new Label(container, SWT.NONE);
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setText(Messages.PlayerXMLPage_lbl_browse);
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
		        fd.setText(Messages.PlayerXMLPage_lbl_btn_save);
		        fd.setFilterPath(Messages.PlayerXMLPage_filterpath);
		        String[] filterExt = { Messages.PlayerXMLPage_extension1, Messages.PlayerXMLPage_extension2 };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        if(selected==null){return;}
		        text.setText(selected);
		        exporter.getContainer().updateButtons();
		        
			}


		});
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				 exporter.getContainer().updateButtons();
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				 exporter.getContainer().updateButtons();
				
			}
		});
	}
	public boolean canFinish(){
		boolean fileFilled = false; 
		if(text!=null && !text.isDisposed()){
			fileFilled = !getFileName().isEmpty();
		}

		if(fileFilled && getSelectedCalsses().length!=0){
			return true;
		}
		return false;
		
	}
	
	public Class<?>[] getSelectedCalsses(){
		return mSelectedClasses;
	}
	
	public String getFileName(){
		return text.getText();
	}
}
