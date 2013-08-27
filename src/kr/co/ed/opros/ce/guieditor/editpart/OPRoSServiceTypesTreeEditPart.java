package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.swt.graphics.Image;

public class OPRoSServiceTypesTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_SERVICETYPES_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_SERVICETYPES_ELEMENT==null)
			ICON_SERVICETYPES_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("ServiceTypesTreeIcon"));
		return ICON_SERVICETYPES_ELEMENT;
	}
	
	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	
	@Override
	protected void refreshVisuals() {
		setWidgetText(OPRoSStrings.getString("ServiceTypesTreeModelLabel"));
		setWidgetImage(getWidgetImage());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
	
}
