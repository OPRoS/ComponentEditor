package kr.co.ed.opros.ce.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.guieditor.command.OPRoSCreateGuideCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSDeleteGuideCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSMoveGuideCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

public class OPRoSElementRulerProvider extends RulerProvider {
	private OPRoSElementRuler ruler;
	private PropertyChangeListener rulerListener = new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent evt){
			if(evt.getPropertyName().equals(OPRoSElementRuler.PROPERTY_CHILDREN)){
				OPRoSElementGuide guide = (OPRoSElementGuide) evt.getNewValue();
				if(getGuides().contains(guide)){
					guide.addPropertyChangeListener(guideListener);
				}else{
					guide.removePropertyChangeListener(guideListener);
				}
				for(int i=0; i<listeners.size();i++){
					 ((RulerChangeListener) listeners.get(i))   
                     .notifyUnitsChanged(ruler.getUnit());   
				}   
			}   
		}   
	};   
	private PropertyChangeListener guideListener = new PropertyChangeListener() {   
		public void propertyChange(PropertyChangeEvent evt) {   
			if (evt.getPropertyName().equals(OPRoSElementGuide.PROPERTY_CHILDREN)) {   
				for (int i = 0; i < listeners.size(); i++) {   
					((RulerChangeListener) listeners.get(i))
                     .notifyPartAttachmentChanged(evt.getNewValue(), evt.getSource());   
				}   
			} else {   
				for (int i = 0; i < listeners.size(); i++) {   
					((RulerChangeListener) listeners.get(i))   
					.notifyGuideMoved(evt.getSource());   
				}   
			}   
		}   
	};   

	public OPRoSElementRulerProvider(OPRoSElementRuler ruler) {   
		this.ruler = ruler;   
		this.ruler.addPropertyChangeListener(rulerListener);   
		List<?> guides = getGuides();   
		for (int i = 0; i < guides.size(); i++) {   
			((OPRoSElementGuide) guides.get(i))   
			.addPropertyChangeListener(guideListener);   
		}   
	}   

	public List<Object> getAttachedModelObjects(Object guide) {   
		return new ArrayList<Object>(((OPRoSElementGuide) guide).getParts());   
	}   

	public Command getCreateGuideCommand(int position) {   
		return new OPRoSCreateGuideCommand(ruler, position);   
	}   

	public Command getDeleteGuideCommand(Object guide) {   
		return new OPRoSDeleteGuideCommand((OPRoSElementGuide) guide, ruler);   
	}   

	public Command getMoveGuideCommand(Object guide, int pDelta) {   
		return new OPRoSMoveGuideCommand((OPRoSElementGuide) guide, pDelta);   
	}   

	public int[] getGuidePositions() {   
		List<OPRoSElementGuide> guides = getGuides();   
		int[] result = new int[guides.size()];   
		for (int i = 0; i < guides.size(); i++) {   
			result[i] = ((OPRoSElementGuide) guides.get(i)).getPosition();   
		}   
		return result;   
	}   

	public Object getRuler() {   
		return ruler;   
	}   

	public int getUnit() {   
		return ruler.getUnit();   
	}   

	public void setUnit(int newUnit) {   
		ruler.setUnit(newUnit);   
	}   

	public int getGuidePosition(Object guide) {   
		return ((OPRoSElementGuide) guide).getPosition();   
	}   

	public List<OPRoSElementGuide> getGuides() {   
		return ruler.getGuides();   
	}   

}
