package kr.co.ed.opros.ce.guieditor.model;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * 모니터링을 변수들을 담는 변수
 * @author hclee
 *
 */
public class MonitoringVariablesModel extends OPRoSElementBaseModel {
	
	public void doLoad(Element parentEle){
		List<Element> list = parentEle.getChildren();
		if(list!=null){
			Iterator<Element> it = list.iterator();
			while(it.hasNext()){
				MonitoringVariableModel prop = new MonitoringVariableModel();
				prop.doLoad(it.next());
				addChild(prop);
			}
		}
	}
	
	public void doSave(Element parentEle){
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		while(it.hasNext()){
			MonitoringVariableModel model = (MonitoringVariableModel)it.next();
			Element ele = new Element("var");
			model.doSave(ele);
			parentEle.addContent(ele);
		}
	}

	@Override
	public boolean addChild(OPRoSElementBaseModel child) {
		boolean isOk = super.addChild(child);
		((OPRoSBodyElementModel)getParent().getParent()).addAddAddMonitoring((MonitoringVariableModel)child);
		((OPRoSBodyElementModel)getParent().getParent()).removeDelMonitoring((MonitoringVariableModel)child);
		return isOk;
	}

	@Override
	public boolean addChild(OPRoSElementBaseModel child, int index) {
		boolean isOk = super.addChild(child, index);
		((OPRoSBodyElementModel)getParent().getParent()).addAddAddMonitoring((MonitoringVariableModel)child);
		((OPRoSBodyElementModel)getParent().getParent()).removeDelMonitoring((MonitoringVariableModel)child);
		return isOk;
	}

	@Override
	public boolean removeChild(OPRoSElementBaseModel child) {
		boolean isOk = super.removeChild(child);
		((OPRoSBodyElementModel)getParent().getParent()).removeAddAddMonitoring((MonitoringVariableModel)child);
		((OPRoSBodyElementModel)getParent().getParent()).removeChangeMonitoring((MonitoringVariableModel)child);
		((OPRoSBodyElementModel)getParent().getParent()).addDelMonitoring((MonitoringVariableModel)child);
		return isOk;
	}
	
	
}
