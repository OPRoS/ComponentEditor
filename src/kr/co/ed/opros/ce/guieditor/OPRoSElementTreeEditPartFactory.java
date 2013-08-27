package kr.co.ed.opros.ce.guieditor;

import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariableTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariablesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSBodyElementTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSComponentTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataInPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataOutPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypeTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventInPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSEventOutPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentCPUTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentOSTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeSemanticsTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertiesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertyTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypeTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesTreeEditPart;
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

public class OPRoSElementTreeEditPartFactory extends
		OPRoSElementEditPartFactory {

	@Override
	public EditPart createEditPart(EditPart editPart, Object model) {
		EditPart part = null;
		if(model instanceof OPRoSBodyElementModel)
			part = new OPRoSBodyElementTreeEditPart();
		
		else if(model instanceof OPRoSComponentElementModel){
			part = new OPRoSComponentTreeEditPart();
		}
		else if(model instanceof OPRoSExeSemanticsElementModel){
			part = new OPRoSExeSemanticsTreeEditPart();
		}
		else if(model instanceof OPRoSServiceProvidedPortElementModel){
			part = new OPRoSServiceProvidedPortTreeEditPart();
		}
		else if(model instanceof OPRoSServiceRequiredPortElementModel){
			part = new OPRoSServiceRequiredPortTreeEditPart();
		}
		else if(model instanceof OPRoSDataInPortElementModel){
			part = new OPRoSDataInPortTreeEditPart();
		}
		else if(model instanceof OPRoSDataOutPortElementModel){
			part = new OPRoSDataOutPortTreeEditPart();
		}
		else if(model instanceof OPRoSEventInPortElementModel){
			part = new OPRoSEventInPortTreeEditPart();
		}
		else if(model instanceof OPRoSEventOutPortElementModel){
			part = new OPRoSEventOutPortTreeEditPart();
		}
		else if(model instanceof OPRoSExeEnvironmentElementModel){
			part = new OPRoSExeEnvironmentTreeEditPart();
		}
		else if(model instanceof OPRoSExeEnvironmentOSElementModel){
			part = new OPRoSExeEnvironmentOSTreeEditPart();
		}
		else if(model instanceof OPRoSExeEnvironmentCPUElementModel){
			part = new OPRoSExeEnvironmentCPUTreeEditPart();
		}
		else if(model instanceof OPRoSPropertiesElementModel){
			part = new OPRoSPropertiesTreeEditPart();
		}
		else if(model instanceof OPRoSPropertyElementModel){
			part = new OPRoSPropertyTreeEditPart();
		}
		else if(model instanceof OPRoSDataTypesElementModel){
			part = new OPRoSDataTypesTreeEditPart();
		}
		else if(model instanceof OPRoSServiceTypesElementModel){
			part = new OPRoSServiceTypesTreeEditPart();
		}
		else if(model instanceof OPRoSDataTypeElementModel){
			part = new OPRoSDataTypeTreeEditPart();
		}
		else if(model instanceof OPRoSServiceTypeElementModel){
			part = new OPRoSServiceTypeTreeEditPart();
		}
		else if(model instanceof MonitoringVariablesModel) {
			part = new MonitoringVariablesTreeEditPart();
		}
		else if(model instanceof MonitoringVariableModel) {
			part = new MonitoringVariableTreeEditPart();
		}
		
		if(part!=null)
			part.setModel(model);
		
		return part;
	}

}
