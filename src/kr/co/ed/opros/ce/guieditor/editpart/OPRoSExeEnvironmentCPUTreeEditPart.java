package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSExeEnvironmentCPUTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	private static Image ICON_ENV_INFO_CPU_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_ENV_INFO_CPU_ELEMENT==null)
			ICON_ENV_INFO_CPU_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("CPUTreeIcon"));
		return ICON_ENV_INFO_CPU_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSExeEnvironmentCPUElementModel model = (OPRoSExeEnvironmentCPUElementModel)getModel();
		setWidgetText(model.getCPUName());
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSExeEnvironmentCPUElementModel.PROPERTY_CPU_NAME)) refreshVisuals();
	}
}
