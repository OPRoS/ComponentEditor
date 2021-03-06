package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSEventInPortTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_EVENTIN_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_EVENTIN_ELEMENT==null)
			ICON_EVENTIN_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("EventInTreeIcon"));
		return ICON_EVENTIN_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSEventInPortElementModel model = (OPRoSEventInPortElementModel)getModel();
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
