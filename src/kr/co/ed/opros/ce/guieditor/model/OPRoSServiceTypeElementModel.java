package kr.co.ed.opros.ce.guieditor.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jd.xml.xslt.Stylesheet;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class OPRoSServiceTypeElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_SERVICE_TYPE_FILENAME = "Service Type File Name";
	private String serviceTypeFileName;
	private Document serviceTypeDoc;
	private Document oldServiceTypeDoc;	
	
	public OPRoSServiceTypeElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
//		super.initializeData();
		serviceTypeFileName=OPRoSStrings.getString("ServiceTypeEleFileNameDefault");
		serviceTypeDoc = null;
	}
	public String getServiceTypeFileName() {
		return serviceTypeFileName;
	}
	public void setServiceTypeFileName(String serviceTypeFileName) {
		String oldValue = this.getServiceTypeFileName();
		this.serviceTypeFileName = serviceTypeFileName;
		getListeners().firePropertyChange(PROPERTY_SERVICE_TYPE_FILENAME, oldValue, serviceTypeFileName);
	}
	
	public Document getServiceTypeDoc() {
		return serviceTypeDoc;
	}
	
	public void setServiceTypeDoc(Document serviceTypeDoc) {
		this.serviceTypeDoc = serviceTypeDoc;
	}
	
	public void changeServiceTypeDoc(Document serviceTypeDoc) {
		if(oldServiceTypeDoc == null) {
			this.oldServiceTypeDoc = getServiceTypeDoc();
		}
		setServiceTypeDoc(serviceTypeDoc);
	}
	
	public Document getOldServiceTypeDoc() {
		return oldServiceTypeDoc;
	}
	
	public void setOldServiceTypeDoc(Document doc) {
		this.oldServiceTypeDoc = doc;
	}
	
	public void doSave(Element parentEle,XMLOutputter opt,String profileDirPath){
		String outFileName;
		String referPath=OPRoSUtil.getOPRoSFilesPath();
		try {
			opt.output(getServiceTypeDoc(), new FileOutputStream(profileDirPath+"/"+getServiceTypeFileName()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int index = getServiceTypeFileName().lastIndexOf(".");
		outFileName = getServiceTypeFileName().substring(0, index);
		String[] params = {
				"-param",
				"filename",
				"'"+profileDirPath+"/"+outFileName+"'",
				"-param",
				"outpath",
				"'"+profileDirPath.substring(0,profileDirPath.lastIndexOf("/"))+"/'",
				referPath+"/OPRoSFiles/GenerateProfiles2011.xsl",
				referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};
		Stylesheet.main(params);
		Element ele=new Element("defined_service_type");
		ele.setText(getServiceTypeFileName());
		parentEle.addContent(ele);
	}
}
