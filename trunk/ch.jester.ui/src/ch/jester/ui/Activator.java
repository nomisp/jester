package ch.jester.ui;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.commonservices.api.persistencyevent.IPersistencyEventQueue;
import ch.jester.commonservices.impl.persistencyevent.PersistencyEventQueue;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.PlayerEditor;
import ch.jester.ui.player.editor.PlayerInput;

public class Activator extends AbstractActivator{

	private static Activator mInstance;

	public static Activator getDefault(){
		return mInstance;
	}

	@Override
	public void startDelegate(BundleContext pContext) {
		mInstance=this;
		
		//Editor an InputTyp binden
		IEditorService openService = getActivationContext().getService(IEditorService.class);
		openService.register(Player.class, PlayerInput.class, PlayerEditor.ID);
		
		//EventQueue aufsetzen
		PersistencyEventQueue eventQueue = PersistencyEventQueue.getDefault();
		eventQueue.getSenderJob().setSystem(true);
		eventQueue.getSenderJob().schedule();		
		
		//Queue als Service registrieren
		getActivationContext().getServiceUtil().registerService(IPersistencyEventQueue.class, eventQueue);
		
		//ShutDown Hook für queue
		registerShutdownHookForQueue();
	}

	private void registerShutdownHookForQueue() {
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {
			
			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
				return true;
			}
			
			@Override
			public void postShutdown(IWorkbench workbench) {
				getActivationContext().getLogger().debug("Shutting down EventQueue");
				getActivationContext().getServiceUtil().getService(IPersistencyEventQueue.class).shutdown();
			}
		});
		
	}

	@Override
	public void stopDelegate(BundleContext pContext) {
		// TODO Auto-generated method stub
		
	}

}
