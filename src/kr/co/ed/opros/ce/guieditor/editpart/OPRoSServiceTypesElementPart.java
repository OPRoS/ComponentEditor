package kr.co.ed.opros.ce.guieditor.editpart;


import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSServiceTypesElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.policy.NodeContainerEditPolicy;
import kr.co.ed.opros.ce.guieditor.policy.NodeSelectionEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;

public class OPRoSServiceTypesElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		ILabeledFigure figure = new OPRoSServiceTypesElementFigure();
		figure.setIcon(OPRoSActivator.getDefault().getImageRegistry().get(IIconConstants.IMG_SERVICETYPES_EVENT));
		figure.setText(OPRoSStrings.getString("ServiceTypesModelLabel"));
		
		setFigureInfomation(figure);		
		return figure;
	}
	
	public void setFigureInfomation(ILabeledFigure figure) {
		OPRoSElementBaseModel model = getCastedModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(new Color(null, 000, 148, 230));
		figure.setBorder(new LineBorder(new Color(null, 000, 127, 198)));
	}
	
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new NodeContainerEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NodeSelectionEditPolicy());
	}	

	@Override
	public IFigure getContentPane() {
		return ((ILabeledFigure)getFigure()).getContentPane();
	}
	
}
