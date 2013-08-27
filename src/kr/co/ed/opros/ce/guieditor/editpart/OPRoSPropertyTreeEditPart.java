package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSPropertyTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_PROPERTY_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_PROPERTY_ELEMENT==null)
			ICON_PROPERTY_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("PropertyTreeIcon"));
		return ICON_PROPERTY_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSPropertyElementModel model = (OPRoSPropertyElementModel)getModel();
		setWidgetText(model.getName());
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME)) refreshVisuals();
	}
}
