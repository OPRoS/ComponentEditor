package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSEventInPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSEventInPortElementPart extends OPRoSPortBasePart {
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSEventInPortElementFigure();
		return figure;
	}
	
	protected void refreshVisuals(){
		OPRoSEventInPortElementFigure figure = (OPRoSEventInPortElementFigure) getFigure();
		OPRoSEventInPortElementModel model = (OPRoSEventInPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
	}
}
