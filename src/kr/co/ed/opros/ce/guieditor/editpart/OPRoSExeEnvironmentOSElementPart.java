package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSExeEnvironmentOSElementFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSPropertyElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class OPRoSExeEnvironmentOSElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSExeEnvironmentOSElementFigure();
		figure.setText(((OPRoSExeEnvironmentOSElementModel)getCastedModel()).getOSName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_ENVIRONMENT_EVENT));
		
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME)){
			refreshVisuals();
		}
		super.propertyChange(evt);
	}
	
	@Override
	protected void refreshVisuals() {		
		OPRoSExeEnvironmentOSElementFigure figure = (OPRoSExeEnvironmentOSElementFigure) getFigure();
		OPRoSExeEnvironmentOSElementModel model = (OPRoSExeEnvironmentOSElementModel)getModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setText(model.getOSName());		
		
		Rectangle bounds = new Rectangle(getCastedModel().getLayout());	
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,	getFigure(), bounds);
		
		super.registerVisuals();
	}
	
}
