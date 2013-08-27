package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;

import org.eclipse.swt.graphics.Image;

public class OPRoSComponentTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	private static Image ICON_COMPONENT_ELEMENT;
	private static Image IMAGE_COMPONENT_ELEMENT;
	private static Image IMAGE_COMPONENT_ELEMENT_LABEL;
	
	public static Image getWidgetIcon(){
		if(ICON_COMPONENT_ELEMENT==null)
			ICON_COMPONENT_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("ComponentTreeIcon"));
		return ICON_COMPONENT_ELEMENT;
	}
	public static Image getWidgetImage(){
		if(IMAGE_COMPONENT_ELEMENT==null)
			IMAGE_COMPONENT_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("ComponentIcon"));
		return IMAGE_COMPONENT_ELEMENT;
	}
	public static Image getWidgetLabelImage(){
		if(IMAGE_COMPONENT_ELEMENT_LABEL==null)
			IMAGE_COMPONENT_ELEMENT_LABEL = OPRoSUtil.createImage(OPRoSStrings.getString("ComponentLabelImage"));
		return IMAGE_COMPONENT_ELEMENT_LABEL;
	}
	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
		OPRoSComponentElementModel model = (OPRoSComponentElementModel)getModel();
		setWidgetText(model.getComponentName());
		
		setWidgetImage(getWidgetIcon());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSStrings.getString("PROPERTY_COMP_NAME"))) refreshVisuals();
	}
	
}
