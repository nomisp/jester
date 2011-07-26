package ch.jester.server.prefpage;



import messages.Messages;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.widgets.Button;

import ch.jester.server.OSGiGatewayActivator;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class StartServerPrefPage extends PreferencePage implements IWorkbenchPreferencePage{
	public StartServerPrefPage() {
	}
	StartStopModel model = new StartStopModel();
	Button btnStart;
	Button btnStop;
	private Text text;
	private Label lblNewLabel_1;
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		btnStart = new Button(container, SWT.NONE);
		btnStart.setBounds(21, 56, 68, 23);
		btnStart.setText(Messages.StartServerPrefPage_btn_start);
		btnStart.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.start();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				model.start();
				
			}
		});
		btnStart.setEnabled(!model.isRunning());
		
		btnStop = new Button(container, SWT.NONE);
		btnStop.setBounds(112, 56, 68, 23);
		btnStop.setText(Messages.StartServerPrefPage_btn_stop);
		btnStop.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.stop();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				model.stop();
				
			}
		});
		btnStop.setEnabled(model.isRunning());
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(21, 37, 68, 13);
		lblNewLabel.setText(Messages.StartServerPrefPage_lbl_status);
		
		text = new Text(container, SWT.NONE);
		text.setEnabled(false);
		text.setEditable(false);
		text.setBounds(112, 37, 76, 19);
		text.setText(model.getStatusText());
		
		lblNewLabel_1 = new Label(container, SWT.WRAP);
		lblNewLabel_1.setBounds(21, 10, 343, 23);
		lblNewLabel_1.setText(Messages.StartServerPrefPage_lbl_desc);
		return container;
	}
	
	class StartStopModel{
		public boolean isRunning(){
			return OSGiGatewayActivator.getDefault().isRunning();
		}
		public void start(){
			OSGiGatewayActivator.getDefault().startServer();
			updateButtons();	

		}
		public void stop(){
			OSGiGatewayActivator.getDefault().stopServer();
			updateButtons();
		}
		void updateButtons(){
			btnStart.setEnabled(!isRunning());
			btnStop.setEnabled(isRunning());
			text.setText(getStatusText());
		}
		String getStatusText(){
			if(isRunning()){
				return Messages.StartServerPrefPage_status_online;
			}else{
				return Messages.StartServerPrefPage_status_offline;
			}
		}
	}

	public void init(IWorkbench workbench) {
	}
}
