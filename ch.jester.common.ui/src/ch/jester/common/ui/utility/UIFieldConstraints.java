package ch.jester.common.ui.utility;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import ch.jester.model.util.PropertyAttributesAnalyser;

/**
 * Hilfsklasse um Constraints von den Entitäten im UI zu überprüfen. Falls
 * Errors vorhanden sind, werden entsprechende ControlDecorations angezeigt.
 * 
 */
public class UIFieldConstraints {
	@SuppressWarnings("unused")
	private Class<?> mClass;
	private PropertyAttributesAnalyser paa;
	private List<ControlDecoration> errorContstraints = new ArrayList<ControlDecoration>();
	private int errorCount;

	public UIFieldConstraints(Class<?> pClass) {
		mClass = pClass;
		paa = new PropertyAttributesAnalyser(pClass);
	}

	/**
	 * Zeigt ob Fehler im UI vorhanden sind
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errorCount > 0;
	}

	/**
	 * Hinzufügen eines Feldes, welches überwacht werden soll.
	 * 
	 * @param pText
	 * @param pProperty
	 */
	public void addConstraint(final Text pText, final String pProperty) {
		pText.setTextLimit(paa.getLength(pProperty));
		final ControlDecoration cd = new ControlDecoration(pText, SWT.LEFT);
		errorContstraints.add(cd);

		Image eImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
				.getImage();
		cd.setImage(eImage);
		cd.hide();
		pText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				check();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				check();

			}

			private void hide() {
				if (!cd.isVisible()) {
					return;
				}
				cd.hide();
				cd.setDescriptionText(Messages.UIFieldConstraints_0);
				if (errorCount > 0) {
					errorCount--;
				}
			}

			private void show(String message) {
				if (cd.isVisible()) {
					return;
				}
				cd.show();
				cd.setDescriptionText(message);
				errorCount++;
			}

			private void check() {
				Class<?> cls = paa.getType(pProperty);
				if (cls == String.class) {
					return;
				}

				if (pText.getText().isEmpty()) {
					hide();
					return;
				}

				if (cls == Integer.class) {
					try {
						Integer.parseInt(pText.getText());
						hide();
					} catch (Exception ex) {
						show(Messages.UIFieldConstraints_integer);
					}
					return;
				}
				if (cls == Float.class) {
					try {
						Float.parseFloat(pText.getText());
						hide();
					} catch (Exception ex) {
						show(Messages.UIFieldConstraints_float);
					}
					return;
				}
				if (cls == Double.class) {
					try {
						Double.parseDouble(pText.getText());
						hide();
					} catch (Exception ex) {
						show(Messages.UIFieldConstraints_double);
					}
					return;
				}
				;
			}
		});

	}
}
