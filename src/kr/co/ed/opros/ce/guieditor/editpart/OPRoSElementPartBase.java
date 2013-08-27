package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import kr.co.ed.opros.ce.editors.ElementChangeDialogCall;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.policy.OPRoSElementEditLayoutPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class OPRoSElementPartBase extends AbstractGraphicalEditPart implements PropertyChangeListener {
	
	public ElementChangeDialogCall elementChangecall;

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected List<OPRoSElementBaseModel> getModelChildren(){
		return ((OPRoSElementBaseModel) getModel()).getChildrenList();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new OPRoSElementEditLayoutPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_LAYOUT)){
			refreshVisuals();
		}
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_ADD)){
			refreshChildren();
		}
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_REMOVE)){
			refreshChildren();
		}
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME)){
			refreshVisuals();
		}
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_DESCRIPTION)){
			refreshVisuals();
		}
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_USAGE)){
			refreshVisuals();
		}
		if(evt.getPropertyName().equals(OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE)){
			((OPRoSBodyElementModel)this.getParent().getModel()).getOPRoSEditor().setOPRoSDirty();
		}
	}
	public void activate(){
		super.activate();
		((OPRoSElementBaseModel)getModel()).addPropertyChangeListener(this);
	}
	public void deactivate(){
		((OPRoSElementBaseModel)getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	public void performRequest(Request req) {
		if(req.getType()==REQ_OPEN){
			getElementChangecall().run();
		}
		super.performRequest(req);
	}
	
	/**
	 * 에디터파트에 연결된 모델을 오프러스 모델로 변환해 리턴한다.
	 * @return
	 */
	public OPRoSElementBaseModel getCastedModel() {
		return (OPRoSElementBaseModel)getModel();
	}
	
	/**
	 * 엘리먼트에 따른 다이얼로그를 띄워 수정할 수 있게해주는 클래스를 리턴한다.
	 * @return
	 */
	public ElementChangeDialogCall getElementChangecall() {
		if(elementChangecall == null)
			elementChangecall = new ElementChangeDialogCall(this);
		return elementChangecall;
	}
	
}
