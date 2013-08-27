package kr.co.ed.opros.ce.guieditor.model;

import org.jdom.Element;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSDataInPortElementModel extends OPRoSPortElementBaseModel {
	public static final String PROPERTY_DATAIN_PORT_REFERENCE = "Data In Reference File";
	public static final String PROPERTY_DATAIN_PORT_QUEUESIZE = "Data In Queue Size";
	public static final String PROPERTY_DATAIN_PORT_QUEUEING_POLICY = "Data In Queueing Policy";
	
	private String reference;
	private String queueSize;
	private String queueingPolicy;
	public OPRoSDataInPortElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
		super.initializeData();
		setUsage(OPRoSStrings.getString("DataInEleUsageDefault"));
		reference=OPRoSStrings.getString("DataInEleReferDefault");
		queueSize=OPRoSStrings.getString("DataInEleQueueSizeDefault");
		queueingPolicy=OPRoSStrings.getString("DataInEleQueueingPolicyDefault");
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		String oldValue = this.getReference();
		this.reference = reference;
		getListeners().firePropertyChange(PROPERTY_DATAIN_PORT_REFERENCE, oldValue, reference);
	}
	public String getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(String queueSize) {
		String oldValue = this.getQueueSize();
		this.queueSize = queueSize;
		getListeners().firePropertyChange(PROPERTY_DATAIN_PORT_QUEUESIZE, oldValue, queueSize);
	}
	public String getQueueingPolicy() {
		return queueingPolicy;
	}
	public void setQueueingPolicy(String queueingPolicy) {
		String oldValue = this.getQueueingPolicy();
		this.queueingPolicy = queueingPolicy;
		getListeners().firePropertyChange(PROPERTY_DATAIN_PORT_QUEUEING_POLICY, oldValue, queueingPolicy);
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
		ele=new Element("queueing_policy");
		ele.setText(getQueueingPolicy());
		parentEle.addContent(ele);
		ele=new Element("queue_size");
		ele.setText(getQueueSize());
		parentEle.addContent(ele);
		ele = new Element("layout");
		ele.setText(Integer.toString(getLayout().x)+","+Integer.toString(getLayout().y)
				+","+Integer.toString(getLayout().width)+","+Integer.toString(getLayout().height));
		parentEle.addContent(ele);
	}
}
