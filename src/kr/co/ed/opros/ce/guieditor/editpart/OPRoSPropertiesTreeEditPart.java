package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.swt.graphics.Image;

public class OPRoSPropertiesTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_PROPERTIES_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_PROPERTIES_ELEMENT==null)
			ICON_PROPERTIES_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("PropertiesTreeIcon"));
		return ICON_PROPERTIES_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
//		OPRoSExeEnvironmentElementModel model = (OPRoSExeEnvironmentElementModel)getModel();
		setWidgetText(OPRoSStrings.getString("PropertiesTreeModelLabel"));
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
}
