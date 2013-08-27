package kr.co.ed.opros.ce.guieditor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jdom.Element;

import kr.co.ed.opros.ce.editors.OPRoSGUIProfileEditor;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

public class OPRoSBodyElementModel extends OPRoSElementBaseModel {
	private String filename;
	public OPRoSComponentElementModel compEle;
	private ArrayList<OPRoSPortElementBaseModel> delPortList;
	private ArrayList<OPRoSPortElementBaseModel> addPortList;
	private ArrayList<OPRoSServiceTypeElementModel> changeServiceType;
	
	private ArrayList<MonitoringVariableModel> addMonitoringList;
	private ArrayList<MonitoringVariableModel> delMonitoringList;
	private ArrayList<MonitoringVariableModel> changeMonitoringList;
	private static OPRoSBodyElementModel instance;
	public OPRoSGUIProfileEditor oprosEditor;

	public OPRoSBodyElementModel(OPRoSGUIProfileEditor editor) {
		super();
		initializeData();
		instance=this;
		setOPRoSEditor(editor);
	}
	
	public void initializeData(){
//		super.initializeData();
		filename=OPRoSStrings.getString("BodyEleFileNameDefault");
		setBorder(2);
		compEle = new OPRoSComponentElementModel();
		addChild(compEle);
		addPortList = new ArrayList<OPRoSPortElementBaseModel>();
		delPortList = new ArrayList<OPRoSPortElementBaseModel>();
		changeServiceType = new ArrayList<OPRoSServiceTypeElementModel>();
		
		addMonitoringList = new ArrayList<MonitoringVariableModel>();
		delMonitoringList = new ArrayList<MonitoringVariableModel>();
		changeMonitoringList = new ArrayList<MonitoringVariableModel>();
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void doLoad(String fileName, Element parentEle){
//		super.doLoad(parentEle);
		if(fileName != null) {
			
		}
		setFilename(fileName);
		String profileDirPath = getFilename().substring(0, getFilename().lastIndexOf("/"));
		compEle.doLoad(parentEle, profileDirPath);
		doPortLoad(parentEle);
	}
	public void doSave(Element parentEle){
		String profileDirPath = getFilename().substring(0, getFilename().lastIndexOf("/"));
		compEle.doSave(parentEle,profileDirPath);
		Element portEle=null;
		portEle=doServicePortSave(portEle);
		portEle=doDataPortSave(portEle);
		portEle=doEventPortSave(portEle);
		if(portEle!=null)
			parentEle.addContent(portEle);
	}
	private Element doServicePortSave(Element parentEle){
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		Element ele;
		while(it.hasNext()){
			if(parentEle==null)
				parentEle = new Element("ports");
			OPRoSElementBaseModel model = it.next();
			if(model instanceof OPRoSServiceProvidedPortElementModel){
				ele=new Element("service_port");
				((OPRoSServiceProvidedPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}else if(model instanceof OPRoSServiceRequiredPortElementModel){
				ele=new Element("service_port");
				((OPRoSServiceRequiredPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}
		}
		return parentEle;
	}
	private Element doDataPortSave(Element parentEle){
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		Element ele;
		while(it.hasNext()){
			if(parentEle==null)
				parentEle = new Element("ports");
			OPRoSElementBaseModel model = it.next();
			if(model instanceof OPRoSDataInPortElementModel){
				ele=new Element("data_port");
				((OPRoSDataInPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}else if(model instanceof OPRoSDataOutPortElementModel){
				ele=new Element("data_port");
				((OPRoSDataOutPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}
		}
		return parentEle;
	}
	private Element doEventPortSave(Element parentEle){
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		Element ele;
		while(it.hasNext()){
			if(parentEle==null)
				parentEle = new Element("ports");
			OPRoSElementBaseModel model = it.next();
			if(model instanceof OPRoSEventInPortElementModel){
				ele=new Element("event_port");
				((OPRoSEventInPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}else if(model instanceof OPRoSEventOutPortElementModel){
				ele=new Element("event_port");
				((OPRoSEventOutPortElementModel)model).doSave(ele);
				parentEle.addContent(ele);
			}
		}
		return parentEle;
	}
	@SuppressWarnings("unchecked")
	private void doPortLoad(Element parentEle){
		String tempStr;
		Element portEle;
		Element subEle;
		portEle = parentEle.getChild("ports");
		if(portEle!=null){
			Iterator<Element> it = portEle.getChildren("service_port").iterator();
			while(it.hasNext()){
				subEle = it.next();
				tempStr=subEle.getChildTextTrim("usage");
				if(tempStr.compareTo("provided")==0){
					OPRoSServiceProvidedPortElementModel model = new OPRoSServiceProvidedPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("type");
					model.setType(tempStr);
					tempStr=subEle.getChildTextTrim("reference");
					model.setReference(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
				}
				else{
					OPRoSServiceRequiredPortElementModel model = new OPRoSServiceRequiredPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("type");
					model.setType(tempStr);
					tempStr=subEle.getChildTextTrim("reference");
					model.setReference(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
				}
			}
			it = portEle.getChildren("data_port").iterator();
			while(it.hasNext()){
				subEle = it.next();
				tempStr=subEle.getChildTextTrim("usage");
				if(tempStr.compareTo("input")==0){
					OPRoSDataInPortElementModel model = new OPRoSDataInPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("data_type");
					model.setType(tempStr);
					tempStr=subEle.getChildTextTrim("reference");
					model.setReference(tempStr);
					tempStr=subEle.getChildTextTrim("queueing_policy");
					model.setQueueingPolicy(tempStr);
					tempStr=subEle.getChildTextTrim("queue_size");
					model.setQueueSize(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
				}
				else{
					OPRoSDataOutPortElementModel model = new OPRoSDataOutPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("data_type");
					model.setType(tempStr);
					tempStr=subEle.getChildTextTrim("reference");
					model.setReference(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
				}
			}
			it = portEle.getChildren("event_port").iterator();
			while(it.hasNext()){
				subEle = it.next();
				tempStr=subEle.getChildTextTrim("usage");
				if(tempStr.compareTo("input")==0){
					OPRoSEventInPortElementModel model = new OPRoSEventInPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("data_type");
					model.setType(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
					
				}
				else{
					OPRoSEventOutPortElementModel model = new OPRoSEventOutPortElementModel();
					model.setUsage(tempStr);
					tempStr=subEle.getChildTextTrim("name");
					model.setName(tempStr);
					tempStr=subEle.getChildTextTrim("description");
					model.setDescription(tempStr);
					tempStr=subEle.getChildTextTrim("data_type");
					model.setType(tempStr);
					addChild(model);
					tempStr = subEle.getChildTextTrim("layout");
					if(tempStr!=null){
						StringTokenizer token=new StringTokenizer(tempStr,",");
						Rectangle rect = new Rectangle();
						if(token.hasMoreTokens()){
							rect.x=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.y=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.width=Integer.valueOf(token.nextToken());
						}
						if(token.hasMoreTokens()){
							rect.height=Integer.valueOf(token.nextToken());
						}
						model.setLayout(rect);
					}
					else {
						Rectangle rect = new Rectangle(0,0,0,0);
						model.setLayout(rect);
					}
				}
			}
		}
	}
	
	public ArrayList<OPRoSServiceTypeElementModel> getChangeServiceType() {
		return changeServiceType;
	}

	public void setChangeServiceType(
			ArrayList<OPRoSServiceTypeElementModel> changeServiceType) {
		this.changeServiceType = changeServiceType;
	}
	
	public boolean addChangeServiceType(OPRoSServiceTypeElementModel model) {
		if(!changeServiceType.contains(model)) {
			changeServiceType.add(model);
			return true;
		}
		return false;
	}
	
	public boolean removeChangeServiceType(OPRoSServiceTypeElementModel model) {
		if(changeServiceType.contains(model)) {
			changeServiceType.remove(model);
			return true;
		}
		return false;
	}
	
	public void addDelPortList(OPRoSPortElementBaseModel model){
		if(addPortList.contains(model)){
			addPortList.remove(model);
			return;
		}
		delPortList.add(model);
	}
	public void addAddPortList(OPRoSPortElementBaseModel model){
		addPortList.add(model);
	}
	public Iterator<OPRoSPortElementBaseModel> getDelPortList(){
		return delPortList.iterator();
	}
	public Iterator<OPRoSPortElementBaseModel> getAddPortList(){
		return addPortList.iterator();
	}
	public void clearModifyPortList(){
		addPortList.clear();
		delPortList.clear();
		changeServiceType.clear();
		addMonitoringList.clear();
		delMonitoringList.clear();
		changeMonitoringList.clear();
	}
	public static OPRoSBodyElementModel getInstance(){
		return instance;
	}
	public boolean removeDelPortList(OPRoSPortElementBaseModel model){
		if(delPortList.contains(model)){
			delPortList.remove(model);
			return true;
		}
		return false;
	}
	public boolean removeAddPortList(OPRoSPortElementBaseModel model){
		if(addPortList.contains(model)){
			addPortList.remove(model);
			return true;
		}
		return false;
	}
	public boolean isNeedSourceModify(){
		boolean bRet=false;
		if(!addPortList.isEmpty()){
			bRet=true;
		}
		if(!delPortList.isEmpty()){
			bRet=true;
		}
		if(!changeServiceType.isEmpty()){
			bRet=true;
		}	
		if(!addMonitoringList.isEmpty()){
			bRet=true;
		}
		if(!delMonitoringList.isEmpty()){
			bRet=true;
		}
		if(!changeMonitoringList.isEmpty()){
			bRet=true;
		}
		return bRet;
	}
	
	public void setOPRoSEditor(OPRoSGUIProfileEditor oprosEditor) {
		this.oprosEditor = oprosEditor;
	}
	
	public OPRoSGUIProfileEditor getOPRoSEditor() {
		return oprosEditor;
	}
	
	/**
	 * 다이어그램에 포함된 컴포넌트 모델을 리턴한다.
	 * @return
	 */
	public OPRoSComponentElementModel getComponentModel() {
		List<OPRoSElementBaseModel> list = getChildrenList();
		for(OPRoSElementBaseModel model : list) {
			if(model instanceof OPRoSComponentElementModel)
				return (OPRoSComponentElementModel)model;
		}
		return null;
	}
	
	/**
	 * 컴포넌트에 등록된 포트모델을 구한다.
	 * @return
	 */
	public List<OPRoSPortElementBaseModel> getPortModel() {
		List<OPRoSPortElementBaseModel> list = new ArrayList<OPRoSPortElementBaseModel>();
		for(OPRoSElementBaseModel element : getChildrenList()) {
			if(element instanceof OPRoSPortElementBaseModel) {
				list.add((OPRoSPortElementBaseModel)element);
			}
		}
		return list;
	}
	
	public ArrayList<MonitoringVariableModel> getAddMonitoringList() {
		for(MonitoringVariableModel model : getChangeMonitoringList()) {
			boolean isDuplicate = false;
			for(MonitoringVariableModel model2 : addMonitoringList) {
				if(model == model2) {
					isDuplicate = true;
				}
			}
			if(!isDuplicate) {
				addMonitoringList.add(model);
			}
		}
		return addMonitoringList;
	}

	public void setAddMonitoringList(
			ArrayList<MonitoringVariableModel> addMonitoringList) {
		this.addMonitoringList = addMonitoringList;
	}

	public ArrayList<MonitoringVariableModel> getDelMonitoringList() {
		for(MonitoringVariableModel model : getChangeMonitoringList()) {
			boolean isDuplicate = false;
			for(MonitoringVariableModel model2 : delMonitoringList) {
				if(model == model2) {
					isDuplicate = true;
				}
			}
			if(!isDuplicate) {
				delMonitoringList.add(model);
			}
		}
		return delMonitoringList;
	}

	public void setDelMonitoringList(
			ArrayList<MonitoringVariableModel> delMonitoringList) {
		this.delMonitoringList = delMonitoringList;
	}

	public ArrayList<MonitoringVariableModel> getChangeMonitoringList() {
		return changeMonitoringList;
	}

	public void setChangeMonitoringList(
			ArrayList<MonitoringVariableModel> changeMonitoringList) {
		this.changeMonitoringList = changeMonitoringList;
	}
	
	public boolean addAddAddMonitoring(MonitoringVariableModel model) {
		if(!addMonitoringList.contains(model)) {
			addMonitoringList.add(model);
			return true;
		}
		return false;
	}
	
	public boolean removeAddAddMonitoring(MonitoringVariableModel model) {
		if(addMonitoringList.contains(model)) {
			addMonitoringList.remove(model);
			return true;
		}
		return false;
	}
	
	public boolean addDelMonitoring(MonitoringVariableModel model) {
		if(!delMonitoringList.contains(model)) {
			delMonitoringList.add(model);
			return true;
		}
		return false;
	}
	
	public boolean removeDelMonitoring(MonitoringVariableModel model) {
		if(delMonitoringList.contains(model)) {
			delMonitoringList.remove(model);
			return true;
		}
		return false;
	}
	
	public boolean addChangeMonitoring(MonitoringVariableModel model) {
		if(!changeMonitoringList.contains(model)) {
			changeMonitoringList.add(model);
			return true;
		}
		return false;
	}
	
	public boolean removeChangeMonitoring(MonitoringVariableModel model) {
		if(changeMonitoringList.contains(model)) {
			changeMonitoringList.remove(model);
			return true;
		}
		return false;
	}
	
}
