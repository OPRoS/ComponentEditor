package kr.co.ed.opros.ce.guieditor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

//import org.eclipse.core.runtime.Preferences;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.opros.mainpreference.Activator;
import org.opros.mainpreference.preferences.PreferenceConstants;

public class OPRoSComponentElementModel extends OPRoSElementBaseModel {
	
	public static final String PROPERTY_COMP_NAME = "Component Name";
	public static final String PROPERTY_COMP_DESCRIPTION = "Component Description";
	public static final String PROPERTY_COMP_VERSION = "Component Version";
	public static final String PROPERTY_COMP_ID = "Component ID";
	public static final String PROPERTY_COMPANY_NAME = "Company Name";
	public static final String PROPERTY_COMPANY_PHONE = "Company Phone";
	public static final String PROPERTY_COMPANY_ADDRESS = "Company Address";
	public static final String PROPERTY_COMPANY_HOMEPAGE = "Company Homepage";
	public static final String PROPERTY_LICENSE_POLICY = "License Policy";

	private String componentName;
	private String componentDescription;
	private String version;
	private String componentID;
	private String companyName;
	private String companyPhone;
	private String companyAddress;
	private String companyHomepage;
	private String licensePolicy;
	private ArrayList<String> dataTypeReference;
	
	private OPRoSExeEnvironmentElementModel exeEnv;
	private OPRoSExeSemanticsElementModel exeSemantics;
	private OPRoSPropertiesElementModel properties;
	private OPRoSDataTypesElementModel dataTypes;
	private OPRoSServiceTypesElementModel serviceTypes;
	private MonitoringVariablesModel monitoringVariables;

