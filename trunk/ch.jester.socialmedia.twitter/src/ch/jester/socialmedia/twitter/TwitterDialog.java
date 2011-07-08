package ch.jester.socialmedia.twitter;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class TwitterDialog extends Dialog {

	protected String result;
	protected Shell shlTwitterStatusUpdater;
	private Text text;
	private Label charCountText;
	private boolean okPressed;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TwitterDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}
	
	public boolean OKPressed(){
		return okPressed;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open() {
		createContents();
		shlTwitterStatusUpdater.open();
		shlTwitterStatusUpdater.layout();
		Display display = getParent().getDisplay();
		while (!shlTwitterStatusUpdater.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlTwitterStatusUpdater = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shlTwitterStatusUpdater.setImage(ResourceManager.getPluginImage("ch.jester.socialmedia.twitter", "icon/twitter.png"));
		shlTwitterStatusUpdater.setSize(450, 300);
		shlTwitterStatusUpdater.setText("Twitter Status Updater");
		shlTwitterStatusUpdater.setLayout(new GridLayout(5, true));
		
		text = new Text(shlTwitterStatusUpdater, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		text.setTextLimit(140);
		
		Button btnNewButton = new Button(shlTwitterStatusUpdater, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Update");
		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = text.getText();
				okPressed=true;
				shlTwitterStatusUpdater.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		Button btnNewButton_1 = new Button(shlTwitterStatusUpdater, SWT.NONE);
		btnNewButton_1.setText("Cancel");
		btnNewButton_1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlTwitterStatusUpdater.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		new Label(shlTwitterStatusUpdater, SWT.NONE);
		new Label(shlTwitterStatusUpdater, SWT.NONE);
		
		charCountText = new Label(shlTwitterStatusUpdater, SWT.SHADOW_NONE | SWT.RIGHT);
		charCountText.setEnabled(false);
		charCountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		charCountText.setText("0 / 140");
		
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {	
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int actLength = text.getText().length();
				charCountText.setText(actLength+" / "+140);
			}
		});

	}
}
