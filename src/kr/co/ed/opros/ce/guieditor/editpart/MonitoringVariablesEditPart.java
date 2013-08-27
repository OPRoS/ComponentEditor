package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.MonitoringVariablesFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Color;

public class MonitoringVariablesEditPart extends OPRoSServiceTypesElementPart {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new MonitoringVariablesFigure();
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.SAMPLE_ICON));
		figure.setText(OPRoSStrings.getString("MonitoringVariablesLabel"));
		
		setFigureInfomation(figure);		
		return figure;
	}
	
	/*
	public void setFigureInfomation(ILabeledFigure figure) {
		OPRoSElementBaseModel model = getCastedModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(new Color(null, 110, 110, 110));
		figure.setBorder(new LineBorder(new Color(null, 70, 70, 70)));
	}
	*/
	
}
