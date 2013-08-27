package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSComponentElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

public class OPRoSComponentElementPart extends OPRoSElementPartBase {
	
	protected IFigure createFigure(){
		OPRoSComponentElementFigure figure = new OPRoSComponentElementFigure();
		OPRoSComponentElementModel model = (OPRoSComponentElementModel)getModel();
		figure.setText(model.getComponentName());
		
		if (model.isCollapsed()) {
			figure.remove(figure.getContentPane());
		} else {
			figure.add(figure.getContentPane());
		}
		
		return figure;
	}
	
	protected void refreshVisuals(){
		OPRoSComponentElementFigure figure = (OPRoSComponentElementFigure)getFigure();
		OPRoSComponentElementModel model = (OPRoSComponentElementModel)getModel();
		figure.setLayout(model.getLayout());
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSComponentElementModel.PROPERTY_COMP_NAME)) refreshVisuals();
		
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_COLLAPSED)){
			if (getCastedModel().isCollapsed()) {
				getFigure().remove(getContentPane());
			} else {
				getFigure().add(getContentPane());
			}
			refreshVisuals();	
		}
	}
	
	
	@Override
	public void performRequest(Request req) {
		super.performRequest(req);
		if (req.getType() == RequestConstants.REQ_OPEN) {
			getCastedModel().setCollapsed(!getCastedModel().isCollapsed());			
		}
	}
	
	@Override
	public IFigure getContentPane() {
		return ((ILabeledFigure)getFigure()).getContentPane();
	}
	
}
