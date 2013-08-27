package kr.co.ed.opros.ce.guieditor.command;


import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;


/**
 * 라벨모델 추가 커맨드
 * @author lee6607
 *
 */
public class ElementAddCommand extends Command {

	protected OPRoSElementBaseModel child;

	protected OPRoSElementBaseModel after;

	protected OPRoSElementBaseModel newContainer;

	protected OPRoSElementBaseModel oldContainer;

	protected int startIndex;

	protected int destIndex;

	public ElementAddCommand(OPRoSElementBaseModel child, OPRoSElementBaseModel newContainer,
			OPRoSElementBaseModel oldContainer, OPRoSElementBaseModel after) {
		this.child = child;
		this.newContainer = newContainer;
		this.oldContainer = oldContainer;
		this.after = after;
	}

	@Override
	public void execute() {
		startIndex = oldContainer.getChildrenList().indexOf(child);
		oldContainer.removeChild(child);
		if (after != null) {
			destIndex = newContainer.getChildrenList().indexOf(after);
			newContainer.addChild(child, destIndex);
		} else {
			newContainer.addChild(child);
		}
	}

	@Override
	public void undo() {
		newContainer.removeChild(child);
		oldContainer.addChild(child, startIndex);
	}
}
