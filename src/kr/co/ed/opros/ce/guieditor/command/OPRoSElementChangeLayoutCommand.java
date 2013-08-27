package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class OPRoSElementChangeLayoutCommand extends Command {
	private OPRoSElementBaseModel model;
	private Rectangle layout;
	private Rectangle oldLayout;
	public void execute(){
		model.setLayout(layout);
	}
	public void setConstraint(Rectangle rect){
		this.layout = rect;
	}
	public void setModel(Object model){
		this.model = (OPRoSElementBaseModel) model;
		this.oldLayout = this.model.getLayout();
	}
	public void undo(){
		model.setLayout(oldLayout);
	}
}
