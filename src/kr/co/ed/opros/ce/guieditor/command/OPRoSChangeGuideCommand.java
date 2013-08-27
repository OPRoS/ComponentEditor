package kr.co.ed.opros.ce.guieditor.command;

import kr.co.ed.opros.ce.editors.OPRoSElementGuide;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;

public class OPRoSChangeGuideCommand extends Command {
	private OPRoSElementBaseModel element;   
    private OPRoSElementGuide oldGuide, newGuide;   
    private int oldAlign, newAlign;   
//    private boolean horizontal;   
  
    public OPRoSChangeGuideCommand(OPRoSElementBaseModel element, boolean horizontalGuide) {   
        super();   
        this.element = element;   
//        horizontal = horizontalGuide;   
    }   
  
    protected void changeGuide(OPRoSElementGuide oldGuide, OPRoSElementGuide newGuide,   
            int newAlignment) {   
        if (oldGuide != null && oldGuide != newGuide) {   
            oldGuide.detachElement(element);   
        }   
        // You need to re-attach the part even if the oldGuide and the newGuide   
        // are the same   
        // because the alignment could have changed   
        if (newGuide != null) {   
            newGuide.attachElement(element, newAlignment);   
        }   
    }   
  
    public void execute() {   
        // Cache the old values   
//        oldGuide = horizontal ? element.getHorizontalGuide() : element   
//                .getVerticalGuide();   
        if (oldGuide != null)   
            oldAlign = oldGuide.getAlignment(element);   
  
        redo();   
    }   
  
    public void redo() {   
        changeGuide(oldGuide, newGuide, newAlign);   
    }   
  
    public void setNewGuide(OPRoSElementGuide guide, int alignment) {   
        newGuide = guide;   
        newAlign = alignment;   
    }   
  
    public void undo() {   
        changeGuide(newGuide, oldGuide, oldAlign);   
    }   

}
