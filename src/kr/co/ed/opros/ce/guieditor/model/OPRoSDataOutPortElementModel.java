package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSDataOutPortElementModel extends OPRoSPortElementBaseModel {
	public static final String PROPERTY_DATAOUT_PORT_REFERENCE = "Data Out Reference File";
	
	private String reference;
	public OPRoSDataOutPortElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
		super.initializeData();
		setUsage(OPRoSStrings.getString("DataOutEleUsageDefault"));
		reference=OPRoSStrings.getString("DataOutEleReferDefault");
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		String oldValue = this.getReference();
		this.reference = reference;
		getListeners().firePropertyChange(PROPERTY_DATAOUT_PORT_REFERENCE, oldValue, reference);
	}
	public void doSave(Element parentEle){
		Element ele;
		ele=new Element("name");
		ele.setText(getName());
		parentEle.addContent(ele);
		ele=new Element("description");
		ele.setText(getDescription());
		parentEle.addContent(ele);
		ele=new Element("data_type");
		ele.setText(getType());
		parentEle.addContent(ele);
		ele=new Element("usage");
		ele.setText(getUsage());
		parentEle.addContent(ele);
		ele=new Element("reference");
		ele.setText(getReference());
		parentEle.addContent(ele);
		ele = new Element("layout");
		ele.setText(Integer.toString(getLayout().x)+","+Integer.toString(getLayout().y)
				+","+Integer.toString(getLayout().width)+","+Integer.toString(getLayout().height));
		parentEle.addContent(ele);
	}
}
