package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.MonitoringVariableFigure;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class MonitoringVariableEditPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new MonitoringVariableFigure();
		figure.setText(((MonitoringVariableModel)getCastedModel()).getName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.SAMPLE_ICON));
		
		return figure;
	}
	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(MonitoringVariableModel.PROPERTY_NAME)){
			refreshVisuals();
		}
		super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {		
		MonitoringVariableFigure figure = (MonitoringVariableFigure) getFigure();
		MonitoringVariableModel model = (MonitoringVariableModel)getModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setText(model.getName());		
		
		Rectangle bounds = new Rectangle(getCastedModel().getLayout());	
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,	getFigure(), bounds);
		
		super.registerVisuals();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}
}
