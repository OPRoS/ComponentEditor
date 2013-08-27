package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSServiceRequiredPortTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	private static Image ICON_SERVICE_REQUIRED_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_SERVICE_REQUIRED_ELEMENT==null)
			ICON_SERVICE_REQUIRED_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("ServiceRequiredTreeIcon"));
		return ICON_SERVICE_REQUIRED_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSServiceRequiredPortElementModel model = (OPRoSServiceRequiredPortElementModel)getModel();
		setWidgetText(model.getName());
		
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME)) refreshVisuals();
	}
}
