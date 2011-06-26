package ch.jester.ui.tournament.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;

public class CategoryMasterDetail extends MasterDetailsBlock {

	private FormPage page;
	private FormToolkit toolkit;

	/**
	 * Create the master details block.
	 */
	public CategoryMasterDetail(FormPage page) {
		this.page = page;
	}

	/**
	 * Create contents of the master details block.
	 * @param managedForm
	 * @param parent
	 */
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		toolkit = managedForm.getToolkit();
		//		
		Section sctnCategories = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		sctnCategories.setText("Categories");
		//
		Composite tableComposite = toolkit.createComposite(sctnCategories, SWT.NONE);
		toolkit.paintBordersFor(tableComposite);
		sctnCategories.setClient(tableComposite);
		GridLayout gl_tableComposite = new GridLayout(2, false);
		tableComposite.setLayout(gl_tableComposite);
		
		Section sctnAllCategories = toolkit.createSection(tableComposite, Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnAllCategories = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnAllCategories.widthHint = 281;
		sctnAllCategories.setLayoutData(gd_sctnAllCategories);
		toolkit.paintBordersFor(sctnAllCategories);
		sctnAllCategories.setText("All Categories");
		sctnAllCategories.setExpanded(true);
		
		Composite composite = toolkit.createComposite(sctnAllCategories, SWT.NONE);
		toolkit.paintBordersFor(composite);
		sctnAllCategories.setClient(composite);
		composite.setLayout(new FormLayout());
		
		Section sctnCategoryDetails = toolkit.createSection(tableComposite, Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnCategoryDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnCategoryDetails.widthHint = 326;
		sctnCategoryDetails.setLayoutData(gd_sctnCategoryDetails);
		toolkit.paintBordersFor(sctnCategoryDetails);
		sctnCategoryDetails.setText("Category Details");
		sctnCategoryDetails.setExpanded(true);
		
		Composite composite_1 = toolkit.createComposite(sctnCategoryDetails, SWT.NONE);
		toolkit.paintBordersFor(composite_1);
		sctnCategoryDetails.setClient(composite_1);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		// Register the pages
	}

	/**
	 * Create the toolbar actions.
	 * @param managedForm
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// Create the toolbar actions
	}
}
