package ch.jester.ui.exporter;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Label;

import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.model.Player;
import ch.jester.model.Tournament;

public class PlayerXMLPage extends WizardPage {
	private Text text;
	private HashMap<String, Class<?>> mMap = new HashMap<String, Class<?>>();
	private Class<?>[] mSelectedClasses;
	private ListViewer listViewer;
	/**
	 * Create the wizard.
	 */
	public PlayerXMLPage() {
		super("wizardPage");
		setTitle("Export");
		setDescription("Export data");
		mMap.put("Players", Player.class);
		mMap.put("Tournaments", Tournament.class);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(23, 180, 421, 21);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(471, 178, 75, 25);
		btnNewButton.setText("browse");
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List list = listViewer.getList();
		list.setBounds(22, 31, 421, 121);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(24, 10, 55, 15);
		lblNewLabel.setText("Export ");
		
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
				
			}
		});
		
		listViewer.setInput(1);
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.zip", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        if(selected==null){return;}
		        text.setText(selected);

		        
			}


		});
	}
	public Class<?>[] getSelectedCalsses(){
		return mSelectedClasses;
	}
	
	public String getFileName(){
		return text.getText();
	}
}
