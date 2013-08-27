package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSEventInPortElementModel extends OPRoSPortElementBaseModel {
	public OPRoSEventInPortElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
		super.initializeData();
		setUsage(OPRoSStrings.getString("EventInEleUsageDefault"));
	}
	public void doSave(Element parentEle){
		Element ele;
		ele = new Element("name");
		ele.setText(getName());
		parentEle.addContent(ele);
		ele = new Element("description");
		ele.setText(getDescription());
		parentEle.addContent(ele);
		ele = new Element("data_type");
		ele.setText(getType());
		parentEle.addContent(ele);
		ele = new Element("usage");
		ele.setText(getUsage());
		parentEle.addContent(ele);
		ele = new Element("layout");
		ele.setText(Integer.toString(getLayout().x)+","+Integer.toString(getLayout().y)
				+","+Integer.toString(getLayout().width)+","+Integer.toString(getLayout().height));
		parentEle.addContent(ele);
	}
}
