package ch.jester.ui.pref;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.importer.IWebImportAdapter;
import ch.jester.commonservices.api.importer.IWebImportHandlerEntry;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.ui.importer.internal.Activator;


public class GenericImporterPref extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, IExecutableExtension{


	private ILogger logger;
	private IContributor mContributor;
	private IConfigurationElement mContributorElement;
	private String savekey;
	private ServiceUtility mService = new ServiceUtility();
	private IImportHandlerEntry entry;
	private IPreferenceManager prefManager;
	public GenericImporterPref() {
		super(GRID);
		logger = Activator.getInstance().getActivationContext().getLogger();
	}
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		mContributorElement=config;
		mContributor = mContributorElement.getContributor();
		
		String subkey = config.getAttribute("id").trim().substring(0, config.getAttribute("id").trim().lastIndexOf("."));
		savekey = subkey;
		System.out.println(savekey);
		IImportManager manager = mService.getService(IImportManager.class);
		IWebImportHandlerEntry e = null;
		for(IImportHandlerEntry entry :manager.getRegistredEntries()){
			String property = entry.getProperty("ImportHandlerId");
			if(savekey.equals(entry.getProperty("ImportHandlerId"))){
				if(!(entry instanceof IWebImportHandlerEntry)){continue;}
				e=(IWebImportHandlerEntry) entry;
				break;
			}
		}
		entry=e;
		IWebImportAdapter adapter = (IWebImportAdapter) entry.getService();
		prefManager = adapter.getPreferenceManager();
		prefManager.setPrefixKey(savekey);
		//System.out.println(entry);
		
	}
	public IPreferenceManager getPreferenceManager(){
		return prefManager;
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getInstance().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		IPreferenceStore preferenceStore = getPreferenceStore();

		
		Set<IPreferenceProperty> props = getPreferenceManager().getProperties();
		for(IPreferenceProperty p:props){
			String key = p.getExternalKey();
			String value = p.getValue().toString();
			//System.out.println(key+" = "+value);
			preferenceStore.setDefault(p.getExternalKey(), p.getDefaultValue().toString());
			FieldEditor editor = null;
			if(p.getType() == Boolean.class){
				editor = new BooleanFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}else if(p.getType() == Integer.class){
				editor = new IntegerFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}else{
				editor = new StringFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}
			String prefName = editor.getPreferenceName();
			
			addField(editor);
		}
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty().startsWith(getPreferenceManager().getPrefixKey())){
					IPreferenceProperty prefProp = getPreferenceManager().getPropertyByExternalKey(event.getProperty());
					prefProp.setValue(event.getNewValue());
				}
				
			}
		});

	}



}
