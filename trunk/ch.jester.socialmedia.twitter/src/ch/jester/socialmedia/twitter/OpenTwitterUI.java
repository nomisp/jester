package ch.jester.socialmedia.twitter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import twitter4j.TwitterException;

import ch.jester.common.ui.handlers.AbstractCommandHandler;

public class OpenTwitterUI extends AbstractCommandHandler {

	@Override
	public Object executeInternal(ExecutionEvent event) {
		TwitterDialog dialog = new TwitterDialog(Display.getDefault().getActiveShell(), SWT.BORDER);
		String status = dialog.open();
		if(!dialog.OKPressed()){
			return null;
		}
		try {
			getServiceUtil().getService(TwitterService.class).getTwitter().postStatus(status);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		mLogger.debug("Twitter Status sent: "+status);
		return null;
	}

}
