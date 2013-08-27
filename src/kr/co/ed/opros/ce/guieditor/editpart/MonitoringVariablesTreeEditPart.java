package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.swt.graphics.Image;

public class MonitoringVariablesTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	
	private static Image ICON_DATATYPES_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_DATATYPES_ELEMENT==null)
			ICON_DATATYPES_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("MonitoringVariableTreeIcon"));
		return ICON_DATATYPES_ELEMENT;
	}
	
	@Override
	protected void refreshVisuals() {
		setWidgetText(OPRoSStrings.getString("MonitoringVariablesLabel"));
		setWidgetImage(getWidgetImage());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
}
