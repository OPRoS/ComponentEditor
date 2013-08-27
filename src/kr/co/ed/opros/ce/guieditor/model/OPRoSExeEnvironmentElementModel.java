package kr.co.ed.opros.ce.guieditor.model;

import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.jdom.Element;

public class OPRoSExeEnvironmentElementModel extends OPRoSElementBaseModel {
	public static final String PROPERTY_DOMAIN = "Execution Environment Domain";
	public static final String PROPERTY_ROBOT_TYPE = "Execution Environment Robot Type";
	
	public static final String PROPERTY_LIBRARY_NAME = "Library Name";
	public static final String PROPERTY_LIBRARY_TYPE = "Library Type";
	public static final String PROPERTY_IMPL_LANGUAGE = "Implemention Language";
	public static final String PROPERTY_COMPILER = "Compiler";
	
	private String libraryName;
	private String libraryType;
	private String implLang;
	private String compiler;
	
	private String domain;
	private String robotType;
	
	
	public OPRoSExeEnvironmentElementModel(){
		super();
		initializeData();
	}
	public void initializeData(){
//		super.initializeData();
		
		if(getParent()!=null) {
			String libType = OPRoSUtil.getLibType();
			if(libType.equals("so")) {
				libraryName = "lib"+((OPRoSComponentElementModel)getParent()).getComponentName()+"."+libType;
			} else {
				libraryName = ((OPRoSComponentElementModel)getParent()).getComponentName()+"."+libType;
			}			
		}
		else
			libraryName =OPRoSStrings.getString("CompInfoLibNameDefault");
		//libraryType = OPRoSStrings.getString("CompInfoLibTypeDefault");
		libraryType = OPRoSUtil.getLibType();
		implLang = OPRoSStrings.getString("CompInfoImplLLangDefault");
		compiler = OPRoSStrings.getString("CompInfoCompilerDefault");
		
		domain = OPRoSStrings.getString("ExeEnvEleDomainDefault");
		robotType = OPRoSStrings.getString("ExeEnvEleRobotTypeDefault");
		
		setLayout(new Rectangle(90,130,70,30));
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.lightBlue);
		setBorder(1);
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		String oldValue = this.getDomain();
		this.domain = domain;
		getListeners().firePropertyChange(PROPERTY_DOMAIN, oldValue, domain);
		
	}
	public String getRobotType() {
		return robotType;
	}
	public void setRobotType(String robotType) {
		String oldValue = this.getRobotType();
		this.robotType = robotType;
		getListeners().firePropertyChange(PROPERTY_ROBOT_TYPE, oldValue, robotType);
	}
	public String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		if(libraryName.compareTo(getLibraryName())==0)
			return;
		String oldValue = this.getLibraryName();
		this.libraryName = libraryName;
		int extensionIndex = libraryName.lastIndexOf(".");
		String libType = libraryName.substring(extensionIndex+1);
		if(libType.compareTo(getLibraryType())!=0){
			setLibraryType(libType);
		}
		getListeners().firePropertyChange(PROPERTY_LIBRARY_NAME, oldValue, libraryName);
	}
	public String getLibraryType() {
		return libraryType;
	}
	public void setLibraryType(String libraryType) {
		if(libraryType.compareTo(getLibraryType())==0)
			return;
		String oldValue = getLibraryType();
		this.libraryType = libraryType;
		int extensionIndex = libraryName.lastIndexOf(".");
		String libType = libraryName.substring(extensionIndex+1);
		if(libType.compareTo(libraryType)!=0){
			libType = libraryName.substring(0, extensionIndex)+"."+libraryType;
			setLibraryName(libType);
		}
		getListeners().firePropertyChange(PROPERTY_LIBRARY_TYPE, oldValue, libraryType);
	}
	public String getImplLang() {
		return implLang;
	}
	public void setImplLang(String implLang) {
		String oldValue = this.getImplLang();
		this.implLang = implLang;
		getListeners().firePropertyChange(PROPERTY_IMPL_LANGUAGE, oldValue, implLang);
	}
	public String getCompiler() {
		return compiler;
	}
	public void setCompiler(String compiler) {
		String oldValue = this.getCompiler();
		this.compiler = compiler;
		getListeners().firePropertyChange(PROPERTY_COMPILER, oldValue, compiler);
	}
	
	@SuppressWarnings("unchecked")
	public void doLoad(Element parentEle){
		String tempStr;
		List<Element> list = parentEle.getChildren("os");
		if(list!=null){
			Iterator<Element> it = list.iterator();
			while(it.hasNext()){
				OPRoSExeEnvironmentOSElementModel osModel = new OPRoSExeEnvironmentOSElementModel();
				osModel.doLoad(it.next());
				this.addChild(osModel);
			}
		}
		list = parentEle.getChildren("cpu");
		if(list!=null){
			Iterator<Element> it = list.iterator();
			while(it.hasNext()){
				OPRoSExeEnvironmentCPUElementModel cpuModel = new OPRoSExeEnvironmentCPUElementModel();
				cpuModel.doLoad(it.next());
				this.addChild(cpuModel);
			}
		}
		//execution environment
		tempStr = parentEle.getChildTextTrim("library_type");
		if(tempStr!=null)
			setLibraryType(tempStr);
		tempStr = parentEle.getChildTextTrim("library_name");
		if(tempStr!=null)
			setLibraryName(tempStr);
		tempStr = parentEle.getChildTextTrim("impl_language");
		if(tempStr!=null)
			setImplLang(tempStr);
		tempStr = parentEle.getChildTextTrim("compiler");
		if(tempStr!=null)
			setCompiler(tempStr);
		tempStr=parentEle.getChildTextTrim("app_domain");
		if(tempStr!=null)
			setDomain(tempStr);
		tempStr=parentEle.getChildTextTrim("app_robot");
		if(tempStr!=null)
			setRobotType(tempStr);
	}
	
	public void doSave(Element parentEle){
		Element ele;
		ele = new Element("library_type");
		ele.setText(getLibraryType());
		parentEle.addContent(ele);
		ele = new Element("library_name");
		ele.setText(getLibraryName());
		parentEle.addContent(ele);
		ele = new Element("impl_language");
		ele.setText(getImplLang());
		parentEle.addContent(ele);
		ele = new Element("compiler");
		ele.setText(getCompiler());
		parentEle.addContent(ele);
		ele = new Element("app_domain");
		ele.setText(getDomain());
		parentEle.addContent(ele);
		ele = new Element("app_robot");
		ele.setText(getRobotType());
		parentEle.addContent(ele);
	}
	
	@Override
	public boolean addChild(OPRoSElementBaseModel child) {
		String name;
		String childName;
		OPRoSElementBaseModel model;
		if(child instanceof OPRoSExeEnvironmentOSElementModel){
			childName = ((OPRoSExeEnvironmentOSElementModel)child).getOSName();
			String childVersion=((OPRoSExeEnvironmentOSElementModel)child).getOSVersion();
			String version;
			Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
			while(it.hasNext()){
				model = it.next();
				if(model instanceof OPRoSExeEnvironmentOSElementModel){
					name = ((OPRoSExeEnvironmentOSElementModel)model).getOSName();
					if(name.compareTo(childName)==0){
						version = ((OPRoSExeEnvironmentOSElementModel)model).getOSVersion();
						if(version.compareTo(childVersion)==0){
							OPRoSUtil.openMessageBox("OS Name & Version is duplicate", SWT.ERROR);
							return false;
						}
					}
				}
			}
		}else{
			childName = ((OPRoSExeEnvironmentCPUElementModel)child).getCPUName();
			Iterator<OPRoSElementBaseModel> it = getChildrenList().iterator();
			while(it.hasNext()){
				model = it.next();
				if(model instanceof OPRoSExeEnvironmentCPUElementModel){
					name = ((OPRoSExeEnvironmentCPUElementModel)model).getCPUName();
					if(name.compareTo(childName)==0){
						OPRoSUtil.openMessageBox("CPU Name is duplicate", SWT.ERROR);
							return false;
					}
				}
			}
		}
		return super.addChild(child);
	}
}
