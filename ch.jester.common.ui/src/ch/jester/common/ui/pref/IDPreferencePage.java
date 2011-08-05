package ch.jester.common.ui.pref;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.preferences.IPreferenceManager;
import ch.jester.commonservices.api.preferences.IPreferenceManagerProvider;
import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceRegistration;
import ch.jester.commonservices.util.ServiceUtility;


/**
 * Die {@link IDPreferencePage} 
 * versucht über einen {@link IPreferenceRegistration} service einen Provider zu finden:<br>
 * Der letzte Qualifier der eigenen ID des ExtensionPoints wird gelöscht und damit versucht einen registrierte {@link IPreferenceManagerProvider} zu finden
 * Beispiel:<br>
 * - Die ID am Extensionpoint  <code>acme.pref</code><br>
 * - Suchparameter für den service ist dann <code>acme</code><br>
 *Wird kein Provider gefunden, so wird eine leere Seite angezeigt
 */
public class IDPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, IExecutableExtension{


	private ILogger logger;

	private String savekey;
	private ServiceUtility mService = new ServiceUtility();
	private IPreferenceManager prefManager;
	private boolean hasChanges;
	public IDPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		logger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	}
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		
		String subkey = config.getAttribute("id").trim().substring(0, config.getAttribute("id").trim().lastIndexOf("."));
		savekey = subkey;
		logger.info("IDPreferencePage: searchkey is: "+savekey);
		IPreferenceManagerProvider provider = mService.getService(IPreferenceRegistration.class).findProvider(savekey);
		logger.info("IDPreferencePage: Provider found: "+provider);
		if(provider!=null){
			prefManager = provider.getPreferenceManager(savekey);
		}
		
	}
	public IPreferenceManager getPreferenceManager(){
		return prefManager;
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(CommonUIActivator.getDefault().getPreferenceStore());
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
			preferenceStore.setDefault(p.getExternalKey(), p.getDefaultValue().toString());
			preferenceStore.setValue(p.getExternalKey(), p.getValue().toString());
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
				if(event.getProperty().startsWith(getPreferenceManager().getId())){
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
