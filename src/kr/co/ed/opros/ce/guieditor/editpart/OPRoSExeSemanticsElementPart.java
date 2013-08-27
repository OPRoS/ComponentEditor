package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSExeSemanticsElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeSemanticsElementModel;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

public class OPRoSExeSemanticsElementPart extends OPRoSElementPartBase {
	
	
	protected IFigure createFigure(){
		IFigure figure = new OPRoSExeSemanticsElementFigure();
		return new Figure();
	}
	
	protected void refreshVisuals(){
		/*
		OPRoSExeSemanticsElementFigure figure = (OPRoSExeSemanticsElementFigure)getFigure();
		OPRoSExeSemanticsElementModel model = (OPRoSExeSemanticsElementModel) getModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
//		figure.setLayout(model.getLayout());
 * */
	}
}
