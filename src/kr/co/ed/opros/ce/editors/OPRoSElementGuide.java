package kr.co.ed.opros.ce.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

public class OPRoSElementGuide implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4375010318557583321L;
	public static final String PROPERTY_CHILDREN = "subparts changed";
	public static final String PROPERTY_POSITION = "position changed";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private Map<OPRoSElementBaseModel, Integer> map;
	private int position;
	private boolean horizontal;
	
	public OPRoSElementGuide(){
		
	}
	public OPRoSElementGuide(boolean isHorizontal){
		setHorizontal(isHorizontal);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	
	public void attachElement(OPRoSElementBaseModel element, int alignment){
		if(getMap().containsKey(element)&&getAlignment(element)==alignment)
			return;
		getMap().put(element,alignment);
//		OPRoSElementGuide parent = isHorizontal() ? element.getHorizontalGuide() : element.getVerticalGuide();
//		if(parent != null && parent != this){
//			parent.detachElement(element);
//		}
//		if(isHorizontal()){
//			element.setHorizontalGuide(this);
//		}else {
//			element.setVerticalGuide(this);
//		}
		listeners.firePropertyChange(PROPERTY_CHILDREN,null,element);
	}
	public void detachElement(OPRoSElementBaseModel element){
		if(getMap().containsKey(element)){
			getMap().remove(element);
//			if(isHorizontal()){
//				element.setHorizontalGuide(null);
//			}else{
//				element.setVerticalGuide(null);
//			}
			listeners.firePropertyChange(PROPERTY_CHILDREN,null,element);
		}
	}
	public int getAlignment(OPRoSElementBaseModel element){
		if(getMap().get(element)!=null)
			return ((Integer)getMap().get(element)).intValue();
		return -2;
	}
	public Map<OPRoSElementBaseModel, Integer> getMap(){
		if(map==null){
			map = new Hashtable<OPRoSElementBaseModel, Integer>();
		}
		return map;
	}
	
	public Set<OPRoSElementBaseModel> getParts(){
		return getMap().keySet();
	}
	
	public int getPosition(){
		return position;
	}
	
	public boolean isHorizontal(){
		return horizontal;
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	public void setHorizontal(boolean isHorizontal){
		horizontal = isHorizontal;
	}
	public void setPosition(int offset){
		if(position != offset){
			int oldValue = position;
			position = offset;
			listeners.firePropertyChange(PROPERTY_POSITION, new Integer(oldValue), new Integer(position));
		}
	}
}
