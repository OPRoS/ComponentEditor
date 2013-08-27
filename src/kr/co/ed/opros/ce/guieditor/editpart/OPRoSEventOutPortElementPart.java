package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSEventOutPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSEventOutPortElementPart extends OPRoSPortBasePart {
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSEventOutPortElementFigure();
		return figure;
	}
	
	protected void refreshVisuals(){
		OPRoSEventOutPortElementFigure figure = (OPRoSEventOutPortElementFigure) getFigure();
		OPRoSEventOutPortElementModel model = (OPRoSEventOutPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
	}
	
}
