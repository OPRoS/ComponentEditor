package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSServiceTypeElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class OPRoSServiceTypeElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSServiceTypeElementFigure();
		figure.setText(((OPRoSServiceTypeElementModel)getCastedModel()).getServiceTypeFileName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_SERVICETYPES_EVENT));
		
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}	
	
	@Override
	public IFigure getContentPane() {
		return ((ILabeledFigure) getFigure()).getContentPane();
	}
	
}
