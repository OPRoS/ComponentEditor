package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSPropertyElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class OPRoSPropertyElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSPropertyElementFigure();
		figure.setText(((OPRoSPropertyElementModel)getCastedModel()).getName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_PROPERTIES_EVENT));
		
		return figure;
	}
	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME)){
			refreshVisuals();
		}
		super.propertyChange(evt);
	}

	@Override
	protected void refreshVisuals() {		
		OPRoSPropertyElementFigure figure = (OPRoSPropertyElementFigure) getFigure();
		OPRoSPropertyElementModel model = (OPRoSPropertyElementModel)getModel();
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
