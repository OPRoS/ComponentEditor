package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSExeEnvironmentCPUElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_CPU_NAME = "CPU Name";

	private String CPUName;
	
	public OPRoSExeEnvironmentCPUElementModel() {
		super();
		initializeData();
	}

	public void initializeData(){
//		super.initializeData();
		CPUName=OPRoSStrings.getString("CPUEleNameDefualt");
	}

	public String getCPUName() {
		return CPUName;
	}

	public void setCPUName(String name) {
		String oldValue = this.getCPUName();
		CPUName = name;
		getListeners().firePropertyChange(PROPERTY_CPU_NAME, oldValue, name);
	}
	public void doLoad(Element ele){
		String tempStr;
		tempStr = ele.getTextTrim();
		if(tempStr!=null)
			setCPUName(tempStr);
	}
}
