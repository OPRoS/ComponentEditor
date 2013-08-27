package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class OPRoSFormPage extends FormPage {	

	public OPRoSFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		/*
		IToolBarManager manager = form.getToolBarManager();

		final String helpContextID = getHelpResource();
		if (helpContextID != null) {
			Action helpAction = new Action("help") { //$NON-NLS-1$
				public void run() {
					//PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextID);
				}
			};
			helpAction.setToolTipText("aaaaa");
			helpAction.setImageDescriptor(OPRoSActivator.getImageDescriptor(IIconConstants.ICON_DEPENDENCIES_EDITOR));
			manager.add(helpAction);
		}
		//check to see if our form parts are contributing actions
		IFormPart[] parts = managedForm.getParts();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] instanceof IAdaptable) {
				IAdaptable adapter = (IAdaptable) parts[i];
				IAction[] actions = (IAction[]) adapter.getAdapter(IAction[].class);
				if (actions != null) {
					for (int j = 0; j < actions.length; j++) {
						form.getToolBarManager().add(actions[j]);
					}
				}
			}
		}
		;*/
		form.updateToolBar();
	}
	
	protected String getHelpResource() {
		return "Help";
	}
}
