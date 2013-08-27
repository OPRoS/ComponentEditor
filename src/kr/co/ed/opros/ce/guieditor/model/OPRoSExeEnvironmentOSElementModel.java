package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSExeEnvironmentOSElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_OS_NAME = "OS Name";
	public static final String PROPERTY_OS_VERSION = "OS Version";
	private String OSName;
	private String OSVersion;
	
	public OPRoSExeEnvironmentOSElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
//		super.initializeData();
		OSName=OPRoSStrings.getString("OSEleNameDefault");
		OSVersion = OPRoSStrings.getString("OSEleVersionDefault");
	}
	public String getOSName() {
		return OSName;
	}
	public void setOSName(String name) {
		String oldValue = this.getOSName();
		OSName = name;
		getListeners().firePropertyChange(PROPERTY_OS_NAME, oldValue, name);
	}
	public String getOSVersion() {
		return OSVersion;
	}
	public void setOSVersion(String version) {
		String oldValue = this.getOSVersion();
		OSVersion = version;
		getListeners().firePropertyChange(PROPERTY_OS_VERSION, oldValue, version);
	}
	public void doLoad(Element ele){
		String tempStr;
		tempStr = ele.getAttributeValue("name");
		if(tempStr!=null)
			setOSName(tempStr);
		tempStr = ele.getAttributeValue("version");
		if(tempStr!=null)
			setOSVersion(tempStr);
	}
}
