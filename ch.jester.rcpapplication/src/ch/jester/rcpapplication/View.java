package ch.jester.rcpapplication;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.Session;

import ch.jester.hibernate.helper.ConfigurationHelper;
import ch.jester.model.Player;

public class View extends ViewPart {
	public View() {
	}
	public static final String ID = "ch.jester.rcpapplication.view";

	private TableViewer viewer;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		ConfigurationHelper ch = new ConfigurationHelper();
		String catalog="???";
		Session ssn = ch.getSession();
		Connection con = ssn.connection();
		try {
			catalog = con.getCatalog();		
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		new Job("insert"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("jester"); 
				EntityManager em = emf.createEntityManager();
				
				for(int i=0;i<50000;i++){
					em.getTransaction().begin();
				Player player = new Player();
				player.setCity("Zürich");
				player.setElo(i);
				player.setFideCode(9);
				player.setFirstName("matthias");
				player.setLastName("liechti");
				player.setNation("CH");
				em.persist(player);
				em.getTransaction().commit();
				}
				
			
				em.close();
				
				return Status.OK_STATUS;
			}
			
		}.schedule();

		
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		// Provide the input to the ContentProvider
		viewer.setInput(new String[] {"JDBCDriver : " + ch.getConnectiondriverclass(),
				"SQLDialect : " + ch.getSqldialect(),
				"User       : " + ch.getUser(),
				"Password   : " + ch.getPassword(),
				"Ip-Adresse : " + ch.getIp(),
				"ConnectionURL : " + ch.getConnectionurl(),
				"Catalog : " + catalog
				});
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}