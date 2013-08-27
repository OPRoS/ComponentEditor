package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.dialog.OPRoSExeEnvOSInputDialog;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSExeEnvironmentOSElementCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private OPRoSExeEnvironmentOSElementModel element;
	
	public int index;
	
	public OPRoSExeEnvironmentOSElementCreateCommand () {
		
	}
	
	public OPRoSExeEnvironmentOSElementCreateCommand (int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSComponentElementModel)
			this.parent = (OPRoSComponentElementModel)parent;
	}
	public void setElement(Object element){
		if(element instanceof OPRoSExeEnvironmentOSElementModel)
			this.element = (OPRoSExeEnvironmentOSElementModel)element;
	}

	@Override
	public boolean canExecute() {
		if(element==null||parent==null||parent.getExeEnvironmentModel()==null)
			return false;
		return true;
	}
	@Override
	public boolean canUndo() {
		if(element==null||parent==null||parent.getExeEnvironmentModel()==null)
			return false;
		return parent.getExeEnvironmentModel().contains(element);
	}
	@Override
	public void execute() {
		OPRoSElementBaseModel model = parent.getExeEnvironmentModel();
		if(model!=null){
			OPRoSExeEnvOSInputDialog dlg = new OPRoSExeEnvOSInputDialog(null);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setOSName(dlg.getOSName());
				element.setOSVersion(dlg.getOSVersion());
				
				if(index < 0) {
					model.addChild(element);
				} else {
					model.addChild(element, index);
				}
			}
		}
	}
	@Override
	public void undo() {
		parent.getExeEnvironmentModel().removeChild(element);
	}
	
}
