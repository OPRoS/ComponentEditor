package kr.co.ed.opros.ce.guieditor;

import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.gef.requests.CreationFactory;

public class OPRoSElementCreationFactory implements CreationFactory {

	private Class<?> template;
	
	public OPRoSElementCreationFactory(Class<?> template){
		this.template = template;
	}
	
	@Override
	public Object getNewObject() {
		if(template==null)
			return null;
		if(template==OPRoSServiceProvidedPortElementModel.class){
			return new OPRoSServiceProvidedPortElementModel();
		}else if(template==OPRoSServiceRequiredPortElementModel.class){
			return new OPRoSServiceRequiredPortElementModel();
		}else if(template==OPRoSDataInPortElementModel.class){
			return new OPRoSDataInPortElementModel();
		}else if(template==OPRoSDataOutPortElementModel.class){
			return new OPRoSDataOutPortElementModel();
		}else if(template==OPRoSEventInPortElementModel.class){
			return new OPRoSEventInPortElementModel();
		}else if(template==OPRoSEventOutPortElementModel.class){
			return new OPRoSEventOutPortElementModel();
		}else if(template == OPRoSExeEnvironmentOSElementModel.class){
			return new OPRoSExeEnvironmentOSElementModel();
		}else if(template == OPRoSExeEnvironmentCPUElementModel.class){
			return new OPRoSExeEnvironmentCPUElementModel();
		}else if(template == OPRoSPropertyElementModel.class){
			return new OPRoSPropertyElementModel();
		}else if(template == OPRoSDataTypeElementModel.class){
			return new OPRoSDataTypeElementModel();
		}else if(template == OPRoSServiceTypeElementModel.class){
			return new OPRoSServiceTypeElementModel();
		}else if(template == MonitoringVariableModel.class) {
			return new MonitoringVariableModel();
		}
		return null;
	}

	@Override
	public Object getObjectType() {
		return template;
	}

}
