package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSExeEnvironmentCPUElementCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private OPRoSExeEnvironmentCPUElementModel element;
	
	public int index;
	
	public OPRoSExeEnvironmentCPUElementCreateCommand() {
		
	}
	public OPRoSExeEnvironmentCPUElementCreateCommand(int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSComponentElementModel)
			this.parent = (OPRoSComponentElementModel)parent;
	}
	public void setElement(Object element){
		if(element instanceof OPRoSExeEnvironmentCPUElementModel)
			this.element = (OPRoSExeEnvironmentCPUElementModel)element;
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
			InputDialog dlg=new InputDialog(null,
					OPRoSStrings.getString("CPUInputDlgTitle"),
					OPRoSStrings.getString("CPUInputDlgLabel"),
					OPRoSStrings.getString("CPUInputDlgDefaultValue"),null);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setCPUName(dlg.getValue());
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
