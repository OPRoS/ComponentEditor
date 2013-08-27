package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSDataOutPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSDataOutPortElementPart extends OPRoSPortBasePart {
	protected IFigure createFigure(){
		IFigure figure = new OPRoSDataOutPortElementFigure();
		return figure;
	}
	protected void refreshVisuals(){
		OPRoSDataOutPortElementFigure figure = (OPRoSDataOutPortElementFigure) getFigure();
		OPRoSDataOutPortElementModel model = (OPRoSDataOutPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
	}
	
}