	public OPRoSComponentElementModel(){
		super();
		initializeData();
		dataTypeReference = new ArrayList<String>();
	}
	
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		String oldValue = this.getComponentName();
		this.componentName = componentName;
		getListeners().firePropertyChange(PROPERTY_COMP_NAME, oldValue, componentName);
	}
	public String getComponentDescription() {
		return componentDescription;
	}
	public void setComponentDescription(String componentDescription) {
		String oldValue = this.getComponentDescription();
		this.componentDescription = componentDescription;
		getListeners().firePropertyChange(PROPERTY_COMP_DESCRIPTION, oldValue, componentDescription);
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		String oldValue = this.getVersion();
		this.version = version;
		getListeners().firePropertyChange(PROPERTY_COMP_VERSION, oldValue, version);
	}
	public String getComponentID() {
		return componentID;
	}
	public void setComponentID(String componentID) {
		String oldValue = this.getComponentID();
		this.componentID = componentID;
		getListeners().firePropertyChange(PROPERTY_COMP_ID, oldValue, componentID);
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		String oldValue = this.getCompanyName();
		this.companyName = companyName;
		getListeners().firePropertyChange(PROPERTY_COMPANY_NAME, oldValue, companyName);
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		String oldValue = this.getCompanyPhone();
		this.companyPhone = companyPhone;
		getListeners().firePropertyChange(PROPERTY_COMPANY_PHONE, oldValue, companyPhone);
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		String oldValue = this.getCompanyAddress();
		this.companyAddress = companyAddress;
		getListeners().firePropertyChange(PROPERTY_COMPANY_ADDRESS, oldValue, companyAddress);
	}
	public String getCompanyHomepage() {
		return companyHomepage;
	}
	public void setCompanyHomepage(String companyHomepage) {
		String oldValue = this.getCompanyHomepage();
		this.companyHomepage = companyHomepage;
		getListeners().firePropertyChange(PROPERTY_COMPANY_HOMEPAGE, oldValue, companyHomepage);
	}
	public String getLicensePolicy() {
		return licensePolicy;
	}
	public void setLicensePolicy(String licensePolicy) {
		String oldValue = this.getLicensePolicy();
		this.licensePolicy = licensePolicy;
		getListeners().firePropertyChange(PROPERTY_LICENSE_POLICY, oldValue, licensePolicy);
	}

	public void initializeData(){
		exeEnv = new OPRoSExeEnvironmentElementModel();
		properties = new OPRoSPropertiesElementModel();
		dataTypes = new OPRoSDataTypesElementModel();
		serviceTypes = new OPRoSServiceTypesElementModel();
		exeSemantics = new OPRoSExeSemanticsElementModel();
		monitoringVariables = new MonitoringVariablesModel();
		
		addChild(exeSemantics);
		addChild(exeEnv);
		addChild(properties);
		addChild(dataTypes);
		addChild(serviceTypes);
		addChild(monitoringVariables);
		
		setComponentName(OPRoSStrings.getString("CompEleNameDefault"));
		setComponentDescription(OPRoSStrings.getString("CompEleDescDefault"));
		setComponentID(componentID=UUID.randomUUID().toString());
		IPreferenceStore pref = Activator.getDefault().getPreferenceStore();
		
		setVersion(pref.getString(PreferenceConstants.COPYRIGHT_VERSION));
		setCompanyName(pref.getString(PreferenceConstants.COPYRIGHT_NAME));
		setCompanyPhone(pref.getString(PreferenceConstants.COPYRIGHT_PHONE));
		setCompanyAddress(pref.getString(PreferenceConstants.COPYRIGHT_ADDRESS));
		setCompanyHomepage(pref.getString(PreferenceConstants.COPYRIGHT_HOMEPAGE));		
		setLicensePolicy(pref.getString(PreferenceConstants.COPYRIGHT_LICENSE));
		setLayout(new Rectangle(100,150,250,200));
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(new Color(null,251,176,59));
		setBorder(3);
	}
	
	public OPRoSElementBaseModel getExeEnvironmentModel(){
		return exeEnv;
	}
	
	public OPRoSElementBaseModel getExeSemanticsModel(){
		return exeSemantics;
	}
	
	public OPRoSElementBaseModel getPropertiesModel(){
		return properties;
	}
	
	public OPRoSElementBaseModel getDataTypesModel(){
		return dataTypes;
	}
	
	public OPRoSElementBaseModel getServiceTypesModel(){
		return serviceTypes;
	}
	
	public Document getServiceTypeDoc(String fileName){
		Iterator<OPRoSElementBaseModel> it = serviceTypes.getChildrenList().iterator();
		OPRoSServiceTypeElementModel model;
		while(it.hasNext()){
			model = (OPRoSServiceTypeElementModel)it.next();
			if(fileName.compareTo(model.getServiceTypeFileName())==0){
				return model.getServiceTypeDoc();
			}
		}
		return null;
	}
	
	public Document getDataTypeDoc(String fileName){
		Iterator<OPRoSElementBaseModel> it = dataTypes.getChildrenList().iterator();
		OPRoSDataTypeElementModel model;
		while(it.hasNext()){
			model = (OPRoSDataTypeElementModel)it.next();
			if(fileName.compareTo(model.getDataTypeFileName())==0){
				return model.getDataTypeDoc();
			}
		}
		return null;
	}
	
	public MonitoringVariablesModel getMonitoringVariablesModel() {
		return monitoringVariables;
	}
	
	public void addDataTypeReference(String dataTypeFile){
		if(dataTypeReference.contains(dataTypeFile))
			return;
		dataTypeReference.add(dataTypeFile);
	}
	
	public Iterator<String> getDataTypeReferenceList(){
		return dataTypeReference.iterator();
	}
	
	public int getDataTypeReferenceIndex(String dataTypeFile){
		return dataTypeReference.indexOf(dataTypeFile);
	}
	
	public String getDataTypeReferenceByIndex(int n){
		return dataTypeReference.get(n);
	}
	
	@SuppressWarnings("unchecked")
	public void doLoad(Element parentEle,String profileDirPath){
		String tempStr;
		Element tempEle;
		tempStr=parentEle.getChildTextTrim("id");
		if(tempStr!=null)
			setComponentID(tempStr);
		tempStr=parentEle.getChildTextTrim("name");
		if(tempStr!=null)
			setComponentName(tempStr);
		tempStr = parentEle.getChildTextTrim("version");
		if(tempStr!=null)
			setVersion(tempStr);
		tempStr = parentEle.getChildTextTrim("description");
		if(tempStr!=null)
			setComponentDescription(tempStr);
		
		tempStr = parentEle.getChildTextTrim("layout");
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
			setLayout(rect);
		}
		
		tempStr = parentEle.getChildTextTrim("collapsed");
		if(tempStr!=null){
			setCollapsed(Integer.parseInt(tempStr)!=0);
		}
		
		tempEle = parentEle.getChild("copyright");
		if(tempEle!=null){
			Element tempSubEle;
			tempSubEle = tempEle.getChild("company");
			if(tempSubEle!=null){
				tempStr=tempSubEle.getChildTextTrim("name");
				if(tempStr!=null)
					setCompanyName(tempStr);
				tempStr=tempSubEle.getChildTextTrim("phone");
				if(tempStr!=null)
					setCompanyPhone(tempStr);
				tempStr=tempSubEle.getChildTextTrim("address");
				if(tempStr!=null)
					setCompanyAddress(tempStr);
				tempStr=tempSubEle.getChildTextTrim("homepage");
				if(tempStr!=null)
					setCompanyHomepage(tempStr);
			}
			tempStr=tempEle.getChildTextTrim("license_policy");
			if(tempStr!=null)
				setLicensePolicy(tempStr);
		}
		tempEle = parentEle.getChild("execution_environment");
		if(tempEle!=null){
			exeEnv.doLoad(tempEle);
//			spec.doLoad(tempEle);
		}
		tempEle = parentEle.getChild("execution_semantics");
		if(tempEle!=null){
			exeSemantics.doLoad(tempEle);
		}
		tempEle = parentEle.getChild("properties");
		if(tempEle!=null){
			properties.doLoad(tempEle);
		}
		tempEle = parentEle.getChild("data_type_list");
		if(tempEle!=null){
			List<Element> list = tempEle.getChildren();
			if(list!=null){
				Iterator<Element> it = list.iterator();
				while(it.hasNext()){
					addDataTypeReference(it.next().getAttributeValue("name"));
				}
			}
		}
		tempEle = parentEle.getChild("defined_data_types");
		if(tempEle!=null){
			dataTypes.doLoad(tempEle,profileDirPath);
		}
		tempEle = parentEle.getChild("defined_service_types");
		if(tempEle!=null){
			serviceTypes.doLoad(tempEle,profileDirPath);
		}		
		tempEle = parentEle.getChild("exports");
		if(tempEle!=null){
			monitoringVariables.doLoad(tempEle);
		}
	}
	
	public void doSave(Element parentEle, String profileDirPath){
		Element ele;
		Element subEle;
		ele = new Element("id");
		ele.setText(getComponentID());
		parentEle.addContent(ele);
		
		ele=new Element("name");
		ele.setText(getComponentName());
		parentEle.addContent(ele);
		
		if(!getVersion().isEmpty()){
			ele = new Element("version");
			ele.setText(getVersion());
			parentEle.addContent(ele);
		}
		if(!getComponentDescription().isEmpty()){
			ele = new Element("description");
			ele.setText(getComponentDescription());
			parentEle.addContent(ele);
		}
		ele = new Element("layout");
		ele.setText(Integer.toString(getLayout().x)+","+Integer.toString(getLayout().y)
				+","+Integer.toString(getLayout().width)+","+Integer.toString(getLayout().height));
		parentEle.addContent(ele);
		
		ele = new Element("collapsed");
		ele.setText(Integer.toString(isCollapsed()?1:0));
		parentEle.addContent(ele);
		
		ele = new Element("copyright");
		parentEle.addContent(ele);
		Element companyEle = new Element("company");
		ele.addContent(companyEle);
		subEle = new Element("name");
		subEle.setText(getCompanyName());
		companyEle.addContent(subEle);
		subEle = new Element("phone");
		subEle.setText(getCompanyPhone());
		companyEle.addContent(subEle);
		subEle = new Element("address");
		subEle.setText(getCompanyAddress());
		companyEle.addContent(subEle);
		subEle = new Element("homepage");
		subEle.setText(getCompanyHomepage());
		companyEle.addContent(subEle);
		subEle = new Element("license_policy");
		subEle.setText(getLicensePolicy());
		ele.addContent(subEle);
		
		ele = new Element("execution_environment");
		parentEle.addContent(ele);
		Iterator<OPRoSElementBaseModel> it = exeEnv.getChildrenList().iterator();
		while(it.hasNext()){
			OPRoSElementBaseModel model = (OPRoSElementBaseModel)it.next();
			if(model instanceof OPRoSExeEnvironmentOSElementModel){
				subEle = new Element("os");
				Attribute attr = new Attribute("name",((OPRoSExeEnvironmentOSElementModel)model).getOSName());
				Attribute version = new Attribute("version",((OPRoSExeEnvironmentOSElementModel)model).getOSVersion());
				subEle.setAttribute(attr);
				subEle.setAttribute(version);
				ele.addContent(subEle);
			}
		}
		it = exeEnv.getChildrenList().iterator();
		while(it.hasNext()){
			OPRoSElementBaseModel model = (OPRoSElementBaseModel)it.next();
			if(model instanceof OPRoSExeEnvironmentCPUElementModel){
				subEle = new Element("cpu");
				subEle.setText(((OPRoSExeEnvironmentCPUElementModel)model).getCPUName());
				ele.addContent(subEle);
			}
		}
		
		exeEnv.doSave(ele);
		
		ele = new Element("execution_semantics");
		parentEle.addContent(ele);
		exeSemantics.doSave(ele);
		
		ele = new Element("properties");
		parentEle.addContent(ele);
		properties.doSave(ele);
		
		ele = new Element("data_type_list");
		Iterator<String> usingDTIt = getDataTypeReferenceList();
		while(usingDTIt.hasNext()){
			subEle = new Element("reference");
			Attribute attr = new Attribute("name",usingDTIt.next());
			subEle.setAttribute(attr);
			ele.addContent(subEle);
		}
		parentEle.addContent(ele);
		
		ele = new Element("defined_data_types");
		dataTypes.doSave(ele,profileDirPath);
		parentEle.addContent(ele);
		
		ele = new Element("defined_service_types");
		serviceTypes.doSave(ele,profileDirPath);
		parentEle.addContent(ele);
		
		ele = new Element("exports");
		monitoringVariables.doSave(ele);
		parentEle.addContent(ele);
	}

}
