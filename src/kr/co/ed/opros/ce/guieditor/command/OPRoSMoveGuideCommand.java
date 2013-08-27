package kr.co.ed.opros.ce.guieditor.command;

import java.util.Iterator;

import kr.co.ed.opros.ce.editors.OPRoSElementGuide;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class OPRoSMoveGuideCommand extends Command {
	private int pDelta;   
    private OPRoSElementGuide guide;   
  
    public OPRoSMoveGuideCommand(OPRoSElementGuide guide, int positionDelta) {   
        super("Move guide");   
        this.guide = guide;   
        pDelta = positionDelta;   
    }   
  
    @SuppressWarnings("unchecked")
	public void execute() {   
        guide.setPosition(guide.getPosition() + pDelta);   
        Iterator iter = guide.getParts().iterator();   
        while (iter.hasNext()) {   
            OPRoSElementBaseModel element = (OPRoSElementBaseModel) iter.next();   
            Rectangle layout = element.getLayout().getCopy();   
            if (guide.isHorizontal()) {   
                layout.y += pDelta;   
            } else {   
                layout.x += pDelta;   
            }   
            element.setLayout(layout);   
        }   
    }   
  
    @SuppressWarnings("unchecked")
	public void undo() {   
        guide.setPosition(guide.getPosition() - pDelta);   
        Iterator iter = guide.getParts().iterator();   
        while (iter.hasNext()) {   
        	OPRoSElementBaseModel element = (OPRoSElementBaseModel) iter.next();   
            Rectangle layout = element.getLayout().getCopy();   
            if (guide.isHorizontal()) {   
                layout.y -= pDelta;   
            } else {   
                layout.x -= pDelta;   
            }   
            element.setLayout(layout);   
        }   
    }   

}
