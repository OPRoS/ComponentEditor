package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSExeEnvironmentCPUElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementDeletePolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class OPRoSExeEnvironmentCPUElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSExeEnvironmentCPUElementFigure();
		figure.setText(((OPRoSExeEnvironmentCPUElementModel)getCastedModel()).getCPUName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_ENVIRONMENT_EVENT));
		
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OPRoSElementDeletePolicy());
	}
	
}
