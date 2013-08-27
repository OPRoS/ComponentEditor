package kr.co.ed.opros.ce.guieditor.command;

public class OPRoSDeleteCommandEx extends OPRoSDeleteCommand {

	@Override
	public void execute() {
		parentModel.removeChild(model);
	}
	
}
