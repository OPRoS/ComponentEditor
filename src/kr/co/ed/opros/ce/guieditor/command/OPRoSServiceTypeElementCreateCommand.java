package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.dialog.OPRoSServiceTypeInputDialog;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSServiceTypeElementCreateCommand extends Command {
	private OPRoSComponentElementModel parent;
	private OPRoSServiceTypeElementModel element;
	
	public int index;
	
	public OPRoSServiceTypeElementCreateCommand() {
		
	}
	
	public OPRoSServiceTypeElementCreateCommand(int index) {
		this.index = index;
	}
	
	public void setParent(Object parent){		
		this.parent = (OPRoSComponentElementModel)parent;
	}
	
	public void setElement(Object element){
		if(element instanceof OPRoSServiceTypeElementModel)
			this.element = (OPRoSServiceTypeElementModel)element;
	}
//	public void setLayout(Rectangle rect){
//		if(element == null)
//			return;
//		element.setLayout(rect);
//	}
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
		return getServiceTypesModel(parent).contains(element);
	}
	@Override
	public void execute() {
		OPRoSElementBaseModel model = getServiceTypesModel(parent);
		
		if(model!=null){
			OPRoSServiceTypeInputDialog dlg = new OPRoSServiceTypeInputDialog(null, parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				element.setServiceTypeFileName(dlg.getServiceTypeFileName());
				element.setServiceTypeDoc(dlg.getServiceTypeDoc());
				
				if(index < 0) {
					model.addChild(element);
				} else {
					model.addChild(element, index);
				}
				
			}
		}
	}
	
	public OPRoSElementBaseModel getServiceTypesModel(OPRoSElementBaseModel parent) {
		if(parent instanceof OPRoSComponentElementModel) 
			return ((OPRoSComponentElementModel)parent).getServiceTypesModel();
		return parent;
	}
	@Override
	public void undo() {
		getServiceTypesModel(parent).removeChild(element);
	}
}
