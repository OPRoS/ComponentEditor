package kr.co.ed.opros.ce.guieditor.editpart;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSExeEnvironmentElementFigure;

import org.eclipse.draw2d.IFigure;

public class OPRoSExeEnvironmentElementPart extends OPRoSServiceTypesElementPart {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSExeEnvironmentElementFigure();
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_ENVIRONMENT_EVENT));
		figure.setText(OPRoSStrings.getString("ExeEnvTreeModelLabel"));
		
		setFigureInfomation(figure);		
		return figure;
	}
	
}
