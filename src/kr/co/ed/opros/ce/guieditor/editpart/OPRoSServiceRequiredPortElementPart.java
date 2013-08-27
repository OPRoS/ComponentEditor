package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSServiceRequiredPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSServiceRequiredPortElementPart extends OPRoSPortBasePart {
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSServiceRequiredPortElementFigure();
		return figure;
	}
	
	protected void refreshVisuals(){
		OPRoSServiceRequiredPortElementFigure figure = (OPRoSServiceRequiredPortElementFigure) getFigure();
		OPRoSServiceRequiredPortElementModel model = (OPRoSServiceRequiredPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
	}
	
}
