package ch.jester.socialmedia.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.jester.socialmedia.api.ISocialStatusUpdater;

public class UpdateStatusDialog extends Dialog {

	protected String result;
	protected Shell shlStatusUpdater;
	private Text text;
	private Label charCountText;
	private boolean okPressed;
	private ISocialStatusUpdater mSocialUpdater;
	private Button btnNewButton;
	private Image mImage;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UpdateStatusDialog(Shell parent, int style, ISocialStatusUpdater pSocialUpdater) {
		super(parent, style);
		setText("SWT Dialog");
		mSocialUpdater = pSocialUpdater;
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
		shlStatusUpdater.open();
		shlStatusUpdater.layout();
		Display display = getParent().getDisplay();
		while (!shlStatusUpdater.isDisposed()) {
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
		shlStatusUpdater = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		if(mImage!=null){
			shlStatusUpdater.setImage(mImage);
		}
		shlStatusUpdater.setSize(450, 300);
		shlStatusUpdater.setText("Status updater");
		shlStatusUpdater.setLayout(new GridLayout(5, true));
		
		text = new Text(shlStatusUpdater, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		text.setTextLimit(mSocialUpdater.getMaxCharacterForStatus());
		
		btnNewButton = new Button(shlStatusUpdater, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Update");

		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = text.getText();
				mSocialUpdater.updateStatus(result);
				reset();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		Button btnNewButton_1 = new Button(shlStatusUpdater, SWT.NONE);
		btnNewButton_1.setText("Cancel");
		btnNewButton_1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlStatusUpdater.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		new Label(shlStatusUpdater, SWT.NONE);
		new Label(shlStatusUpdater, SWT.NONE);
		
		charCountText = new Label(shlStatusUpdater, SWT.SHADOW_NONE | SWT.RIGHT);
		charCountText.setEnabled(false);
		charCountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		reset();
		
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {	
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(text.getText().isEmpty()){
					btnNewButton.setEnabled(false);
				}else{
					btnNewButton.setEnabled(true);
				}
				int actLength = text.getText().length();
				charCountText.setText(actLength+" / "+mSocialUpdater.getMaxCharacterForStatus());
			}
		});
		reset();
	}

	private void reset() {
		charCountText.setText("0 / "+mSocialUpdater.getMaxCharacterForStatus());
		btnNewButton.setEnabled(false);
		text.setText("");
		
	}

	public void setImage(Image img) {
		mImage = img;
		
	}
}
