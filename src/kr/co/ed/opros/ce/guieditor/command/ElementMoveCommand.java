package kr.co.ed.opros.ce.guieditor.command;


import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;


/**
 * 변수 모델을 이동할때 쓰이는 커멘드
 * 기존 모델 이동 커멘드와 합칠수가없어 부득이하게 따로 클래스를만듦
 * @author lee6607
 *
 */
public class ElementMoveCommand extends Command {

	private OPRoSElementBaseModel parent;

	private OPRoSElementBaseModel child;

	private OPRoSElementBaseModel after;

	private int startIndex;

	private int destIndex;

	public ElementMoveCommand(OPRoSElementBaseModel child, OPRoSElementBaseModel parent,
			OPRoSElementBaseModel after) {
		this.child = child;
		this.parent = parent;
		this.after = after;
	}

	@Override
	public void execute() {
		startIndex = parent.getChildrenList().indexOf(child);
		parent.removeChild(child);
		if (after != null) {
			destIndex = parent.getChildrenList().indexOf(after);
			parent.addChild(child, destIndex);
		} else {
			parent.addChild(child);
		}
	}

	@Override
	public void undo() {
		parent.removeChild(child);
		parent.addChild(child, startIndex);
	}
}