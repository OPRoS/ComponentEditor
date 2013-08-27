package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Attribute;
import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSPropertyElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_PROPERTY_NAME = "Component Property Name";
	public static final String PROPERTY_PROPERTY_TYPE = "Component Property Type";
	public static final String PROPERTY_PROPERTY_DEFAULT_VALUE = "Component Property Default Vaule";
	private String name;
	private String type;
	private String defaultValue;
	
	public OPRoSPropertyElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
		name = OPRoSStrings.getString("PropertyEleNameDefault");
		type = OPRoSStrings.getString("PropertyEleTypeDefault");
		defaultValue = OPRoSStrings.getString("PropertyEleDefaultValueDefault");
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String oldValue = this.getName();
		this.name = name;
		getListeners().firePropertyChange(PROPERTY_PROPERTY_NAME, oldValue, name);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		String oldValue = this.getType();
		this.type = type;
		getListeners().firePropertyChange(PROPERTY_PROPERTY_TYPE, oldValue, type);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		String oldValue = this.getDefaultValue();
		this.defaultValue = defaultValue;
		getListeners().firePropertyChange(PROPERTY_PROPERTY_DEFAULT_VALUE, oldValue, defaultValue);
	}
	public void doLoad(Element ele){
		String tempStr;
		tempStr = ele.getAttributeValue("name");
		if(tempStr!=null)
			setName(tempStr);
		tempStr = ele.getAttributeValue("type");
		if(tempStr!=null)
			setType(tempStr);
		tempStr = ele.getTextTrim();
		if(tempStr!=null)
			setDefaultValue(tempStr);
	}
	public void doSave(Element ele){
		Attribute attr = new Attribute("name",getName());
		ele.setAttribute(attr);
		attr = new Attribute("type",getType());
		ele.setAttribute(attr);
		ele.setText(getDefaultValue());
	}
}
