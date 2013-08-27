package kr.co.ed.opros.ce.guieditor.model;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jdom.Element;

public class OPRoSExeSemanticsElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_EXE_TYPE = "Execution Type";
	public static final String PROPERTY_EXE_PERIOD = "Execution Period";
	public static final String PROPERTY_EXE_PRIORITY = "Execution Priority";
	public static final String PROPERTY_EXE_INSTANCE_TYPE = "Execution Instance Type";
	
	private String exeType;
	private String exePeriod;
	private String exePriority;
	private String exeInstanceType;
	
	public OPRoSExeSemanticsElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
	
		exeType = OPRoSStrings.getString("CompInfoExeTypeDefault");
		exePeriod = OPRoSStrings.getString("CompInfoExePeriodDefault");
		exePriority = OPRoSStrings.getString("CompInfoExePriorityDefault");
		exeInstanceType = OPRoSStrings.getString("CompInfoExeInstanceTypeDefault");
		
		setLayout(new Rectangle(80,130,100,25));
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.lightBlue);
//		setBorder(1);
	}
	
	public String getExeType() {
		return exeType;
	}
	
	public void setExeType(String exeType) {
		String oldValue = this.getExeType();
		this.exeType = exeType;
		getListeners().firePropertyChange(PROPERTY_EXE_TYPE, oldValue, exeType);
	}
	
	public String getExePeriod() {
		return exePeriod;
	}
	
	public void setExePeriod(String exePeriod) {
		String oldValue = this.getExePeriod();
		this.exePeriod = exePeriod;
		getListeners().firePropertyChange(PROPERTY_EXE_PERIOD, oldValue, exePeriod);
	}
	
	public String getExePriority() {
		return exePriority;
	}
	
	public void setExePriority(String exePriority) {
		String oldValue = this.getExePriority();
		this.exePriority = exePriority;
		getListeners().firePropertyChange(PROPERTY_EXE_PRIORITY, oldValue, exePriority);
	}
	
	public String getExeInstanceType() {
		return exeInstanceType;
	}
	
	public void setExeInstanceType(String exeInstanceType) {
		String oldValue = this.getExeInstanceType();
		this.exeInstanceType = exeInstanceType;
		getListeners().firePropertyChange(PROPERTY_EXE_INSTANCE_TYPE, oldValue, exeInstanceType);
	}
	
	public void doLoad(Element parentEle){
		String tempStr;
		//execution semantics
		tempStr = parentEle.getChildTextTrim("type");
		if(tempStr!=null)
			setExeType(tempStr);
		tempStr = parentEle.getChildTextTrim("period");
		if(tempStr!=null)
			setExePeriod(tempStr);
		tempStr = parentEle.getChildTextTrim("priority");
		if(tempStr!=null)
			setExePriority(tempStr);
		tempStr = parentEle.getChildTextTrim("instance_creation");
		if(tempStr!=null)
			setExeInstanceType(tempStr);
	}
	public void doSave(Element parentEle){
		Element ele;
	
		ele = new Element("type");
		ele.setText(getExeType());
		parentEle.addContent(ele);
		ele = new Element("period");
		ele.setText(getExePeriod());
		parentEle.addContent(ele);
		ele = new Element("priority");
		ele.setText(getExePriority());
		parentEle.addContent(ele);
		ele = new Element("instance_creation");
		ele.setText(getExeInstanceType());
		parentEle.addContent(ele);
	}
}
