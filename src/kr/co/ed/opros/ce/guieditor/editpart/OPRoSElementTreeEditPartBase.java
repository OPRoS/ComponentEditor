/**
 * 
 */
package kr.co.ed.opros.ce.guieditor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

/**
 * @author jwkim
 *
 */
public class OPRoSElementTreeEditPartBase extends
		AbstractTreeEditPart implements PropertyChangeListener {

	@Override
	public void activate() {
		super.activate();
		((OPRoSElementBaseModel)getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		((OPRoSElementBaseModel)getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List getModelChildren() {
		return ((OPRoSElementBaseModel)getModel()).getChildrenList();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_ADD)) refreshChildren();
		if(evt.getPropertyName().equals(OPRoSElementBaseModel.PROPERTY_REMOVE)) refreshChildren();
	}

}
