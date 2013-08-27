package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.dialog.MonitorVariableInputDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSDataPortDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSDataTypeInputDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSEventPortDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSExeEnvOSInputDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSPropertyInputDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSServicePortDialog;
import kr.co.ed.opros.ce.guieditor.dialog.OPRoSServiceTypeInputDialog;
import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariableEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariableTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataInPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataInPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataOutPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataOutPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypeElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypeTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventInPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventInPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventOutPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventOutPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentOSElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentOSTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertyElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertyTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypeElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypeTreeEditPart;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.dialogs.InputDialog;

public class ElementChangeDialogCall implements Runnable {
	
	public EditPart editpart;
	
	public ElementChangeDialogCall(EditPart editpart) {
		this.editpart = editpart;
	}
	
	public void setEditpart(EditPart editpart) {
		this.editpart = editpart;
	}

	@Override
	public void run() {
		if(editpart == null)
			return;
			
		if(editpart instanceof OPRoSServiceProvidedPortElementPart){
			OPRoSServiceProvidedPortElementModel model = (OPRoSServiceProvidedPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSServicePortDialog dlg = 
				new OPRoSServicePortDialog(null,true,(OPRoSElementBaseModel) model,	parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSServicePortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSServiceProvidedPortElementModel newModel = new OPRoSServiceProvidedPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setReference(dlg.getPortRefer());
				newModel.setType(dlg.getPortType());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setUsage(OPRoSStrings.getString("ServicePortUsage0"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);		
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSServiceRequiredPortElementPart){
			OPRoSServiceRequiredPortElementModel model = (OPRoSServiceRequiredPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSServicePortDialog dlg = 
				new OPRoSServicePortDialog(null,false,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSServicePortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSServiceRequiredPortElementModel newModel = new OPRoSServiceRequiredPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setReference(dlg.getPortRefer());
				newModel.setType(dlg.getPortType());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setUsage(OPRoSStrings.getString("ServicePortUsage1"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSDataInPortElementPart){
			OPRoSDataInPortElementModel model = (OPRoSDataInPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSDataPortDialog dlg = 
				new OPRoSDataPortDialog(null,true,(OPRoSElementBaseModel) model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSDataPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSDataInPortElementModel newModel = new OPRoSDataInPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setReference(dlg.getPortRefer());
				newModel.setUsage(OPRoSStrings.getString("DataPortUsage0"));
				newModel.setQueueingPolicy(dlg.getPortPolicy());
				newModel.setQueueSize(dlg.getPortQueueSize());
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSDataOutPortElementPart){
			OPRoSDataOutPortElementModel model = (OPRoSDataOutPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSDataPortDialog dlg = 
				new OPRoSDataPortDialog(null,false,(OPRoSElementBaseModel) model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSDataPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSDataOutPortElementModel newModel = new OPRoSDataOutPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setReference(dlg.getPortRefer());
				newModel.setUsage(OPRoSStrings.getString("DataPortUsage1"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSEventInPortElementPart){
			OPRoSEventInPortElementModel model = (OPRoSEventInPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSEventPortDialog dlg = 
				new OPRoSEventPortDialog(null,true,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSEventPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSEventInPortElementModel newModel = new OPRoSEventInPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setUsage(OPRoSStrings.getString("EventPortUsage0"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSEventOutPortElementPart){
			OPRoSEventOutPortElementModel model = (OPRoSEventOutPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSEventPortDialog dlg = 
				new OPRoSEventPortDialog(null,false,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSEventPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);				
				OPRoSEventOutPortElementModel newModel = new OPRoSEventOutPortElementModel();
				parent.addChild(newModel);				
				newModel.setName(dlg.getPortName());				
				newModel.setDescription(dlg.getPortDescript());				
				newModel.setType(dlg.getPortType());				
				newModel.setUsage(OPRoSStrings.getString("EventPortUsage1"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				
				parent.getOPRoSEditor().setDirty(true);
			}	
		} else if(editpart instanceof OPRoSDataTypeElementPart) {
			OPRoSDataTypeElementPart treeEditPart = (OPRoSDataTypeElementPart)editpart;
			OPRoSDataTypeElementModel model = (OPRoSDataTypeElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSDataTypeInputDialog dlg = new OPRoSDataTypeInputDialog(null, model, comp);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				model.setDataTypeFileName(dlg.getDataTypeFileName());
				model.setDataTypeDoc(dlg.getDataTypeDoc());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSServiceTypeElementPart) {
			OPRoSServiceTypeElementPart treeEditPart = (OPRoSServiceTypeElementPart)editpart;
			OPRoSServiceTypeElementModel model = (OPRoSServiceTypeElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSServiceTypeInputDialog dlg = new OPRoSServiceTypeInputDialog(null,model,comp);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				model.setServiceTypeFileName(dlg.getServiceTypeFileName());
				model.changeServiceTypeDoc(dlg.getServiceTypeDoc());
				parent.addChangeServiceType(model);
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSPropertyElementPart) {
			OPRoSPropertyElementPart treeEditPart = (OPRoSPropertyElementPart)editpart;
			OPRoSPropertyElementModel model = (OPRoSPropertyElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSPropertyInputDialog dialog = new OPRoSPropertyInputDialog(null, model,comp);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.setName((dialog.getName()));
				model.setType(dialog.getType());
				model.setDefaultValue(dialog.getDefaultValue());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSExeEnvironmentOSElementPart) {
			OPRoSExeEnvironmentOSElementPart treeEditPart = (OPRoSExeEnvironmentOSElementPart)editpart;
			OPRoSExeEnvironmentOSElementModel model = (OPRoSExeEnvironmentOSElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSExeEnvOSInputDialog dialog = new OPRoSExeEnvOSInputDialog(null, model);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.setOSName(dialog.getOSName());
				model.setOSVersion(dialog.getOSVersion());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof MonitoringVariableEditPart) {
			MonitoringVariableEditPart treeEditPart = (MonitoringVariableEditPart)editpart;
			MonitoringVariableModel model = (MonitoringVariableModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			MonitorVariableInputDialog dialog = new MonitorVariableInputDialog(null, model, comp);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.changeName(dialog.getName());
				model.changeType(dialog.getType());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		}
		
		
		
		
		else if(editpart instanceof OPRoSServiceProvidedPortTreeEditPart){
			OPRoSServiceProvidedPortElementModel model = (OPRoSServiceProvidedPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSServicePortDialog dlg = 
				new OPRoSServicePortDialog(null,true,(OPRoSElementBaseModel) model,	parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSServicePortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSServiceProvidedPortElementModel newModel = new OPRoSServiceProvidedPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setReference(dlg.getPortRefer());
				newModel.setType(dlg.getPortType());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setUsage(OPRoSStrings.getString("ServicePortUsage0"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSServiceRequiredPortTreeEditPart){
			OPRoSServiceRequiredPortElementModel model = (OPRoSServiceRequiredPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSServicePortDialog dlg = 
				new OPRoSServicePortDialog(null,false,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSServicePortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSServiceRequiredPortElementModel newModel = new OPRoSServiceRequiredPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setReference(dlg.getPortRefer());
				newModel.setType(dlg.getPortType());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setUsage(OPRoSStrings.getString("ServicePortUsage1"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSDataInPortTreeEditPart){
			OPRoSDataInPortElementModel model = (OPRoSDataInPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSDataPortDialog dlg = 
				new OPRoSDataPortDialog(null,true,(OPRoSElementBaseModel) model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSDataPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSDataInPortElementModel newModel = new OPRoSDataInPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setReference(dlg.getPortRefer());
				newModel.setUsage(OPRoSStrings.getString("DataPortUsage0"));
				newModel.setQueueingPolicy(dlg.getPortPolicy());
				newModel.setQueueSize(dlg.getPortQueueSize());
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSDataOutPortTreeEditPart){
			OPRoSDataOutPortElementModel model = (OPRoSDataOutPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSDataPortDialog dlg = 
				new OPRoSDataPortDialog(null,false,(OPRoSElementBaseModel) model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSDataPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSDataOutPortElementModel newModel = new OPRoSDataOutPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setReference(dlg.getPortRefer());
				newModel.setUsage(OPRoSStrings.getString("DataPortUsage1"));
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSEventInPortTreeEditPart){
			OPRoSEventInPortElementModel model = (OPRoSEventInPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSEventPortDialog dlg = 
				new OPRoSEventPortDialog(null,true,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSEventPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSEventInPortElementModel newModel = new OPRoSEventInPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setUsage(OPRoSStrings.getString("EventPortUsage0"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}
		}else if(editpart instanceof OPRoSEventOutPortTreeEditPart){
			OPRoSEventOutPortElementModel model = (OPRoSEventOutPortElementModel)editpart.getModel();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel) model.getParent();
			OPRoSEventPortDialog dlg = 
				new OPRoSEventPortDialog(null,false,(OPRoSElementBaseModel)model,parent);
			dlg.open();
			String oldName = model.getName();
			
			if(dlg.getReturnCode()==OPRoSEventPortDialog.OK){
				parent.removeChild(model);
				parent.addDelPortList(model);
				OPRoSEventOutPortElementModel newModel = new OPRoSEventOutPortElementModel();
				parent.addChild(newModel);
				newModel.setName(dlg.getPortName());
				newModel.setDescription(dlg.getPortDescript());
				newModel.setType(dlg.getPortType());
				newModel.setUsage(OPRoSStrings.getString("EventPortUsage0"));
				String usingDataType=dlg.getUsingDataTypeFileName();
				if(!usingDataType.isEmpty()){
					parent.compEle.addDataTypeReference(usingDataType);
				}
				newModel.setLayout(model.getLayout());
				parent.addAddPortList(newModel);
				
				if(!oldName.equals(dlg.getPortName())) {
					String str = OPRoSActivator.getPreferenceValue(OPRoSActivator.KEY_PORT_RENAME);
					if(str != null && parent != null) {
						str = str + "&&("+((OPRoSBodyElementModel)parent).getComponentModel().getComponentName()+")"+oldName+":"+dlg.getPortName();
						OPRoSActivator.setPreferenceValue(OPRoSActivator.KEY_PORT_RENAME, str);
					}
				}
				parent.getOPRoSEditor().setDirty(true);
			}	
		}
		
		else if(editpart instanceof OPRoSDataTypeTreeEditPart) {
			OPRoSDataTypeTreeEditPart treeEditPart = (OPRoSDataTypeTreeEditPart)editpart;
			OPRoSDataTypeElementModel model = (OPRoSDataTypeElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSDataTypeInputDialog dlg = new OPRoSDataTypeInputDialog(null,model,comp);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				model.setDataTypeFileName(dlg.getDataTypeFileName());
				model.setDataTypeDoc(dlg.getDataTypeDoc());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSServiceTypeTreeEditPart) {
			OPRoSServiceTypeTreeEditPart treeEditPart = (OPRoSServiceTypeTreeEditPart)editpart;
			OPRoSServiceTypeElementModel model = (OPRoSServiceTypeElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSServiceTypeInputDialog dlg = new OPRoSServiceTypeInputDialog(null,model,comp);
			dlg.open();
			if(dlg.getReturnCode()==InputDialog.OK){
				model.setServiceTypeFileName(dlg.getServiceTypeFileName());
				model.changeServiceTypeDoc(dlg.getServiceTypeDoc());
				parent.addChangeServiceType(model);
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSPropertyTreeEditPart) {
			OPRoSPropertyTreeEditPart treeEditPart = (OPRoSPropertyTreeEditPart)editpart;
			OPRoSPropertyElementModel model = (OPRoSPropertyElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSPropertyInputDialog dialog = new OPRoSPropertyInputDialog(null, model,comp);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.setName((dialog.getName()));
				model.setType(dialog.getType());
				model.setDefaultValue(dialog.getDefaultValue());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		} else if(editpart instanceof OPRoSExeEnvironmentOSTreeEditPart) {
			OPRoSExeEnvironmentOSTreeEditPart treeEditPart = (OPRoSExeEnvironmentOSTreeEditPart)editpart;
			OPRoSExeEnvironmentOSElementModel model = (OPRoSExeEnvironmentOSElementModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			OPRoSExeEnvOSInputDialog dialog = new OPRoSExeEnvOSInputDialog(null, model);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.setOSName(dialog.getOSName());
				model.setOSVersion(dialog.getOSVersion());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		}  else if(editpart instanceof MonitoringVariableTreeEditPart) {
			MonitoringVariableTreeEditPart treeEditPart = (MonitoringVariableTreeEditPart)editpart;
			MonitoringVariableModel model = (MonitoringVariableModel)treeEditPart.getModel();
			OPRoSComponentElementModel comp = (OPRoSComponentElementModel)model.getParent().getParent();
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)comp.getParent();
			MonitorVariableInputDialog dialog = new MonitorVariableInputDialog(null, model, comp);
			dialog.open();
			if(dialog.getReturnCode()==InputDialog.OK){
				model.changeName(dialog.getName());
				model.changeType(dialog.getType());
				parent.getOPRoSEditor().getCommandStack().markSaveLocation();
			}
		}
	}
}
