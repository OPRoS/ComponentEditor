package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.dialog.MonitorVariableInputDialog;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class MonitoringVariableCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private MonitoringVariableModel element;
	
	public int index;
	
	public MonitoringVariableCreateCommand () {
		
	}
	
	public MonitoringVariableCreateCommand (int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSComponentElementModel)
			this.parent = (OPRoSComponentElementModel)parent;
	}
	
	public void setElement(Object element){
		if(element instanceof MonitoringVariableModel)
			this.element = (MonitoringVariableModel)element;
	}

	@Override
	public boolean canExecute() {
		if(element==null||parent==null)
			return false;
		return true;
	}
	@Override
	public boolean canUndo() {
		if(element==null||parent==null)
			return false;
		return parent.getDataTypesModel().contains(element);
	}
	@Override
	public void execute() {
		OPRoSElementBaseModel model = parent.getMonitoringVariablesModel();
		if(model!=null){
			MonitorVariableInputDialog dlg = new MonitorVariableInputDialog(null, parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setName(dlg.getName());
				element.setType(dlg.getType());
				
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
		parent.getDataTypesModel().removeChild(element);
	}
}
