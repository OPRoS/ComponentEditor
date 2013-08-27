package kr.co.ed.opros.ce.guieditor.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSBodyElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementEditLayoutPolicy;

public class OPRoSBodyElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSBodyElementFigure();
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new OPRoSElementEditLayoutPolicy());
	}
	
	protected void refreshVisuals(){
		OPRoSBodyElementFigure figure = (OPRoSBodyElementFigure)getFigure();
		OPRoSBodyElementModel model = (OPRoSBodyElementModel) getModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
		figure.setBorder(new LineBorder(model.getBorder()));
		figure.setFilename(model.getFilename());
	}

}
