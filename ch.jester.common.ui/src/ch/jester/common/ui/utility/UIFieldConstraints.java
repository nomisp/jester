package ch.jester.common.ui.utility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import ch.jester.model.util.PropertyAttributesAnalyser;

public class UIFieldConstraints {
	Class<?> mClass;
	private PropertyAttributesAnalyser paa; 
	private List<ControlDecoration> errorContstraints = new ArrayList<ControlDecoration>();
	private int errorCount;
	public UIFieldConstraints(Class<?> pClass) {
		mClass = pClass;
		paa= new PropertyAttributesAnalyser(pClass);
	}
	public boolean hasErrors(){
		return errorCount>0;
	}
	public void addConstraint(final Text pText, final String pProperty){
		pText.setTextLimit(paa.getLength(pProperty));
		final ControlDecoration cd = new ControlDecoration(pText, SWT.LEFT);
		errorContstraints.add(cd);
		
		Image eImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
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
			private void hide(){
				if(!cd.isVisible()){return;}
				cd.hide();
				if(errorCount>0){
					errorCount--;
				}
			}
			private void show(){
				if(cd.isVisible()){return;}
				cd.show();
				errorCount++;
			}
			private void check(){
				Class<?> cls = paa.getType(pProperty);
				if(cls==String.class){return;}
				
				if(pText.getText().isEmpty()){
					hide();
					return;
				}
				
				if(cls==Integer.class){
					try{
						Integer.parseInt(pText.getText());
						hide();
					}catch(Exception ex){
						show();
					}
					return;
				}
				if(cls==Float.class){
					try{
						Float.parseFloat(pText.getText());
						hide();
					}catch(Exception ex){
						show();
					}
					return;
				}
				if(cls==Double.class){
					try{
						Double.parseDouble(pText.getText());
						hide();
					}catch(Exception ex){
						show();
					}
					return;
				};
			}
		});
		
	}
}
