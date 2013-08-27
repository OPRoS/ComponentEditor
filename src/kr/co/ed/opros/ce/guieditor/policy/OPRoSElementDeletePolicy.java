package kr.co.ed.opros.ce.guieditor.policy;

import kr.co.ed.opros.ce.guieditor.command.OPRoSDeleteCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class OPRoSElementDeletePolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		OPRoSDeleteCommand command = new OPRoSDeleteCommand();
		command.setModel(getHost().getModel());
		command.setParentModel(getHost().getParent().getModel());
		return command;
	}
	
}
