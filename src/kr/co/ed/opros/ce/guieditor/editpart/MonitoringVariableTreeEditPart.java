package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

public class MonitoringVariableTreeEditPart extends
		OPRoSElementTreeEditPartBase {
	private static Image ICON_DATA_TYPE_ELEMENT;
	public static Image getWidgetImage(){
		if(ICON_DATA_TYPE_ELEMENT==null)
			ICON_DATA_TYPE_ELEMENT = OPRoSUtil.createImage(OPRoSStrings.getString("MonitoringVariableTreeIcon"));
		return ICON_DATA_TYPE_ELEMENT;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}
	@Override
	protected void refreshVisuals() {
		MonitoringVariableModel model = (MonitoringVariableModel)getModel();
		setWidgetText(model.getName());
		setWidgetImage(getWidgetImage());
		this.getWidget().setData(this);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(MonitoringVariableModel.PROPERTY_NAME)) refreshVisuals();
	}
}
