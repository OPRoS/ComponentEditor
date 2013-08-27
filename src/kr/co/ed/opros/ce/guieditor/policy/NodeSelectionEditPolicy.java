package kr.co.ed.opros.ce.guieditor.policy;

import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesTreeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;


/**
 * ParentModel�� ���õǾ����� �ν��� �׿������� �θ� ���� �����ϵ���..... �����Ҹ���.... ��ư...
 * @author lee6607
 *
 */
public class NodeSelectionEditPolicy extends GraphicalEditPolicy{

	@Override
	public EditPart getTargetEditPart(Request request) {
		if(request.getType().equals(RequestConstants.REQ_SELECTION)){
			EditPart part = getHost();
			if(part instanceof OPRoSServiceTypesElementPart ||
					part instanceof OPRoSServiceTypesTreeEditPart) 
				part = part.getParent();
			return part;
		}
		return null;
	}
}
