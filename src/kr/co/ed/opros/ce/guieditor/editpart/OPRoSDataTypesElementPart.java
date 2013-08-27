package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSDataTypesElementFigure;

import org.eclipse.draw2d.IFigure;

public class OPRoSDataTypesElementPart extends OPRoSServiceTypesElementPart {
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSDataTypesElementFigure();
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_DATATYPES_EVENT));
		figure.setText(OPRoSStrings.getString("DataTypesModelLabel"));
		
		setFigureInfomation(figure);
		return figure;
	}
}
