package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSDataTypeElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;

import org.eclipse.draw2d.IFigure;

public class OPRoSDataTypeElementPart extends OPRoSServiceTypeElementPart {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSDataTypeElementFigure();
		figure.setText(((OPRoSDataTypeElementModel)getCastedModel()).getDataTypeFileName());
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_DATATYPES_EVENT));
		
		return figure;
	}
	
}
