package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSExeEnvironmentOSTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	private static Image ICON_ENV_INFO_OS_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_ENV_INFO_OS_ELEMENT==null)
			ICON_ENV_INFO_OS_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("OSTreeIcon"));
		return ICON_ENV_INFO_OS_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSExeEnvironmentOSElementModel model = (OPRoSExeEnvironmentOSElementModel)getModel();
		setWidgetText(model.getOSName()+OPRoSStrings.getString("OSTreeLabelConnector")+model.getOSVersion());
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME)) refreshVisuals();
		if(evt.getPropertyName().equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_VERSION)) refreshVisuals();
	}
}
