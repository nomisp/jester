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
import ch.jester.common.ui.handlers.api.IHandlerAdd;
import ch.jester.common.ui.handlers.api.IHandlerDelete;
import ch.jester.common.ui.handlers.api.IHandlerEditor;
import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.common.ui.utility.PartListener2Adapter;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.commonservices.util.ServiceUtility;





/**
 * Controller welcher ein Paging unterstützt.
 * Die aktuelle Paging Selektion wird dabei in eine WritableList geschrieben, 
 * so dass der Standard Databindmechnismus verwendet werden kann.
 * 
 * Updates von Objekte passieren zuerst in der WritableList.
 *
 * @param <T>
 */
public abstract class DaoController<T extends IEntityObject> implements IHandlerDelete<T>, IHandlerAdd<T>, IHandlerEditor<T> {
	private TableViewer mViewer;
	private ServiceUtility mServices = CommonUIActivator.getDefault().getActivationContext().getServiceUtil();
	private ILogger mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
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

	/**
	 * Die zu observierenden Properties
	 * @return
	 */
	public abstract String[] observableProperties();
	
	/**
	 * Label erstellung für den Integrierten Labelprovider
	 * @param pDao
	 * @return
	 */
	public abstract String callBackLabels(T pDao);
	
	/**
	 * Label erstellung für den Integrierten Labelprovider (für Cols)
	 * @param pDao
	 * @param col
	 * @return
	 */
	public  String indexedCallBackLabels(T pDao, int col){return null;};

	protected IPartService getPartService(){
		return (IPartService)mPart.getSite().getService(IPartService.class);
	}
	
	@Override
	public void handleAdd(T pPlayer) {
		handleAdd(createList(pPlayer));
	}

	@Override
	public void handleAdd(Collection<T> pPlayerCollection) {
		obsModel.addAll(pPlayerCollection);
		context.updateTargets();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void handleOpenEditor(Object pObject){
		IEditorService eService = mServices.getService(IEditorService.class);
		if(!eService.isEditorRegistred(pObject)){return;}
		final EditorAccess access = eService.openEditor(pObject);	
		mLogger.debug("Editor Reactivated: "+access.wasReactivated());
		if(!access.wasReactivated()){
			((AbstractEditor)access.getPart()).setDaoService(persister);
			getPartService().addPartListener(new NestedPartListener(access));
		}
	}
	@Override
	public void handleDelete(final List<T> pList) throws ProcessingException {
		//ProcessingException exception;
		abstract class ExceptionRunnable implements Runnable {
			ProcessingException e;
			public ProcessingException getException(){
				return e;
			}
			public void setException(ProcessingException e){
				this.e=e;
			}
		};
		ExceptionRunnable runner;
		UIUtility.syncExecInUIThread(runner = new ExceptionRunnable() {
			
			@Override
			public void run() {
				synchronized(persister){
				try{
					persister.delete(pList);
					obsModel.removeAll(pList);
				}catch(ProcessingException e){
					setException(e);
				
				}catch(Exception e){
					setException(new ProcessingException(e));
				}finally{
					context.updateTargets();
				}
				}
				
			}
		});
		if(runner.getException()!=null){
			throw runner.getException();
		}

	}
	
	/**
	 * Für die Search
	 * @param pPlayerCollection
	 */
	public void setSearched(final Collection<T> pPlayerCollection){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				pageController.enablePaging(false);
				mViewer.setInput(new WritableList(pPlayerCollection, persister.getDaoClass()));
				
			}
		});

		
	}
	/**
	 * Löschen der Suche
	 */
	public void clearSearched(){
		UIUtility.syncExecInUIThread(new Runnable() {
			
			@Override
			public void run() {
				pageController.enablePaging(true);
				mViewer.setInput(pageController.getPageContent());
				
			}
		});
	
	}
	
	private List<T> createList(T o){
		ArrayList<T> list = new ArrayList<T>();
		list.add(o);
		return list;
	}

	/**
	 * Gibt Zugriff auf den internen PageController
	 * @return den verwendeten PageController
	 */
	public PageController<?> getPageController() {
		return pageController;
	}
	
	class NestedPartListener extends PartListener2Adapter{
		EditorAccess mAccess;
		public NestedPartListener(EditorAccess pAccess){
			mAccess=pAccess;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			if(partRef.getPart(false) == mAccess.getPart()){
				AbstractEditor<IEntityObject> closedEditor = (AbstractEditor<IEntityObject>) mAccess.getPart();
				
				if(!closedEditor.wasSaved()){
					IEditorInputAccess<IEntityObject> input = (IEditorInputAccess<IEntityObject>)closedEditor.getEditorInput();
					//TODO Issue!!
					if(input.getInput().getId()==null){
						obsModel.remove(input.getInput());
					}
				}
				mLogger.debug("Editor wasSaved: "+closedEditor.wasSaved());
				getPartService().removePartListener(this);
				//mAccess.close();
			}
	}
	}
	
	/**
	 * Der Label Provider
	 *
	 */
	class ObsMapLblProv extends ObservableMapLabelProvider{

		public ObsMapLblProv(IObservableMap[] attributeMaps) {
			super(attributeMaps);
			
		}
		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex) {
			String txt = DaoController.this.indexedCallBackLabels((T) element, columnIndex);
			if(txt!=null){
				return txt;
			}
			return getText(element);
		}
		@SuppressWarnings("unchecked")
		@Override
		public String getText(Object element) {
			return DaoController.this.callBackLabels((T) element);
		}
		
	}

}
