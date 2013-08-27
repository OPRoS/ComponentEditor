package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.dialog.OPRoSPropertyInputDialog;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSPropertyElementCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private OPRoSPropertyElementModel element;
	
	public int index;
	
	public OPRoSPropertyElementCreateCommand () {
		
	}
	
	public OPRoSPropertyElementCreateCommand (int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSComponentElementModel)
			this.parent = (OPRoSComponentElementModel)parent;
	}
	public void setElement(Object element){
		if(element instanceof OPRoSPropertyElementModel)
			this.element = (OPRoSPropertyElementModel)element;
	}
//	public void setLayout(Rectangle rect){
//		if(element == null)
//			return;
//		element.setLayout(rect);
//	}
	@Override
	public boolean canExecute() {
		if(element==null||parent==null||parent.getPropertiesModel()==null)
			return false;
		return true;
	}
	@Override
	public boolean canUndo() {
		if(element==null||parent==null||parent.getPropertiesModel()==null)
			return false;
		return parent.getPropertiesModel().contains(element);
	}
	@Override
	public void execute() {
		OPRoSElementBaseModel model = parent.getPropertiesModel();
		if(model!=null){
			OPRoSPropertyInputDialog dlg = new OPRoSPropertyInputDialog(null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setName(dlg.getName());
				element.setType(dlg.getType());
				element.setDefaultValue(dlg.getDefaultValue());
				String dataTypeFileName=dlg.getUsingDataTypeFileName();
				if(dataTypeFileName!=null&&!dataTypeFileName.isEmpty()){
					if(!dataTypeFileName.isEmpty()){
						parent.addDataTypeReference(dataTypeFileName);
					}
				}
				
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
		parent.getPropertiesModel().removeChild(element);
	}
}
