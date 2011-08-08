package ch.jester.ui.round.form;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

/**
 * ContentProvider für den Graphen
 *
 */
public class ZestNodeContentProvider extends ArrayContentProvider  implements IGraphEntityContentProvider {

	@Override
	public Object[] getConnectedTo(Object entity) {
		if (entity instanceof ZestDataNode) {
			ZestDataNode node = (ZestDataNode) entity;
			return node.getConnectedTo().toArray();
		}
		throw new RuntimeException("Type not supported");
	}
}

