package ch.jester.common.settings;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.model.SettingItem;
import ch.jester.model.SettingItemValue;
import ch.jester.model.factories.ModelFactory;
import ch.jester.model.internal.Activator;

/**
 * HelperKlasse für das Persistieren und zurückholen von SettingObjekten
 * @param <T>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class SettingHelper<T extends ISettingObject> {

	private ILogger mLogger;
	private ModelFactory modelFactory = ModelFactory.getInstance();
	private SettingItem settingItem;
	private Set<String> fieldsToExclude;
	private Set<String> fieldsToInclude;
	
	public SettingHelper() {
		mLogger = Activator.getInstance().getActivationContext().getLogger();
		fieldsToExclude = new HashSet<String>();
        fieldsToInclude = new HashSet<String>();
        addFieldToExclude("CLASS");
	}
	
	/*
	 * Analysieren und speichern eines SettingObjects
	 */
	
	public SettingItem analyzeSettingObjectToStore(T settingObject, SettingItem settingItem) {
        // TODO Peter: Tournament muss bereits gesetzt sein. Evtl. Abfangen/Exception 
		Map classDescription = null;
        try {
            classDescription = PropertyUtils.describe(settingObject);
            settingItem.setRootClassName(((Class) classDescription.get("class")).getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        
        if (fieldsToInclude != null && fieldsToInclude.size() > 0 && !fieldsToInclude.isEmpty()) {
            for (Object propName : classDescription.keySet()) {
                if (fieldsToInclude.contains(((String) propName).toUpperCase()) &&
                        classDescription.get(propName) != null && !classDescription.get(propName).equals("")) {
                	settingItem = generateSettingItemValue(classDescription, settingObject, propName, settingItem);
                }
            }
        } else {
            for (Object propName : classDescription.keySet()) {
                if (!fieldsToExclude.contains(((String) propName).toUpperCase()) &&
                        classDescription.get(propName) != null && !classDescription.get(propName).equals("")) {
                	settingItem = generateSettingItemValue(classDescription, settingObject, propName, settingItem);
                }
            }
        }
		return settingItem;
	}

    private SettingItem generateSettingItemValue(Map classDescription, T settingObject, Object propName, SettingItem settingItem) {
        if (classDescription.get(propName) instanceof Collection) {
            int seq = 1;
            for (Object o : (Collection) classDescription.get(propName)) {
                if (o != null) {
                    SettingItemValue settingItemValue = modelFactory.createSettingItemValue();
                    settingItemValue.setFieldNameInRootClass((String) propName);
                    settingItemValue.setFieldClassNameInRootClass(getClassName(classDescription.get(propName).getClass()));
                    settingItemValue = analyzeSettingItemValue(settingObject, propName, settingItemValue, o, true);
                    settingItemValue.setSequenceNo(seq);
                    settingItemValue.setSettingItem(settingItem);
                    seq = seq + 1;
                }
            }
        } else {
            SettingItemValue settingItemValue = modelFactory.createSettingItemValue();
            settingItemValue.setFieldClassNameInRootClass(getClassName(classDescription.get(propName).getClass()));
            settingItemValue.setFieldNameInRootClass((String) propName);
            settingItemValue = analyzeSettingItemValue(settingObject, propName, settingItemValue, classDescription.get(propName), false);
            settingItemValue.setSequenceNo(0);
            settingItemValue.setSettingItem(settingItem);
        }
        return settingItem;
    }
    
    private SettingItemValue analyzeSettingItemValue(T settingObject, Object propName, SettingItemValue itemValue, Object instance, Boolean isCollectionElement) {
        try {
            if (instance == null) return itemValue;
            itemValue.setValueClassName(getClassName(instance.getClass()));
            if (itemValue.getValueClassName().equals("java.lang.String")) {
                itemValue.setValueAsString((String) instance);
            } else if (itemValue.getValueClassName().equals("java.util.Date") || itemValue.getValueClassName().equals("java.sql.Date")) {
                itemValue.setValueAsString(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format((Date) instance));
            } else {
                if (isCollectionElement) {
                    itemValue.setValueAsString(instance.toString());
                } else {
                    itemValue.setValueAsString(BeanUtils.getSimpleProperty(settingObject, (String) propName));
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return itemValue;
    }
    
	
	/*
	 * Wiederherstellen eines SettingObjects
	 */
	
	public T restoreSettingObject(T settingObject, SettingItem settingItem) {
		this.settingItem = settingItem;
		if (checkForSettingObject(settingObject)) {
			settingObject = restoreSettingObject(settingObject);
			settingItem = null;
		}
		return settingObject;
	}
	
	private T restoreSettingObject(T settingObject) {
		if (settingItem.getRootClassName().isEmpty()) return settingObject;
		List<SettingItemValue> settingItemValues = getAsMultiOrderedList(settingItem.getSettingItemValues(), "fieldNameInRootClass,sequenceNo");
//		Set<SettingItemValue> settingItemValues = settingItem.getSettingItemValues();
		Map<String, Collection> collections = new HashMap<String, Collection>();
        Map classDescription = null;
        try {
            classDescription = PropertyUtils.describe(settingObject);
        } catch (IllegalAccessException e) {
        	mLogger.error(e);
        } catch (InvocationTargetException e) {
        	mLogger.error(e);
        } catch (NoSuchMethodException e) {
        	mLogger.error(e);
        }
        for (SettingItemValue settingItemValue : settingItemValues) {
		//	Object entityObject = null;
			if (classDescription.containsKey(settingItemValue.getFieldNameInRootClass())) {
                try {
                    if (PropertyUtils.getProperty(settingObject, settingItemValue.getFieldNameInRootClass()) instanceof Collection) {
                        Collection collection = collections.get(settingItemValue.getFieldNameInRootClass());
                        if (collection == null) {
                            Class c = Class.forName(settingItemValue.getFieldClassNameInRootClass());
                            try {
                                collection = (Collection) c.newInstance();
                            } catch (InstantiationException e) {
                            	mLogger.error(e);
                            }
                        }
                        if ("java.lang.String".equals(settingItemValue.getValueClassName())) {
                            collection.add(settingItemValue.getValueAsString());
                        } else if ("java.lang.Long".equals(settingItemValue.getValueClassName())) {
                            collection.add(Long.parseLong(settingItemValue.getValueAsString()));
                        } else if ("java.lang.Integer".equals(settingItemValue.getValueClassName())) {
                            collection.add(Integer.parseInt(settingItemValue.getValueAsString()));
                        } else if ("java.lang.Float".equals(settingItemValue.getValueClassName())) {
                            collection.add(Float.parseFloat(settingItemValue.getValueAsString()));
                        } else if ("java.lang.Boolean".equals(settingItemValue.getValueClassName())) {
                            collection.add(Boolean.parseBoolean(settingItemValue.getValueAsString()));
                        } else if ("java.lang.Double".equals(settingItemValue.getValueClassName())) {
                            collection.add(Double.parseDouble(settingItemValue.getValueAsString()));
                        } else if ("java.util.Date".equals(settingItemValue.getValueClassName()) || "java.sql.Date".equals(settingItemValue.getValueClassName())) {
                            try {
                                collection.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(settingItemValue.getValueClassName()));
                            } catch (ParseException e) {
                            	mLogger.error(e);
                            }
                        }
                        collections.put(settingItemValue.getFieldNameInRootClass(), collection);
                    } else {
                    	settingObject = setProperty(settingObject, settingItemValue.getFieldNameInRootClass(), settingItemValue.getValueAsString(), settingItemValue.getValueClassName());
                    }
                } catch (IllegalAccessException e) {
                	mLogger.error(e);
                } catch (InvocationTargetException e) {
                	mLogger.error(e);
                } catch (NoSuchMethodException e) {
                	mLogger.error(e);
                } catch (ClassNotFoundException e) {
                	mLogger.error(e);
                }
            }
        }
		return settingObject;
	}
	
	private T setProperty(T settingObject, String propName, Object valueObject, String propClassName) {
        try {
            if (propClassName == null || "java.lang.String".equals(propClassName)) {
                PropertyUtils.setProperty(settingObject, propName, valueObject);
            } else {
                if ("java.lang.Long".equals(propClassName)) {
                    PropertyUtils.setProperty(settingObject, propName, Long.parseLong((String) valueObject));
                } else if ("java.lang.Integer".equals(propClassName)) {
                    PropertyUtils.setProperty(settingObject, propName, Integer.parseInt((String) valueObject));
                } else if ("java.lang.Float".equals(propClassName)) {
                    PropertyUtils.setProperty(settingObject, propName, Float.parseFloat((String) valueObject));
                } else if ("java.lang.Boolean".equals(propClassName)) {
                    PropertyUtils.setProperty(settingObject, propName, Boolean.parseBoolean((String) valueObject));
                } else if ("java.lang.Double".equals(propClassName)) {
                    PropertyUtils.setProperty(settingObject, propName, Double.parseDouble((String) valueObject));
                } else if ("java.util.Date".equals(propClassName) || "java.sql.Date".equals(propClassName)) {
                    try {
                        PropertyUtils.setProperty(settingObject, propName, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse((String) valueObject));
                    } catch (ParseException e) {
                    	mLogger.error(e);
                    }
                } else {
                    try {
                        Class c = Class.forName(propClassName);
                        if (c.isEnum()) {
                            PropertyUtils.setProperty(settingObject, propName, Enum.valueOf(c, (String) valueObject));
                        } else {
                            PropertyUtils.setProperty(settingObject, propName, valueObject);
                        }
                    } catch (ClassNotFoundException e) {
                    	mLogger.error(e);
                    }
                }
            }
        } catch (IllegalAccessException e) {
        	mLogger.error(e);
        } catch (InvocationTargetException e) {
        	mLogger.error(e);
        } catch (NoSuchMethodException e) {
        	mLogger.error(e);
        }
        return settingObject;
    }
	
	private boolean checkForSettingObject(T settingObject) {
		if (settingItem == null) return false;
		if (settingItem.getRootClassName().equals(getClassName(settingObject.getClass()))){
			return true;
		} else {
			mLogger.debug("Wrong SettingObject-Type");
			return false;
		}
	}
	
	/**
	 * Gibt den korrekten Klassennamen zurück auch wenn es sich um
	 * Hibernate javassist-Klassen handelt. 
	 * @param clazz
	 * @return
	 */
    private String getClassName(Class clazz) {
        String className = "";
        if (clazz.getName().indexOf("_$$_javassist") != -1) {
            className = clazz.getName().substring(0, clazz.getName().indexOf("_$$_javassist"));
        } else {
            className = clazz.getName();
        }
        return className;
    }
    
	/**
	 * Liefert eine Liste zurück welche nach mehreren Kriterien sortiert ist
	 * 
	 * @param collectionToConvertAndOrder
	 * @param orderPropertyAndDirection
	 * @return
	 */
	private List getAsMultiOrderedList(Collection collectionToConvertAndOrder, String orderPropertyAndDirection) {
		if (collectionToConvertAndOrder != null) {
			String[] props = orderPropertyAndDirection.split(",");
			ComparatorChain chain = new ComparatorChain();
			List toOrder = new ArrayList(collectionToConvertAndOrder);

			for (int i = 0; i < props.length; i++) {
				String prop = props[i];
				String[] instruction = prop.split(" ");
				//String propertyName = "";
				//String direction = "";
				if (instruction.length == 1) {
					chain.addComparator(new BeanComparator(instruction[0]
							.trim(), new NullComparator()));
				} else {
					if ("asc".equalsIgnoreCase(instruction[1].trim())) {
						chain.addComparator(new BeanComparator(instruction[0]
								.trim(), new NullComparator()));
					} else {
						chain.addComparator(new ReverseComparator(
								new BeanComparator(instruction[0].trim(),
										new NullComparator())));
					}
				}
			}

			Collections.sort(toOrder, chain);
			return toOrder;
		}
		return new ArrayList();
	}
     
    /**
     * Hinzufügen von Feldern, welche nicht gespeichert werden sollen
     * @param fieldToExclude Feld, das nicht gespeichert werden soll
     */
    public void addFieldToExclude(String fieldToExclude) {
        fieldsToExclude.add(fieldToExclude.toUpperCase());
    }
 
    /**
     * Felder die gespeicher werden sollen. Per Default werden alle gespeichert.
     * @param fieldToInclude
     */
    public void addFieldToInclude(String fieldToInclude) {
        fieldsToInclude.add(fieldToInclude.toUpperCase());
    }
}
