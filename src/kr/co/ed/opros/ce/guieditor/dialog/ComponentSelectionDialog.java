package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.List;

import kr.co.ed.opros.ce.provider.ComponentListLabelProvider;
import kr.co.ed.opros.ce.provider.ProjectListContentProvider;
import kr.co.ed.opros.ce.provider.TreeLabelProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ComponentSelectionDialog extends Dialog {
	
	public TableViewer fTableViewer;
	
	public Object selectedItem;	
	
	public List<String> input;
	
	public String labelString;

	public ComponentSelectionDialog(Shell shell, List<String> input, String desc) {
		super(shell);
		this.input = input;
		this.labelString = desc;
	}

	@Override
	protected Control createDialogArea(Composite parent) {		
		GridLayout layout = new GridLayout(1, false);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		parent.setLayout(layout);
		parent.setLayoutData(gd);	
		
		
		Label label = new Label(parent, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		label.setLayoutData(gd);
		label.setText(labelString);
		
		fTableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = 250;
		gd.heightHint = 300;
		fTableViewer.getTable().setLayoutData(gd);
		fTableViewer.setContentProvider(new ProjectListContentProvider());
		fTableViewer.setLabelProvider(new ComponentListLabelProvider());
		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				okPressed();			
			}
		});
		
		fTableViewer.setInput(input);		
		return super.createDialogArea(parent);
	}

	@Override
	protected void okPressed() {
		ISelection selection = fTableViewer.getSelection();
		if(selection instanceof IStructuredSelection) {
			selectedItem = ((IStructuredSelection)selection).getFirstElement();
		}
		
		if(selectedItem == null) {
			MessageDialog.openError(getShell(), "Error", "Select a Component.");
			return;
		}
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Select Component");
		super.configureShell(newShell);
	}

	public Object getSelectedItem() {
		return selectedItem;
	}
}
