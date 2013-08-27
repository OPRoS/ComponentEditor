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
	 * ��������Ʈ�� ����� ���� �������� �𵨷� ��ȯ�� �����Ѵ�.
	 * @return
	 */
	public OPRoSElementBaseModel getCastedModel() {
		return (OPRoSElementBaseModel)getModel();
	}
	
	/**
	 * ������Ʈ�� ���� ���̾�α׸� ��� ������ �� �ְ����ִ� Ŭ������ �����Ѵ�.
	 * @return
	 */
	public ElementChangeDialogCall getElementChangecall() {
		if(elementChangecall == null)
			elementChangecall = new ElementChangeDialogCall(this);
		return elementChangecall;
	}
	
}
