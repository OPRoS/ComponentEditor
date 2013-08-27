package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.editors.OPRoSElementGuide;
import kr.co.ed.opros.ce.editors.OPRoSElementRuler;

import org.eclipse.gef.commands.Command;

public class OPRoSCreateGuideCommand extends Command {
	private OPRoSElementGuide guide;   
    private OPRoSElementRuler parent;   
    private int position;   
  
    public OPRoSCreateGuideCommand(OPRoSElementRuler parent, int position) {   
        super("Create guide");   
        this.parent = parent;   
        this.position = position;   
    }   
  
    public boolean canUndo() {   
        return true;   
    }   
  
    public void execute() {   
        if (guide == null)   
            guide = new OPRoSElementGuide(!parent.isHorizontal());   
        guide.setPosition(position);   
        parent.addGuide(guide);   
    }   
  
    public void undo() {   
        parent.removeGuide(guide);   
    }   

}
