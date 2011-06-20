package ch.jester.common.ui.pref;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;


public class IDPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, IExecutableExtension{


	private ILogger logger;
	private IContributor mContributor;
	private IConfigurationElement mContributorElement;
	private String savekey;
	private ServiceUtility mService = new ServiceUtility();
	private IPreferenceManager prefManager;
	private boolean hasChanges;
	public IDPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		logger = Activator.getDefault().getActivationContext().getLogger();
	}
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		mContributorElement=config;
		mContributor = mContributorElement.getContributor();
		
		String subkey = config.getAttribute("id").trim().substring(0, config.getAttribute("id").trim().lastIndexOf("."));
		savekey = subkey;
		IPreferenceManagerProvider provider = mService.getService(IPreferenceRegistration.class).findProvider(savekey);
		System.out.println("Provider: "+provider);
		prefManager = provider.getPreferenceManager(savekey);
		
	}
	public IPreferenceManager getPreferenceManager(){
		return prefManager;
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		if(getPreferenceManager()!=null&&getPreferenceManager().getDescription()!=null){
			setDescription(getPreferenceManager().getDescription());
		}
	}
	protected void createInvalidUI() {
		setDescription("Service not found");
		setTitle(getTitle()+": "+getDescription());
		
	}
	private boolean isSetupValid() {
		return prefManager!=null;
	}

	@Override
	protected void createFieldEditors() {
		if(!isSetupValid()){
			createInvalidUI();
			return;
		}else{
			createValidUI();
		}
		
		

	}
	protected void createValidUI() {
		hasChanges = false;
		IPreferenceStore preferenceStore = getPreferenceStore();
	
		
		Set<IPreferenceProperty> props = getPreferenceManager().getProperties();
		for(IPreferenceProperty p:props){
			String key = p.getExternalKey();
			String value = p.getValue().toString();
			//System.out.println(key+" = "+value);
			preferenceStore.setDefault(p.getExternalKey(), p.getDefaultValue().toString());
			FieldEditor editor = null;
			if(p.getSelectableValues()!=null){
				editor = new ComboFieldEditor(key, p.getLabel(), p.getSelectableValues(), getFieldEditorParent()); 
			}else if(p.getType() == Boolean.class){
				editor = new BooleanFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}else if(p.getType() == Integer.class){
				editor = new IntegerFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}else{
				editor = new StringFieldEditor(key, p.getLabel(), getFieldEditorParent());
			}
			editor.setEnabled(p.getEnabled(), getFieldEditorParent());
			addField(editor);
			
		}
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty().startsWith(getPreferenceManager().getPrefixKey())){
					IPreferenceProperty prefProp = getPreferenceManager().getPropertyByExternalKey(event.getProperty());
					prefProp.setValue(event.getNewValue());
					hasChanges = true;
				}
				
			}
		});
		
		
	}
	@Override
	public boolean performOk() {
		boolean result =  super.performOk();
		if(hasChanges&&getPreferenceManager().getNeedRestartAfterChange()){
			UIUtility.openRestartConfirmation();
		}
		return result;
	}


}
