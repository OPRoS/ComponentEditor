package kr.co.ed.opros.ce.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;

public class OPRoSElementRuler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1499925989329469094L;
	public static final String PROPERTY_CHILDREN = "children changed";
	public static final String PROPERTY_UNIT = "units changed";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private int unit;
	private boolean horizontal;
	private List<OPRoSElementGuide> guides = new ArrayList<OPRoSElementGuide>();
	
	public OPRoSElementRuler(boolean isHorizontal){
		this(isHorizontal,RulerProvider.UNIT_PIXELS);
	}
	public OPRoSElementRuler(boolean isHorizontal, int uint){
		horizontal = isHorizontal;
		setUnit(uint);
	}
	public void addGuide(OPRoSElementGuide guide){
		if(!guides.contains(guide)){
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN,null,guide);
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	public List<OPRoSElementGuide> getGuides(){
		return guides;
	}
	public int getUnit(){
		return unit;
	}
	public boolean isHidden(){
		return false;
	}
	public boolean isHorizontal(){
		return horizontal;
	}
	public void removeGuide(OPRoSElementGuide guide){
		if(guides.remove(guide)){
			listeners.firePropertyChange(PROPERTY_CHILDREN,null,guide);
		}
	}
	public void remvoePropertyChagneListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	public void setHidden(boolean isHidden){
		
	}
	public void setUnit(int newUnit){
		if(unit != newUnit){
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT,oldUnit,newUnit);
		}
	}
}
