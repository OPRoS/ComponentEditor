package kr.co.ed.opros.ce.ui.actions;

import kr.co.ed.opros.ce.wizards.export.UserComponentRegisterWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * ����� ������Ʈ�� ���ø����� ����ϴ� �׼�
 * ��� �����縦 ȣ���Ѵ�.
 * @author hclee
 *
 */
public class UserComponentRegisterActionDelegate implements
		IObjectActionDelegate {
	
	public IWorkbenchPart targetPart;
	
	public IStructuredSelection selection;

	@Override
	public void run(IAction action) {
		UserComponentRegisterWizard wizard = new UserComponentRegisterWizard();
		wizard.init(targetPart.getSite().getWorkbenchWindow().getWorkbench(), selection);
		WizardDialog dialog = new WizardDialog(targetPart.getSite().getShell(), wizard);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IStructuredSelection)
			this.selection = (IStructuredSelection)selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

}
