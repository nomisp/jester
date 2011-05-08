package ch.jester.common.ui.databinding;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editor.EditorAccessor.EditorAccess;
import ch.jester.common.ui.editor.IEditorInputAccess;
import ch.jester.common.ui.internal.Activator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.PartListener2Adapter;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.persistency.IDaoObject;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.util.ServiceUtility;





public abstract class DaoController<T extends IDaoObject>{
	private TableViewer mViewer;
	private ServiceUtility mServices = Activator.getDefault().getActivationContext().getServiceUtil();
	private IDaoService<T> persister;
	private ViewPart mPart;
	private PageController<T> pageController;
	private DataBindingContext context;
	private WritableList obsModel;
	
	public Class<?> getTargetClass(){
		return persister.getDaoClass();
	}


	public DaoController(ViewPart pPart, TableViewer pViewer, IDaoService<T> pdaoservice){
		int cacheSize = 1000;
		persister = pdaoservice;
		mViewer=pViewer;
		context = new DataBindingContext();
		obsModel = new WritableList(new ArrayList<T>(), pdaoservice.getDaoClass());
		pageController = new PageController<T>(obsModel,new TableViewerAdapter(mViewer), persister, cacheSize);
		mPart=pPart;
		mViewer.setInput(obsModel);
		ObservableListContentProvider contentProvider;
		mViewer.setContentProvider(contentProvider = new ObservableListContentProvider());
		ObservableMapLabelProvider lblprov = new ObsMapLblProv(Properties
				.observeEach(contentProvider.getKnownElements(),
						BeanProperties.values(
								observableProperties())));
		mViewer.setLabelProvider(lblprov);
	}

	public abstract String[] observableProperties();
	
	public abstract String callBackLabels(T pDao);

	protected IPartService getPartService(){
		return (IPartService)mPart.getSite().getService(IPartService.class);
	}
	
	public void addDaoObject(T pPlayer) {
		addDaoObject(createList(pPlayer));
	}
	public void addDaoObject(Collection<T> pPlayerCollection) {
		obsModel.addAll(pPlayerCollection);
		context.updateTargets();
	}
	@SuppressWarnings("rawtypes")
	public void openEditor(Object pObject){
		IEditorService eService = mServices.getService(IEditorService.class);
		if(!eService.isEditorRegistred(pObject)){return;}
		final EditorAccess access = eService.openEditor(pObject);	
		System.out.println("reactivated: "+access.wasReactivated());
		if(!access.wasReactivated()){
			((AbstractEditor)access.getPart()).setPlayerDao(persister);
			getPartService().addPartListener(new NestedPartListener(access));
		}
	}
	
	public void removeDaoObject(T pObject) {
		removeDaoObject(createList(pObject));
	}
	public void removeDaoObject(final List<T> pList) {
		
		UIUtility.syncExecInUIThread(new Runnable() {
			@Override
			public void run() {
				synchronized(persister){
				persister.delete(pList);
				obsModel.removeAll(pList);
				context.updateTargets();
				}
				
			}
		});

	}
	
	/**
	 * Für die Search
	 * @param pPlayerCollection
	 */
	public void setSearched(final Collection<T> pPlayerCollection){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setInput(new WritableList(pPlayerCollection, persister.getDaoClass()));
				
			}
		});

		
	}
	public void clearSearched(){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				mViewer.setInput(pageController.getPageContent());
				
			}
		});
	
	}
	
	private List<T> createList(T o){
		ArrayList<T> list = new ArrayList<T>();
		list.add(o);
		return list;
	}

	public PageController<?> getPageController() {
		return pageController;
	}
	
	class NestedPartListener extends PartListener2Adapter{
		EditorAccess mAccess;
		public NestedPartListener(EditorAccess pAccess){
			mAccess=pAccess;
		}
		
		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			if(partRef.getPart(false) == mAccess.getPart()){
				AbstractEditor closedEditor = (AbstractEditor) mAccess.getPart();
				if(!closedEditor.wasSaved()){
					IEditorInputAccess<?> input = (IEditorInputAccess<?>)closedEditor.getEditorInput();
					obsModel.remove(input.getInput());
				}
				System.out.println("Was saved: "+closedEditor.wasSaved());
				getPartService().removePartListener(this);
				//mAccess.close();
			}
	}
	}
	
	class ObsMapLblProv extends ObservableMapLabelProvider{

		public ObsMapLblProv(IObservableMap[] attributeMaps) {
			super(attributeMaps);
			
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return getText(element);
		}
		@Override
		public String getText(Object element) {
			return DaoController.this.callBackLabels((T) element);
		}
		
	}

}