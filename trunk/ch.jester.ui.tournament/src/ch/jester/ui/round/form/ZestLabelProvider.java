package ch.jester.ui.round.form;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.ui.tournament.internal.Activator;

public class ZestLabelProvider extends LabelProvider implements IEntityStyleProvider, ISelfStyleProvider{
	private Image playerImage;
	private Color ORANGE = new Color(Display.getDefault(), new RGB(238,118,33));
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
	
	/*private Player toPlayer(Object element){
		if(element instanceof PlayerDataNode){
			PlayerDataNode node = (PlayerDataNode) element;
			return node.getPlayer();
		}
		return null;
	}*/
	private PlayerDataNode toPlayerNode(Object element){
		if(element instanceof PlayerDataNode){
			PlayerDataNode node = (PlayerDataNode) element;
			return node;
		}
		return null;
	}
	private Pairing toPairing(Object element){
		if(element instanceof ZestDataNode){
			ZestDataNode node = (ZestDataNode) element;
			if(node.getData() instanceof Pairing){
				return (Pairing) node.getData();
			}
		}
		return null;
	}
	private Round toRound(Object element){
		if(element instanceof ZestDataNode){
			ZestDataNode node = (ZestDataNode) element;
			if(node.getData() instanceof Round){
				return (Round) node.getData();
			}
		}
		return null;
	}
	
	@Override
	public Color getNodeHighlightColor(Object entity) {
		return ORANGE;
	}
	@Override
	public Color getBorderColor(Object entity) {
		PlayerDataNode player = toPlayerNode(entity);
		if(player!=null && player.hasWon()){
			return Display.getDefault().getSystemColor (SWT.COLOR_GREEN);
		}
		return null;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return null;
	}
	@Override
	public int getBorderWidth(Object entity) {
		PlayerDataNode player = toPlayerNode(entity);
		if(player!=null && player.hasWon()){
			return 1;
		}
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
	@Override
	public void dispose() {
		super.dispose();
		if(playerImage!=null){
			playerImage.dispose();
		}
		if(ORANGE!=null){
			ORANGE.dispose();
		}
	}

	@Override
	public void selfStyleConnection(Object element, GraphConnection connection) {	
	}
	@Override
	public void selfStyleNode(Object element, GraphNode node) {
		node.setLocation(0, -100);
	/*	if(toRound(element)!=null){
			node.setLocation(1, 1);
		}
		if(toPairing(element)!=null){
			
		}*/
	}


}

