package kr.co.ed.opros.ce.guieditor.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OPRoSServiceTypesElementModel extends OPRoSElementBaseModel {
	public OPRoSServiceTypesElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
//		super.initializeData();
		setLayout(new Rectangle(0,0,100,70));
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.lightBlue);
		setBorder(1);
	}
	
	@SuppressWarnings("unchecked")
	public void doLoad(Element parentEle,String profileDirPath){
		List<Element> list = parentEle.getChildren();
		if(list!=null){
			Iterator<Element> it = list.iterator();
			String filePath;
			Document serviceTypeDoc=null;
			FileInputStream serviceTypeInputStream=null;
			while(it.hasNext()){
				OPRoSServiceTypeElementModel model = new OPRoSServiceTypeElementModel();
				model.setServiceTypeFileName(it.next().getText());
				if(!model.getServiceTypeFileName().isEmpty()){
					filePath = profileDirPath+"/"+model.getServiceTypeFileName();
					try {
						serviceTypeInputStream = new FileInputStream(filePath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					SAXBuilder builder = new SAXBuilder();
					try {
						serviceTypeDoc = builder.build( serviceTypeInputStream );
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(serviceTypeDoc!=null){
						model.setServiceTypeDoc(serviceTypeDoc);
					}else{
						OPRoSUtil.openMessageBox("Data Doc is NULL", SWT.OK);
					}
					if(serviceTypeInputStream!=null)
						try {
							serviceTypeInputStream.close();
							addChild(model);
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}
	
	public void doSave(Element parentEle,String profileDirPath){
		XMLOutputter opt = new XMLOutputter();
		Format form = opt.getFormat();
		form.setEncoding("euc-kr");
		form.setLineSeparator("\r\n");
		form.setIndent("	");
		form.setTextMode(Format.TextMode.TRIM);
		opt.setFormat(form);
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		while(it.hasNext()){
			OPRoSServiceTypeElementModel model = (OPRoSServiceTypeElementModel)it.next();
			model.doSave(parentEle,opt,profileDirPath);
		}
	}
	
	@Override
	public boolean addChild(OPRoSElementBaseModel child) {
		String name;
		String childName=((OPRoSServiceTypeElementModel)child).getServiceTypeFileName();
		Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
		while(it.hasNext()){
			name = ((OPRoSServiceTypeElementModel)it.next()).getServiceTypeFileName();
			if(name.compareTo(childName)==0){
				OPRoSUtil.openMessageBox("ServiceType File Name is duplicate", SWT.ERROR);
				return false;
			}
		}
		return super.addChild(child);
	}
}
