package kr.co.ed.opros.ce.guieditor;

import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariableEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariablesEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSBodyElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSComponentElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataInPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataOutPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypeElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypesElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventInPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventOutPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentCPUElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentOSElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeSemanticsElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertiesElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertyElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypeElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesElementPart;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariablesModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypesElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeSemanticsElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertiesElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypesElementModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class OPRoSElementEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart editPart, Object model) {
		AbstractGraphicalEditPart part = null;
		if(model instanceof OPRoSBodyElementModel){
			part = new OPRoSBodyElementPart();
		}else if(model instanceof OPRoSComponentElementModel){
			part = new OPRoSComponentElementPart();
		}else if(model instanceof OPRoSExeSemanticsElementModel){
			part = new OPRoSExeSemanticsElementPart();
		}else if(model instanceof OPRoSServiceProvidedPortElementModel){
			part = new OPRoSServiceProvidedPortElementPart();
		}else if(model instanceof OPRoSServiceRequiredPortElementModel){
			part = new OPRoSServiceRequiredPortElementPart();
		}else if(model instanceof OPRoSDataInPortElementModel){
			part = new OPRoSDataInPortElementPart();
		}else if(model instanceof OPRoSDataOutPortElementModel){
			part = new OPRoSDataOutPortElementPart();
		}else if(model instanceof OPRoSEventInPortElementModel){
			part = new OPRoSEventInPortElementPart();
		}else if(model instanceof OPRoSEventOutPortElementModel){
			part = new OPRoSEventOutPortElementPart();
		}else if(model instanceof OPRoSExeEnvironmentElementModel){
			part = new OPRoSExeEnvironmentElementPart();
		}else if(model instanceof OPRoSExeEnvironmentOSElementModel){
			part = new OPRoSExeEnvironmentOSElementPart();
		}else if(model instanceof OPRoSExeEnvironmentCPUElementModel){
			part = new OPRoSExeEnvironmentCPUElementPart();
		}else if(model instanceof OPRoSPropertiesElementModel){
			part = new OPRoSPropertiesElementPart();
		}else if(model instanceof OPRoSPropertyElementModel){
			part = new OPRoSPropertyElementPart();
		}else if(model instanceof OPRoSDataTypesElementModel){
			part = new OPRoSDataTypesElementPart();
		}else if(model instanceof OPRoSServiceTypesElementModel){
			part = new OPRoSServiceTypesElementPart();
		}else if(model instanceof OPRoSDataTypeElementModel){
			part = new OPRoSDataTypeElementPart();
		}else if(model instanceof OPRoSServiceTypeElementModel){
			part = new OPRoSServiceTypeElementPart();
		}else if(model instanceof MonitoringVariablesModel){
			part = new MonitoringVariablesEditPart();
		}else if(model instanceof MonitoringVariableModel) {
			part = new MonitoringVariableEditPart();
		}
		part.setModel(model);
		return part;
	}

}
