package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.dialog.OPRoSDataTypeInputDialog;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSDataTypeElementCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private OPRoSDataTypeElementModel element;
	
	public int index;
	
	public OPRoSDataTypeElementCreateCommand () {
		
	}
	
	public OPRoSDataTypeElementCreateCommand (int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSComponentElementModel)
			this.parent = (OPRoSComponentElementModel)parent;
	}
	
	public void setElement(Object element){
		if(element instanceof OPRoSDataTypeElementModel)
			this.element = (OPRoSDataTypeElementModel)element;
	}

	@Override
	public boolean canExecute() {
		if(element==null||parent==null||parent.getDataTypesModel()==null)
			return false;
		return true;
	}
	@Override
	public boolean canUndo() {
		if(element==null||parent==null||parent.getDataTypesModel()==null)
			return false;
		return parent.getDataTypesModel().contains(element);
	}
	@Override
	public void execute() {
		OPRoSElementBaseModel model = parent.getDataTypesModel();
		if(model!=null){
			OPRoSDataTypeInputDialog dlg = new OPRoSDataTypeInputDialog(null, parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setDataTypeFileName(dlg.getDataTypeFileName());
				element.setDataTypeDoc(dlg.getDataTypeDoc());
				
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
