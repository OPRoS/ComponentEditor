package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSDataPortDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSEventPortDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSServicePortDialog;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;

public class OPRoSPortCreateCommand extends Command {
	public OPRoSBodyElementModel parent;
	public OPRoSPortElementBaseModel element;
	public Rectangle layout;
	
	public void setParent(Object parent){
		if(parent instanceof OPRoSBodyElementModel){
			this.parent = (OPRoSBodyElementModel) parent;
		}
	}
	
	public void setElement(Object element){
		if(element instanceof OPRoSPortElementBaseModel)
			this.element = (OPRoSPortElementBaseModel) element;
	}
	
	public void setLayout(Rectangle rect){
		if(element==null)
			return;
		layout=rect;
	}
	
	@Override
	public boolean canExecute() {
		if(element==null||parent==null)
			return false;
		return true;
	}

	@Override
	public boolean canUndo() {
		if(element == null|| parent == null)
			return false;
		return parent.contains(element);
	}

	@Override
	public void execute() {
		if(element instanceof OPRoSServiceProvidedPortElementModel){
			OPRoSServicePortDialog dlg = new OPRoSServicePortDialog(null,true,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSServiceProvidedPortElementModel)element).setName(dlg.getPortName());
				((OPRoSServiceProvidedPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSServiceProvidedPortElementModel)element).setType(dlg.getPortType());
				((OPRoSServiceProvidedPortElementModel)element).setReference(dlg.getPortRefer());
				((OPRoSServiceProvidedPortElementModel)element).setUsage(OPRoSStrings.getString("ServicePortUsage0"));
				//변경된 내용의 ServiceType을 수정해야 한다.
			}else{
				return;
			}
		}else if(element instanceof OPRoSServiceRequiredPortElementModel){
			OPRoSServicePortDialog dlg = new OPRoSServicePortDialog(null,false,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSServiceRequiredPortElementModel)element).setName(dlg.getPortName());
				((OPRoSServiceRequiredPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSServiceRequiredPortElementModel)element).setType(dlg.getPortType());
				((OPRoSServiceRequiredPortElementModel)element).setReference(dlg.getPortRefer());
				((OPRoSServiceRequiredPortElementModel)element).setUsage(OPRoSStrings.getString("ServicePortUsage1"));
			}else{
				return;
			}
		}else if(element instanceof OPRoSDataInPortElementModel){
			OPRoSDataPortDialog dlg = new OPRoSDataPortDialog(null,true,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSDataInPortElementModel)element).setName(dlg.getPortName());
				((OPRoSDataInPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSDataInPortElementModel)element).setType(dlg.getPortType());
				((OPRoSDataInPortElementModel)element).setReference(dlg.getPortRefer());
				((OPRoSDataInPortElementModel)element).setUsage(OPRoSStrings.getString("DataPortUsage0"));
				((OPRoSDataInPortElementModel)element).setQueueingPolicy(dlg.getPortPolicy());
				((OPRoSDataInPortElementModel)element).setQueueSize(dlg.getPortQueueSize());
			}else{
				return;
			}
		}else if(element instanceof OPRoSDataOutPortElementModel){
			OPRoSDataPortDialog dlg = new OPRoSDataPortDialog(null,false,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSDataOutPortElementModel)element).setName(dlg.getPortName());
				((OPRoSDataOutPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSDataOutPortElementModel)element).setType(dlg.getPortType());
				((OPRoSDataOutPortElementModel)element).setReference(dlg.getPortRefer());
				((OPRoSDataOutPortElementModel)element).setUsage(OPRoSStrings.getString("DataPortUsage1"));
			}else{
				return;
			}
		}else if(element instanceof OPRoSEventInPortElementModel){
			OPRoSEventPortDialog dlg = new OPRoSEventPortDialog(null,true,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSEventInPortElementModel)element).setName(dlg.getPortName());
				((OPRoSEventInPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSEventInPortElementModel)element).setType(dlg.getPortType());
				((OPRoSEventInPortElementModel)element).setUsage(OPRoSStrings.getString("EventPortUsage0"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
			}else{
				return;
			}
		}else if(element instanceof OPRoSEventOutPortElementModel){
			OPRoSEventPortDialog dlg = new OPRoSEventPortDialog(null,false,null,parent);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				((OPRoSEventOutPortElementModel)element).setName(dlg.getPortName());
				((OPRoSEventOutPortElementModel)element).setDescription(dlg.getPortDescript());
				((OPRoSEventOutPortElementModel)element).setType(dlg.getPortType());
				((OPRoSEventOutPortElementModel)element).setUsage(OPRoSStrings.getString("EventPortUsage1"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
			}else{
				return;
			}
		}
		parent.addChild(element);
		element.setLayout(layout);
		if(element.getDirection()==1){
			element.setLayout(layout);
		}
		parent.addAddPortList(element);
	}

	@Override
	public void undo() {
		parent.removeChild(element);
		boolean ret = parent.removeAddPortList(element);
		if(!ret)
			parent.addDelPortList(element);
	}
}
