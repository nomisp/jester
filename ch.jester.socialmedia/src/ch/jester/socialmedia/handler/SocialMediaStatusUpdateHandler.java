package ch.jester.socialmedia.handler;



import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.socialmedia.api.ISocialStatusUpdater;
import ch.jester.socialmedia.dialog.UpdateStatusDialog;

public class SocialMediaStatusUpdateHandler extends AbstractCommandHandler{
	public final static String PARAM = "ch.jester.socialmedia.statusupdater.serviceid";
	@Override
	public Object executeInternal(ExecutionEvent event) {
		String serviceid = event.getParameter(PARAM);
		Object object = getServiceUtil().getService(serviceid);
		if(!(object instanceof ISocialStatusUpdater)){
			
			mLogger.info("Wrong configured: Object in "+PARAM+" must implement interface ISocialStatusUpdater ("+object+")");
			return null;
		}
		
		UpdateStatusDialog dialog = new UpdateStatusDialog(Display.getDefault().getActiveShell(), SWT.BORDER, (ISocialStatusUpdater)object);
		Image img = tryGetImage(event);
		if(img!=null){
			dialog.setImage(img);
		}
		
		dialog.open();
		return null;
	}
	
	private Image tryGetImage(ExecutionEvent e){
		try{
		Object trigger = e.getTrigger();
		Event eventtrigger = (Event) trigger;
		if(eventtrigger.widget instanceof ToolItem){
			return ((ToolItem)eventtrigger.widget).getImage();
		}
		
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return null;
	}

}
