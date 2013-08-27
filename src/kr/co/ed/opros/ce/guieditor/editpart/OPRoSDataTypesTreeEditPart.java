package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.swt.graphics.Image;

public class OPRoSDataTypesTreeEditPart extends OPRoSElementTreeEditPartBase {
	private static Image ICON_DATATYPES_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_DATATYPES_ELEMENT==null)
			ICON_DATATYPES_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("DataTypesTreeIcon"));
		return ICON_DATATYPES_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
//		super.createEditPolicies();
	}
	@Override
	protected void refreshVisuals() {
//		OPRoSExeEnvironmentElementModel model = (OPRoSExeEnvironmentElementModel)getModel();
		setWidgetText(OPRoSStrings.getString("DataTypesTreeModelLabel"));
		setWidgetImage(getWidgetImage());
//		super.refreshVisuals();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
}
