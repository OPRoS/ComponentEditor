package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Image;

public class OPRoSServiceTypeTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_SERVICE_TYPE_ELEMENT;
	
	public static Image getWidgetImage(){
		if(ICON_SERVICE_TYPE_ELEMENT==null)
			ICON_SERVICE_TYPE_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("ServiceTypeTreeIcon"));
		return ICON_SERVICE_TYPE_ELEMENT;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}
	
	@Override
	protected void refreshVisuals() {
		OPRoSServiceTypeElementModel model = (OPRoSServiceTypeElementModel)getModel();
		setWidgetText(model.getServiceTypeFileName());
		setWidgetImage(getWidgetImage());
		this.getWidget().setData(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSServiceTypeElementModel.PROPERTY_SERVICE_TYPE_FILENAME)) refreshVisuals();
	}	
	
}
