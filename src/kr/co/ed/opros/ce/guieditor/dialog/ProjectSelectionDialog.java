package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.List;

import kr.co.ed.opros.ce.provider.ProjectListContentProvider;
import kr.co.ed.opros.ce.provider.TreeLabelProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ProjectSelectionDialog extends Dialog {
	
	public CheckboxTableViewer fTreeViewer;
	
	public Object[] selectedItem;	
	
	public List<IProject> input;

	public ProjectSelectionDialog(Shell shell, List<IProject> input) {
		super(shell);
		this.input = input;
	}

	@Override
	protected Control createDialogArea(Composite parent) {		
		Group group = new Group(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.heightHint = 300;
		gd.widthHint = 320;
		group.setLayoutData(gd);
		group.setLayout(new GridLayout(1, false));
		
		Label label = new Label(group, SWT.NONE);
		label.setText("Please select a project to change the compile options.");
		
		fTreeViewer = CheckboxTableViewer.newCheckList(group, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(fTreeViewer.getControl());
		fTreeViewer.setContentProvider(new ProjectListContentProvider());
		fTreeViewer.setLabelProvider(new TreeLabelProvider());
		fTreeViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
			}
		});
		
		fTreeViewer.setInput(input);		
		return super.createDialogArea(parent);
	}

	@Override
	protected void okPressed() {
		selectedItem = fTreeViewer.getCheckedElements();
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Select Project");
		super.configureShell(newShell);
	}

	public Object[] getSelectedItem() {
		return selectedItem;
	}
}
