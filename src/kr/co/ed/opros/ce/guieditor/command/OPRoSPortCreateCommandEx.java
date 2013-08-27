package kr.co.ed.opros.ce.guieditor.command;

public class OPRoSPortCreateCommandEx extends OPRoSPortCreateCommand {
	
	@Override
	public void execute() {
		parent.addChild(element);
		element.setLayout(layout);
		if(element.getDirection()==1){
			element.setLayout(layout);
		}
	}
}
