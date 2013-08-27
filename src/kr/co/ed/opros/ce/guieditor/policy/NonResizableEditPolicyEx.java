package kr.co.ed.opros.ce.guieditor.policy;

import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class NonResizableEditPolicyEx extends NonResizableEditPolicy {
	
	
	/*
	@Override
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();
		
		PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);		
		rect.setBounds(getPortRectangle(request));

		feedback.translateToRelative(rect);
		feedback.setBounds(rect);
	}
	*/
	
	/**
	 * 포트에 맞는 렉텡글을 구한다.
	 * @param request
	 * @return
	 */
	private Rectangle getPortRectangle(ChangeBoundsRequest request) {
		OPRoSPortElementBaseModel portModel = (OPRoSPortElementBaseModel)getHost().getModel();
		OPRoSBodyElementModel bodyModel = getBodyModel(portModel);
		if(bodyModel == null) 
			return null;
		OPRoSComponentElementModel compModel = bodyModel.getComponentModel();
		if(compModel == null)
			return null;
		
		PrecisionRectangle componentRect = new PrecisionRectangle(compModel.getLayout());
		ScalableFreeformRootEditPart part = (ScalableFreeformRootEditPart)bodyModel.getOPRoSEditor().getGraphicalViewer().getRootEditPart();
		
		
		Point newPoint = new Point(portModel.getLayout().getLocation().x + request.getMoveDelta().x, 
				portModel.getLayout().getLocation().y + request.getMoveDelta().y);		
		return (OPRoSUtil2.setLayout(newPoint, request.getSizeDelta(), portModel, componentRect.scale(part.getZoomManager().getZoom()), part.getZoomManager().getZoom()));
	}
	
	/**
	 * 켄버스 모델을 구한다.
	 * @param child
	 * @return
	 */
	private OPRoSBodyElementModel getBodyModel(OPRoSElementBaseModel child) {
		if(child.getParent() instanceof OPRoSBodyElementModel) {
			return (OPRoSBodyElementModel)child.getParent();
		}
		return getBodyModel(child.getParent());
	}
	
}
