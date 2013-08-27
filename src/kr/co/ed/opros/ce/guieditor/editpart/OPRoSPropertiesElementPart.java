package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSPropertiesElementFigure;

import org.eclipse.draw2d.IFigure;

public class OPRoSPropertiesElementPart extends OPRoSServiceTypesElementPart {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSPropertiesElementFigure();
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_PROPERTIES_EVENT));
		figure.setText(OPRoSStrings.getString("PropertiesModelLabel"));
		
		setFigureInfomation(figure);
		return figure;
	}
	
}
