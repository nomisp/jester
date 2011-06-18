package ch.jester.orm.prefs;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.orm.IORMConfiguration;
import ch.jester.orm.ORMPlugin;
import ch.jester.orm.ORMStoreHandler;
import ch.jester.orm.internal.ORMDBUtil;

public class DataBaseSubPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, IExecutableExtension {
	private List<EditorSetup> editors = new ArrayList<EditorSetup>();
	private ILogger logger;
	private IConfigurationElement mContributorElement;
	private IORMConfiguration mConfig;
	private IContributor mContributor;
	public DataBaseSubPreferencePage() {
		super(GRID);
		logger = ORMPlugin.getDefault().getActivationContext().getLogger();
	}
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		mContributorElement=config;
		mContributor = mContributorElement.getContributor();
	
		IExtension extension =  getContributorConfig(mContributor);
		IConfigurationElement el = extension.getConfigurationElements()[0];
		mConfig = (IORMConfiguration) el.createExecutableExtension("ORMConfiguration");
		mConfig.setConfigElement(el);
		
		
	}
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(ORMPlugin.getDefault().getPreferenceStore());
		mConfig.setORMStoreHandler(new ORMStoreHandler(getPreferenceStore(), mContributor));
	}
	
	private IExtension getContributorConfig(IContributor pContributor){
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(mContributor);
		for(IExtension ex:extensions){
			if(ex.getExtensionPointUniqueIdentifier().equals("ch.jester.orm."+ORMPlugin.EP_CONFIGURATION)){
				return ex;
			}
		}
		return null;
	}

	@Override
	protected void createFieldEditors() {
		
		IPreferenceStore preferenceStore = getPreferenceStore();
	
		Iterator<String> keyit = mConfig.getConfiguration().keySet().iterator();
		
		while(keyit.hasNext()){
			EditorSetup esetup = new EditorSetup();
			esetup.key = keyit.next();
			esetup.value = mConfig.getConfiguration().get(esetup.key);
			esetup.savekey = mContributor.getName()+"."+esetup.key;
			preferenceStore.setDefault(esetup.savekey, esetup.value);
			String preferenceValue = preferenceStore.getString(esetup.savekey);
			final Object objValue = convertString(preferenceValue);
			if(objValue instanceof Boolean){
				esetup.editorclass = BooleanFieldEditor.class;
			}else if(objValue instanceof Integer){
				esetup.editorclass = IntegerFieldEditor.class;
			}else{
				esetup.editorclass = StringFieldEditor.class;

			}
			editors.add(esetup);
			
		}
		Collections.sort(editors, new Comparator<EditorSetup>() {
			@Override
			public int compare(EditorSetup arg0, EditorSetup arg1) {
				if(arg0.editorclass == BooleanFieldEditor.class && arg1.editorclass == BooleanFieldEditor.class){
					return 0;
				}
				if(arg0.editorclass == BooleanFieldEditor.class ){
					return 1;
				}
				if(arg1.editorclass == BooleanFieldEditor.class ){
					return -1;
				}
				return 0;
			}
			
			
		});
		for(EditorSetup e:editors){
			preferenceStore.setDefault(e.savekey, e.value);
			try {
				FieldEditor editor = (FieldEditor) e.editorclass.getConstructor(String.class, String.class, Composite.class).newInstance(e.savekey, e.key, getFieldEditorParent());
				editor.setPreferenceStore(getPreferenceStore());
				editor.load();
				addField(editor);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}
		}
		
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(!event.getProperty().startsWith(mContributor.getName())){
					return;
				}
				String configKey = getConfigKey(event.getProperty());
				logger.debug("Property changed: "+event.getProperty()+" old: "+event.getOldValue()+" new: "+event.getNewValue() );
				logger.debug("- ConfigKey is "+configKey);
				mConfig.getConfiguration().put(configKey, event.getNewValue().toString());
				ORMDBUtil.openRestartConfirmation();
			}

		
		});

	}
	private Object convertString(String value) {
		if(value.toLowerCase().equals("false")){
			return false;
		}
		if(value.toLowerCase().equals("true")){
			return true;
		}
		try{
			Integer b = Integer.parseInt(value);
			return b;
		}catch(Exception e){
		}
		return value;
	}
	private String getConfigKey(String saveKey) {
		for(EditorSetup e:editors){
			if(e.savekey.equals(saveKey)){
				return e.key;
			}
		}
		return null;
	}
	private class EditorSetup{
		String key;
		String value;
		String savekey;
		Class<?> editorclass;
		
		
	}
 


}
