package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Attribute;
import org.jdom.Element;

public class MonitoringVariableModel extends OPRoSElementBaseModel {
	
	public static final String PROPERTY_NAME = "Property Name";
	
	private String name;
	private String oldName;	
	private String type;
	private String oldType;	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MonitoringVariableModel(){
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		String oldValue = this.getName();
		this.name = name;
		getListeners().firePropertyChange(PROPERTY_NAME, oldValue, name);
	}
	
	public boolean changeName(String name) {		
		if(!getName().equals(name)) {		
			if(oldName == null || oldName.equals("")) {
				oldName = getName();
			}
			setName(name);
			setChangeModel();
			return true;
		}
		return false;
	}

	public boolean changeType(String type) {		
		if(!getType().equals(type)) {		
			if(oldType == null || oldType.equals("")) {
				oldType = getType();
			}
			setType(type);
			setChangeModel();
			return true;
		}
		return false;
	}
	
	private void setChangeModel() {
		((OPRoSBodyElementModel)getParent().getParent().getParent()).addChangeMonitoring(this);
	}
		
	public void doLoad(Element ele){
		String tempStr;
		tempStr = ele.getAttributeValue("name");
		if(tempStr!=null)
			setName(tempStr);
		tempStr = ele.getAttributeValue("type");
		if(tempStr!=null)
			setType(tempStr);
	}
	
	public void doSave(Element parentEle){
		parentEle.setAttribute(new Attribute("name", getName()));
		parentEle.setAttribute(new Attribute("type", getType()));
	}
	
	public String getOldType() {
		return oldType;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public void setOldType(String oldType) {
		this.oldType = oldType;
	}

	
}
