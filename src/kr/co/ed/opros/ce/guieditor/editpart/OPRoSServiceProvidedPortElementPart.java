package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSServiceProvidedPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSServiceProvidedPortElementPart extends OPRoSPortBasePart {
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSServiceProvidedPortElementFigure();
		return figure;
	}
	
	protected void refreshVisuals(){
		OPRoSServiceProvidedPortElementFigure figure = (OPRoSServiceProvidedPortElementFigure) getFigure();
		OPRoSServiceProvidedPortElementModel model = (OPRoSServiceProvidedPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
	}
}
