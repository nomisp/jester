package ch.jester.ui.tournament;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Wizard Seite um dem Turnier Kategorien hinzuzufügen
 * @author Peter
 *
 */
public class NewTournWizPageCategories extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public NewTournWizPageCategories() {
		super("CategoriesPage");
		setTitle("Categories");
		setDescription("Adding categories to the tournament");
		setPageComplete(true);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
	}

}
