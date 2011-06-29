package ch.jester.ui.round.form;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.IColorDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;

import ch.jester.model.Player;
import ch.jester.ui.tournament.internal.Activator;

public class ZestLabelProvider extends LabelProvider implements IEntityStyleProvider{
	private Image playerImage;
	private static Color ORANGE = new Color(Display.getDefault(), new RGB(238,118,33));
	@Override
	public String getText(Object element) {
		if (element instanceof ZestDataNode) {
			ZestDataNode myNode = (ZestDataNode) element;
			
			return myNode.getName();
		}
		// Not called with the IGraphEntityContentProvider
		if (element instanceof ZestConnection) {
			ZestConnection myConnection = (ZestConnection) element;
			return myConnection.getLabel();
		}

		if (element instanceof EntityConnectionData) {
			EntityConnectionData test = (EntityConnectionData) element;
			return "";
		}
		throw new RuntimeException("Wrong type: "
				+ element.getClass().toString());
	}
	@Override
	public Image getImage(Object element) {
		if(toPlayerNode(element)!=null){
				return getPlayerImage();
		}
		return null;
	}
	
	private Image getPlayerImage(){
		if(playerImage==null){
			playerImage = Activator.imageDescriptorFromPlugin("ch.jester.ui.tournament", "icons/player.png").createImage();
		}
		return playerImage;
	}
	
	private Player toPlayer(Object element){
		if(element instanceof PlayerDataNode){
			PlayerDataNode node = (PlayerDataNode) element;
			return node.getPlayer();
		}
		return null;
	}
	private PlayerDataNode toPlayerNode(Object element){
		if(element instanceof PlayerDataNode){
			PlayerDataNode node = (PlayerDataNode) element;
			return node;
		}
		return null;
	}
	
	@Override
	public Color getNodeHighlightColor(Object entity) {
		return ORANGE;
	}
	@Override
	public Color getBorderColor(Object entity) {
		return null;
	}
	@Override
	public Color getBorderHighlightColor(Object entity) {
		return null;
	}
	@Override
	public int getBorderWidth(Object entity) {
		return 0;
	}
	@Override
	public Color getBackgroundColour(Object entity) {
		Player player;
		if(toPlayerNode(entity)!=null){
			PlayerDataNode p = toPlayerNode(entity);
			if(p.isBlack()){
				Color color = Display.getDefault().getSystemColor (SWT.COLOR_GRAY);
				return color;
			}else{
				Color color = Display.getDefault().getSystemColor (SWT.COLOR_WHITE);
				return color;
			}
			
		}
		
		return null;
	}
	@Override
	public Color getForegroundColour(Object entity) {
		return null;
	}
	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}
	@Override
	public boolean fisheyeNode(Object entity) {
		return false;
	}

}

