package kr.co.ed.opros.ce.guieditor.figure;

import kr.co.ed.opros.ce.CommonFont;

import org.eclipse.draw2d.MarginBorder;

public class OPRoSServiceTypesElementFigure extends LabeledFigure {

	public OPRoSServiceTypesElementFigure(){
		super();
		getLabel().setFont(CommonFont.font5);
		getLabel().setBorder(new MarginBorder(2, 2, 2, 2));
	}
	
	public void setText(String dispName){
		getLabel().setText(dispName);
	}
	
}
