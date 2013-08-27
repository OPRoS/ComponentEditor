package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.gef.commands.Command;

public class OPRoSDeleteCommand extends Command {
	public OPRoSElementBaseModel model;
	public OPRoSElementBaseModel parentModel;
	public boolean isServiceTypeChange = false;
	
	
	
	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}

	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return super.canUndo();
	}

	public void execute(){
		parentModel.removeChild(model);
		if(model instanceof OPRoSPortElementBaseModel){
			OPRoSBodyElementModel.getInstance().addDelPortList((OPRoSPortElementBaseModel)model);
			
			String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_DELETE);
			if(parentModel != null) {
				str = str + "&&("+((OPRoSBodyElementModel)parentModel).getComponentModel().getComponentName()+")"+((OPRoSPortElementBaseModel)model).getName();
				OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_DELETE, str);
			}
		} else if(model instanceof OPRoSServiceTypeElementModel) {
			OPRoSBodyElementModel body = (OPRoSBodyElementModel)parentModel.getParent().getParent();
			isServiceTypeChange = body.removeChangeServiceType((OPRoSServiceTypeElementModel)model);
		}
	}
	
	public void setModel(Object model){
		this.model = (OPRoSElementBaseModel) model;
	}
	
	public void setParentModel(Object parentModel){
		this.parentModel = (OPRoSElementBaseModel) parentModel;
	}
	
	public void undo(){
		parentModel.addChild(model);
		if(model instanceof OPRoSPortElementBaseModel){
			boolean ret = OPRoSBodyElementModel.getInstance().removeDelPortList((OPRoSPortElementBaseModel)model);
			if(!ret)
				OPRoSBodyElementModel.getInstance().addAddPortList((OPRoSPortElementBaseModel)model);
			
			String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_DELETE);
			String[] strs = str.split("&&");
			if(strs.length > 1) {
				
				String temp = "";
				for(int i=0; i<strs.length-1; i++) {
					if(strs[i] != null && !strs[i].equals(""))
						temp = temp + "&&" +strs[i];
				}
				OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_DELETE, temp);
			}
		} else if(model instanceof OPRoSServiceTypeElementModel) {
			if(isServiceTypeChange) {
				OPRoSBodyElementModel body = (OPRoSBodyElementModel)parentModel.getParent().getParent();
				body.addChangeServiceType((OPRoSServiceTypeElementModel)model);
			}
		}
	}
}
