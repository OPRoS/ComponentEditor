package kr.co.ed.opros.ce.guieditor.model;

import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.jdom.Element;

public class OPRoSPropertiesElementModel extends OPRoSElementBaseModel {
	public OPRoSPropertiesElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
//		super.initializeData();
		setLayout(new Rectangle(170,130,70,30));
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.lightBlue);
		setBorder(1);
	}
	@SuppressWarnings("unchecked")
	public void doLoad(Element parentEle){
		List<Element> list = parentEle.getChildren();
		if(list!=null){
			Iterator<Element> it = list.iterator();
			while(it.hasNext()){
				OPRoSPropertyElementModel prop = new OPRoSPropertyElementModel();
				prop.doLoad(it.next());
				addChild(prop);
			}
		}
	}
	public void doSave(Element parentEle){
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		while(it.hasNext()){
			OPRoSPropertyElementModel model = (OPRoSPropertyElementModel)it.next();
			Element ele = new Element("property");
			model.doSave(ele);
			parentEle.addContent(ele);
		}
	}
	
	@Override
	public boolean addChild(OPRoSElementBaseModel child) {
		String name;
		String childName;
		OPRoSElementBaseModel model;
		childName = ((OPRoSPropertyElementModel)child).getName();
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		while(it.hasNext()){
			model = it.next();
			name = ((OPRoSPropertyElementModel)model).getName();
			if(name.compareTo(childName)==0){
				OPRoSUtil.openMessageBox("Property is duplicate", SWT.ERROR);
				return false;
			}
		}
		return super.addChild(child);
	}
}
