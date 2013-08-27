package kr.co.ed.opros.ce.guieditor.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kr.co.ed.opros.ce.editors.OPRoSElementGuide;
import kr.co.ed.opros.ce.editors.OPRoSElementRuler;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;

public class OPRoSDeleteGuideCommand extends Command {
	private OPRoSElementRuler parent;   
    private OPRoSElementGuide guide;   
    @SuppressWarnings("unchecked")
	private Map oldParts;   
  
    public OPRoSDeleteGuideCommand(OPRoSElementGuide guide, OPRoSElementRuler parent) {   
        super("Delete guide");   
        this.guide = guide;   
        this.parent = parent;   
    }   
  
    public boolean canUndo() {   
        return true;   
    }   
  
    @SuppressWarnings("unchecked")
	public void execute() {   
        oldParts = new HashMap(guide.getMap());   
        Iterator iter = oldParts.keySet().iterator();   
        while (iter.hasNext()) {   
            guide.detachElement((OPRoSElementBaseModel) iter.next());   
        }   
        parent.removeGuide(guide);   
    }   
  
    @SuppressWarnings("unchecked")
	public void undo() {   
        parent.addGuide(guide);   
        Iterator iter = oldParts.keySet().iterator();   
        while (iter.hasNext()) {   
        	OPRoSElementBaseModel element = (OPRoSElementBaseModel) iter.next();   
            guide.attachElement(element, ((Integer) oldParts.get(element)).intValue());   
        }   
    }   

}
