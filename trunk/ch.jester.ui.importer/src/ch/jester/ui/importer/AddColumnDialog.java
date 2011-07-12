package ch.jester.ui.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import ch.jester.common.importer.VirtualCell;
import ch.jester.common.ui.adapters.TableLabelProviderAdapter;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.common.ui.utility.SelectionUtility;
import ch.jester.commonservices.api.importer.IVirtualTable.IVirtualCell;

public class AddColumnDialog extends Dialog implements SelectionListener {
	private static int colcounter = 1;
	private Text mText;
	private List<IVirtualCell> mCells = new ArrayList<IVirtualCell>();
	private Table mTable;
	private TableViewer tableViewer;
	private IVirtualCell mSelectedCell;
	private Text mTxtSeq, mTxtName, mTxtDelim;
	private List<String> mInput;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public AddColumnDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.AddColumnDialog_lbl_btn_modify);
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, true));

		Label lblInputSelection = new Label(container, SWT.NONE);
		lblInputSelection.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblInputSelection
				.setText(Messages.AddColumnDialog_lbl_input_selection);

		mText = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		GridData gd_mText = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_mText.heightHint = 154;
		gd_mText.widthHint = 506;
		mText.setLayoutData(gd_mText);
		mText.setFont(SWTResourceManager.getFont("Courier", 9, SWT.NORMAL)); //$NON-NLS-1$

		Menu menu = new Menu(mText);
		mText.setMenu(menu);

		MenuItem mntmAddColumn = new MenuItem(menu, SWT.NONE);
		mntmAddColumn.setText(Messages.AddColumnDialog_lbl_newcolumn);
				
						Group grpColumns = new Group(container, SWT.NONE);
						GridData gd_grpColumns = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
						gd_grpColumns.heightHint = 156;
						grpColumns.setLayoutData(gd_grpColumns);
						grpColumns.setText(Messages.AddColumnDialog_lbl_columns);
						
								tableViewer = new TableViewer(grpColumns, SWT.BORDER
										| SWT.FULL_SELECTION);
								mTable = tableViewer.getTable();
								mTable.setBounds(10, 18, 232, 132);
								
										TableViewerColumn tableViewerColumn = new TableViewerColumn(
												tableViewer, SWT.NONE);
										TableColumn tblclmnAttribute = tableViewerColumn.getColumn();
										tblclmnAttribute.setWidth(170);
										tblclmnAttribute.setText(Messages.AddColumnDialog_lbl_attributes);
										
												Menu menu_1 = new Menu(mTable);
												mTable.setMenu(menu_1);
												
														MenuItem mntmDelete = new MenuItem(menu_1, SWT.NONE);
														mntmDelete.setText(Messages.AddColumnDialog_lbl_delete);
														mntmDelete.addSelectionListener(new SelectionAdapter() {
															SelectionUtility su = new SelectionUtility(null);

															@Override
															public void widgetSelected(SelectionEvent e) {
																ISelection selection = tableViewer.getSelection();
																if (selection == null) {
																	return;
																}
																su.setSelection(selection);
																IVirtualCell c = su.getFirstSelectedAs(IVirtualCell.class);
																mCells.remove(c);
																tableViewer.setInput(mCells);

															}
														});
														
																tableViewer.setLabelProvider(new TableLabelProviderAdapter() {
																	@Override
																	public String getColumnText(Object element, int columnIndex) {
																		return ((IVirtualCell) element).getName();
																	}
																});
																tableViewer.setContentProvider(ArrayContentProvider.getInstance());
																
																		tableViewer
																				.addSelectionChangedListener(new ISelectionChangedListener() {
																					SelectionUtility su = new SelectionUtility(null);
																
																					@Override
																					public void selectionChanged(SelectionChangedEvent event) {
																						su.setSelection(event.getSelection());
																						IVirtualCell c = su
																								.getFirstSelectedAs(IVirtualCell.class);
																						mSelectedCell = c;
																						if (c == null) {
																							mTxtDelim.setEnabled(false);
																							mTxtSeq.setEnabled(false);
																							mTxtName.setEnabled(false);
																							return;
																						}
																						mTxtDelim.setEnabled(true);
																						mTxtSeq.setEnabled(true);
																						mTxtName.setEnabled(true);
																						if (c.getDelimiter() != null) {
																							mTxtDelim.setText(c.getDelimiter());
																						} else {
																							mTxtDelim.setText(""); //$NON-NLS-1$
																						}
																						if (c.getDelimiterSequence() != -1) {
																							mTxtSeq.setText(c.getDelimiterSequence() + ""); //$NON-NLS-1$
																						} else {
																							mTxtSeq.setText(""); //$NON-NLS-1$
																						}
																						if (c.getName() != null) {
																							mTxtName.setText(c.getName());
																							tableViewer.refresh();
																						} else {
																							mTxtName.setText(""); //$NON-NLS-1$
																							tableViewer.refresh();
																						}
																
																						mText.setSelection(c.getStart(), c.getStop());
																
																					}
																				});
		
				Group grpAttributes = new Group(container, SWT.NONE);
				GridData gd_grpAttributes = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
				gd_grpAttributes.heightHint = 157;
				grpAttributes.setLayoutData(gd_grpAttributes);
				grpAttributes.setText(Messages.AddColumnDialog_lbl_properties);
				
						mTxtSeq = new Text(grpAttributes, SWT.BORDER);
						mTxtSeq.setBounds(71, 75, 76, 21);
						mTxtSeq.setEnabled(false);
						
								mTxtDelim = new Text(grpAttributes, SWT.BORDER);
								mTxtDelim.setBounds(71, 48, 76, 21);
								mTxtDelim.setEnabled(false);
								
										Label lblMatch = new Label(grpAttributes, SWT.NONE);
										lblMatch.setBounds(10, 78, 55, 15);
										lblMatch.setText(Messages.AddColumnDialog_lbl_sequence);
										
												Label lblDelimiter = new Label(grpAttributes, SWT.NONE);
												lblDelimiter.setBounds(10, 51, 55, 15);
												lblDelimiter.setText(Messages.AddColumnDialog_lbl_delimiter);
												
														Label lblName = new Label(grpAttributes, SWT.NONE);
														lblName.setBounds(10, 23, 55, 15);
														lblName.setText(Messages.AddColumnDialog_lbl_name);
														
																mTxtName = new Text(grpAttributes, SWT.BORDER);
																mTxtName.setBounds(71, 23, 76, 21);
																mTxtName.setEnabled(false);
																mTxtDelim.addModifyListener(new ModifyListener() {

																	@Override
																	public void modifyText(ModifyEvent e) {
																		getSelectedCell().setDelimiter(mTxtDelim.getText());

																	}
																});
																mTxtSeq.addModifyListener(new ModifyListener() {

																	@Override
																	public void modifyText(ModifyEvent e) {
																		int i = 0;
																		try {
																			i = Integer.parseInt(mTxtSeq.getText());
																		} catch (NumberFormatException e0) {
																			return;
																		}
																		getSelectedCell().setDelimiterSequence(i);

																	}
																});
																
																		mTxtName.addKeyListener(new KeyAdapter() {
																			@Override
																			public void keyReleased(KeyEvent e) {
																				getSelectedCell().setName(mTxtName.getText());
																				tableViewer.refresh();
																
																			}
																		});
		mntmAddColumn.addSelectionListener(this);

		return container;
	}

	public IVirtualCell getSelectedCell() {
		return mSelectedCell;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(544, 471);
	}

	public void setInputText(String[] pInput) {
		mInput = Arrays.asList(pInput);
		StringBuilder flatInput = new StringBuilder();
		for (int i = 0; i < mInput.size(); i++) {
			if (!mInput.get(i).endsWith("\r\n")) { //$NON-NLS-1$
				mInput.set(i, mInput.get(i) + "\r\n"); //$NON-NLS-1$
			}
			flatInput.append(mInput.get(i));
		}
		mText.setText(flatInput.toString());
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		SafeRunner.run(new SafeMessageBoxRunner() {
			@Override
			public void run() throws Exception {
				createColumnFromSelection();
				
			}
		});
	}

	private void createColumnFromSelection() {
		final String name = Messages.AddColumnDialog_predefined_column_name + colcounter;
		colcounter++;
		String selection = mText.getSelectionText();
		if (selection.contains("\n")) { //$NON-NLS-1$
			throw new RuntimeException(Messages.AddColumnDialog_exception_linefeed);
		}

		Point selectionPoint = mText.getSelection();
		int selectionLineNr = mText.getCaretLineNumber();
		int lfBeforeSelection = selectionPoint.x;
		String entireText = mText.getText();
		for (; lfBeforeSelection >= 0; lfBeforeSelection--) {
			if (entireText.charAt(lfBeforeSelection) == '\n') {
				lfBeforeSelection++;
				break;
			}
		}
		final int cellStart = selectionPoint.x - lfBeforeSelection;
		final int cellStop = cellStart + selection.length();
		boolean check = mInput.get(selectionLineNr)
				.substring(cellStart, cellStop).equals(selection);
		Assert.isTrue(check, "Testselection falsch"); //$NON-NLS-1$

		IVirtualCell c = new VirtualCell(name, cellStart, cellStop);
		mCells.add(c);
		AddColumnDialog.this.tableViewer.setInput(mCells);
	}

	public List<IVirtualCell> getCell() {
		return mCells;
	}

	public void setCell(List<IVirtualCell> cells) {
		this.mCells.addAll(cells);
		this.tableViewer.setInput(cells);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	

}
