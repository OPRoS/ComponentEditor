package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class OPRoSDataInPortTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_DATAIN_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_DATAIN_ELEMENT==null)
			ICON_DATAIN_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("DataInTreeIcon"));
		return ICON_DATAIN_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSDataInPortElementModel model = (OPRoSDataInPortElementModel)getModel();
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
