package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSDataInPortElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

public class OPRoSDataInPortElementPart extends OPRoSPortBasePart {
	protected IFigure createFigure(){
		IFigure figure = new OPRoSDataInPortElementFigure();
		return figure;
	}
	protected void refreshVisuals(){
		OPRoSDataInPortElementFigure figure = (OPRoSDataInPortElementFigure) getFigure();
		OPRoSDataInPortElementModel model = (OPRoSDataInPortElementModel)getModel();
		figure.setDirection(model.getDirection());
		figure.setForegroundColor(model.getForegroundColor());
		figure.setLayout(model.getLayout());
		figure.setText(model.getName());
		
		if((model.getQueueSize() == null || model.getQueueSize().equals(""))
				|| (model.getQueueingPolicy() == null || model.getQueueingPolicy().equals(""))) {
			figure.getLabel().setForegroundColor(new Color(null, 255, 000, 000));			
		}
		else {
			figure.getLabel().setForegroundColor(new Color(null, 000, 000, 000));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME)
				|| evt.getPropertyName().equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUESIZE)
				|| evt.getPropertyName().equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUEING_POLICY)) 
			
			refreshVisuals();
	}
}
