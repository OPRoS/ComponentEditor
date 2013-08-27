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

public class OPRoSDataTypeElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_DATA_TYPE_FILENAME = "Data Type File Name";
	private String dataTypeFileName;
	private Document dataTypeDoc;
	
	public OPRoSDataTypeElementModel(){
		super();
		initializeData();
	}
	
	public void initializeData(){
//		super.initializeData();
		dataTypeFileName=OPRoSStrings.getString("DataTypeEleFileNameDefault");
		dataTypeDoc = null;
	}
	
	public String getDataTypeFileName() {
		return dataTypeFileName;
	}
	
	public void setDataTypeFileName(String dataTypeFileName) {
		String oldValue = this.getDataTypeFileName();
		this.dataTypeFileName = dataTypeFileName;
		getListeners().firePropertyChange(PROPERTY_DATA_TYPE_FILENAME, oldValue, dataTypeFileName);
	}
	
	public Document getDataTypeDoc() {
		return dataTypeDoc;
	}
	
	public void setDataTypeDoc(Document dataTypeDoc) {
		this.dataTypeDoc = dataTypeDoc;
	}
	
	public void doSave(Element parentEle, XMLOutputter opt, String profileDirPath){
		String outFileName;
		String referPath=OPRoSUtil.getOPRoSFilesPath();
		try {
			opt.output(getDataTypeDoc(), new FileOutputStream(profileDirPath+"/"+getDataTypeFileName()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int index = getDataTypeFileName().lastIndexOf(".");
		outFileName = getDataTypeFileName().substring(0, index);
		String[] params = {
				"-param","filename","'"+profileDirPath+"/"+outFileName+"'",
				"-param","outpath","'"+profileDirPath.substring(0,profileDirPath.lastIndexOf("/"))+"/'",
				referPath+"/OPRoSFiles/GenerateProfiles2011.xsl",referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};
		Stylesheet.main(params);
		Element ele=new Element("defined_data_type");
		ele.setText(getDataTypeFileName());
		parentEle.addContent(ele);
	}
	
}
