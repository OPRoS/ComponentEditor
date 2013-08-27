package kr.co.ed.opros.ce.guieditor.PropertyView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeSemanticsElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jdom.Document;
import org.jdom.Element;

public class OPRoSElementPropertySource implements IPropertySource {

	private OPRoSElementBaseModel element;
	
	public OPRoSElementPropertySource(OPRoSElementBaseModel element){
		this.element = element;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		if(element instanceof OPRoSComponentElementModel){
			OPRoSUnEditableTextPropertyDescriptor componentNameDescriptor = 
				new OPRoSUnEditableTextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMP_NAME, OPRoSComponentElementModel.PROPERTY_COMP_NAME);
			componentNameDescriptor.setCategory(OPRoSStrings.getString("PropertyCompCategory"));
			properties.add(componentNameDescriptor);
			PropertyDescriptor componentDescriptionDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMP_DESCRIPTION, OPRoSComponentElementModel.PROPERTY_COMP_DESCRIPTION);
			componentDescriptionDescriptor.setCategory(OPRoSStrings.getString("PropertyCompCategory"));
			properties.add(componentDescriptionDescriptor);
			PropertyDescriptor componentVersionDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMP_VERSION, OPRoSComponentElementModel.PROPERTY_COMP_VERSION);
			componentVersionDescriptor.setCategory(OPRoSStrings.getString("PropertyCompCategory"));
			properties.add(componentVersionDescriptor);
			PropertyDescriptor componentIDDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMP_ID, OPRoSComponentElementModel.PROPERTY_COMP_ID);
			componentIDDescriptor.setCategory(OPRoSStrings.getString("PropertyCompCategory"));
			properties.add(componentIDDescriptor);
			PropertyDescriptor companyNameDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMPANY_NAME, OPRoSComponentElementModel.PROPERTY_COMPANY_NAME);
			companyNameDescriptor.setCategory(OPRoSStrings.getString("PropertyCopyRightCategory"));
			properties.add(companyNameDescriptor);
			PropertyDescriptor companyPhoneDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMPANY_PHONE, OPRoSComponentElementModel.PROPERTY_COMPANY_PHONE);
			companyPhoneDescriptor.setCategory(OPRoSStrings.getString("PropertyCopyRightCategory"));
			properties.add(companyPhoneDescriptor);
			PropertyDescriptor companyAddressDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMPANY_ADDRESS, OPRoSComponentElementModel.PROPERTY_COMPANY_ADDRESS);
			companyAddressDescriptor.setCategory(OPRoSStrings.getString("PropertyCopyRightCategory"));
			properties.add(companyAddressDescriptor);
			PropertyDescriptor companyHomepageDescriptor = 
				new TextPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_COMPANY_HOMEPAGE, OPRoSComponentElementModel.PROPERTY_COMPANY_HOMEPAGE);
			companyHomepageDescriptor.setCategory(OPRoSStrings.getString("PropertyCopyRightCategory"));
			properties.add(companyHomepageDescriptor);
			String[] licensePolicyDatas = {
					OPRoSStrings.getString("PropertyCopyRightLicenceCombo0"),
					OPRoSStrings.getString("PropertyCopyRightLicenceCombo1"),
					OPRoSStrings.getString("PropertyCopyRightLicenceCombo2"),
					OPRoSStrings.getString("PropertyCopyRightLicenceCombo3"),
					OPRoSStrings.getString("PropertyCopyRightLicenceCombo4")};
			ComboBoxPropertyDescriptor licensePolicyDescriptor = 
				new ComboBoxPropertyDescriptor(OPRoSComponentElementModel.PROPERTY_LICENSE_POLICY, OPRoSComponentElementModel.PROPERTY_LICENSE_POLICY,licensePolicyDatas);
			licensePolicyDescriptor.setCategory(OPRoSStrings.getString("PropertyCopyRightCategory"));
			properties.add(licensePolicyDescriptor);
//			makeComponentInfoPropertiesView(properties);
			makeExeEnvPropertiesView(properties);
			makeExeSemanticsPropertiesView(properties);
//		}else if(element instanceof OPRoSComponentSpecificationElementModel){
//			makeComponentInfoPropertiesView(properties);
		}else if(element instanceof OPRoSExeEnvironmentElementModel){
			makeExeEnvPropertiesView(properties);
		}else if(element instanceof OPRoSExeSemanticsElementModel){
			makeExeSemanticsPropertiesView(properties);
		}else if(element instanceof OPRoSPortElementBaseModel){
			PropertyDescriptor portNameDescriptor = 
				new TextPropertyDescriptor(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME, OPRoSPortElementBaseModel.PROPERTY_PORT_NAME);
			portNameDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
			properties.add(portNameDescriptor);
			PropertyDescriptor portDsscriptionDescriptor = 
				new TextPropertyDescriptor(OPRoSPortElementBaseModel.PROPERTY_PORT_DESCRIPTION, OPRoSPortElementBaseModel.PROPERTY_PORT_DESCRIPTION);
			portDsscriptionDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
			properties.add(portDsscriptionDescriptor);
			OPRoSUnEditableTextPropertyDescriptor portUsageDescriptor = 
				new OPRoSUnEditableTextPropertyDescriptor(OPRoSPortElementBaseModel.PROPERTY_PORT_USAGE, OPRoSPortElementBaseModel.PROPERTY_PORT_USAGE);
			portUsageDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
			properties.add(portUsageDescriptor);
			
			ArrayList<String> portTypes = getPortTypes(element);
			if(portTypes!=null){
				String[] types=new String[portTypes.size()];
				portTypes.toArray(types);
				PropertyDescriptor portTypeDescriptor = 
					new ComboBoxPropertyDescriptor(OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE, OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE,types);
				portTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(portTypeDescriptor);
			}else{
				PropertyDescriptor portTypeDescriptor=
					new TextPropertyDescriptor(OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE, OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE);
				portTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(portTypeDescriptor);
			}
			
			if(element instanceof OPRoSServiceProvidedPortElementModel){
				OPRoSUnEditableTextPropertyDescriptor referenceDescriptor = 
					new OPRoSUnEditableTextPropertyDescriptor(OPRoSServiceProvidedPortElementModel.PROPERTY_SERVICE_PROVIDED_PORT_REFERENCE, OPRoSServiceProvidedPortElementModel.PROPERTY_SERVICE_PROVIDED_PORT_REFERENCE);
				referenceDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(referenceDescriptor);
			}else if(element instanceof OPRoSServiceRequiredPortElementModel){
				OPRoSUnEditableTextPropertyDescriptor referenceDescriptor = 
					new OPRoSUnEditableTextPropertyDescriptor(OPRoSServiceRequiredPortElementModel.PROPERTY_SERVICE_REQUIRED_PORT_REFERENCE, OPRoSServiceRequiredPortElementModel.PROPERTY_SERVICE_REQUIRED_PORT_REFERENCE);
				referenceDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(referenceDescriptor);
			}else if(element instanceof OPRoSDataInPortElementModel){
				OPRoSUnEditableTextPropertyDescriptor referenceDescriptor = 
					new OPRoSUnEditableTextPropertyDescriptor(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_REFERENCE, OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_REFERENCE);
				referenceDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(referenceDescriptor);
				PropertyDescriptor queueSizeDescriptor = 
					new TextPropertyDescriptor(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUESIZE, OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUESIZE);
				queueSizeDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				queueSizeDescriptor.setValidator(new ICellEditorValidator(){
					@Override
					public String isValid(Object value) {
						int nValue =-1;
						try{
							nValue = Integer.parseInt((String)value);
						}catch(NumberFormatException e){
							return OPRoSStrings.getString("PropertyVaridateNotNumberErrorMessage");
						}
						return (nValue>=0)? null: OPRoSStrings.getString("PropertyVaridateNumberErrorMessage");
					}
					
				});
				properties.add(queueSizeDescriptor);
				String[] policyTypes={
						OPRoSStrings.getString("DataPortPolicy0"),
						OPRoSStrings.getString("DataPortPolicy1"),
						OPRoSStrings.getString("DataPortPolicy2")};
				PropertyDescriptor queueingPolicyDescriptor = 
					new ComboBoxPropertyDescriptor(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUEING_POLICY, OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUEING_POLICY,policyTypes);
				queueingPolicyDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(queueingPolicyDescriptor);
			}else if(element instanceof OPRoSDataOutPortElementModel){
				OPRoSUnEditableTextPropertyDescriptor referenceDescriptor = 
					new OPRoSUnEditableTextPropertyDescriptor(OPRoSDataOutPortElementModel.PROPERTY_DATAOUT_PORT_REFERENCE, OPRoSDataOutPortElementModel.PROPERTY_DATAOUT_PORT_REFERENCE);
				referenceDescriptor.setCategory(OPRoSStrings.getString("PropertyPortInfoCategory"));
				properties.add(referenceDescriptor);
			}
		}else if(element instanceof OPRoSExeEnvironmentOSElementModel){
			PropertyDescriptor OSNameDescriptor = 
				new TextPropertyDescriptor(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME, OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME);
			OSNameDescriptor.setCategory(OPRoSStrings.getString("PropertyOSInfoCategory"));
			properties.add(OSNameDescriptor);
			PropertyDescriptor OSVersionDescriptor = 
				new TextPropertyDescriptor(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_VERSION, OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_VERSION);
			OSVersionDescriptor.setCategory(OPRoSStrings.getString("PropertyOSInfoCategory"));
			properties.add(OSVersionDescriptor);
		}else if(element instanceof OPRoSExeEnvironmentCPUElementModel){
			PropertyDescriptor CPUNameDescriptor = 
				new TextPropertyDescriptor(OPRoSExeEnvironmentCPUElementModel.PROPERTY_CPU_NAME, OPRoSExeEnvironmentCPUElementModel.PROPERTY_CPU_NAME);
			CPUNameDescriptor.setCategory(OPRoSStrings.getString("PropertyCPUInfoCategory"));
			properties.add(CPUNameDescriptor);
		}else if(element instanceof OPRoSPropertyElementModel){
			PropertyDescriptor propertyNameDescriptor = 
				new TextPropertyDescriptor(OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME, OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME);
			propertyNameDescriptor.setCategory(OPRoSStrings.getString("PropertyPropertyInfoCategory"));
			properties.add(propertyNameDescriptor);
			PropertyDescriptor propertyTypeDescriptor = 
				new TextPropertyDescriptor(OPRoSPropertyElementModel.PROPERTY_PROPERTY_TYPE, OPRoSPropertyElementModel.PROPERTY_PROPERTY_TYPE);
			propertyTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyPropertyInfoCategory"));
			properties.add(propertyTypeDescriptor);
			PropertyDescriptor propertyDefaultValueDescriptor = 
				new TextPropertyDescriptor(OPRoSPropertyElementModel.PROPERTY_PROPERTY_DEFAULT_VALUE, OPRoSPropertyElementModel.PROPERTY_PROPERTY_DEFAULT_VALUE);
			propertyDefaultValueDescriptor.setCategory(OPRoSStrings.getString("PropertyPropertyInfoCategory"));
			properties.add(propertyDefaultValueDescriptor);
		}else if(element instanceof OPRoSDataTypeElementModel){
			PropertyDescriptor dataTypeFileNameDescriptor = 
				new TextPropertyDescriptor(OPRoSDataTypeElementModel.PROPERTY_DATA_TYPE_FILENAME, OPRoSDataTypeElementModel.PROPERTY_DATA_TYPE_FILENAME);
			dataTypeFileNameDescriptor.setCategory(OPRoSStrings.getString("PropertyDataTypeInfoCategory"));
			properties.add(dataTypeFileNameDescriptor);
		}else if(element instanceof OPRoSServiceTypeElementModel){
			PropertyDescriptor serviceTypeFileNameDescriptor = 
				new TextPropertyDescriptor(OPRoSServiceTypeElementModel.PROPERTY_SERVICE_TYPE_FILENAME, OPRoSServiceTypeElementModel.PROPERTY_SERVICE_TYPE_FILENAME);
			serviceTypeFileNameDescriptor.setCategory(OPRoSStrings.getString("PropertyServiceTypeInfoCategory"));
			properties.add(serviceTypeFileNameDescriptor);
		}
		return properties.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if(element instanceof OPRoSComponentElementModel){
			OPRoSComponentElementModel elem = (OPRoSComponentElementModel) element;
			if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_NAME))
				return ""+elem.getComponentName();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_DESCRIPTION))
				return ""+elem.getComponentDescription();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_VERSION))
				return ""+elem.getVersion();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_ID))
				return ""+elem.getComponentID();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_NAME))
				return ""+elem.getCompanyName();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_PHONE))
				return ""+elem.getCompanyPhone();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_ADDRESS))
				return ""+elem.getCompanyAddress();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_HOMEPAGE))
				return ""+elem.getCompanyHomepage();
			else if(id.equals(OPRoSComponentElementModel.PROPERTY_LICENSE_POLICY)){
				String licensePolicy = elem.getLicensePolicy();
				if(licensePolicy.equals(OPRoSStrings.getString("PropertyCopyRightLicenceCombo0")))
					return new Integer(0);
				else if(licensePolicy.equals(OPRoSStrings.getString("PropertyCopyRightLicenceCombo1")))
					return new Integer(1);
				else if(licensePolicy.equals(OPRoSStrings.getString("PropertyCopyRightLicenceCombo2")))
					return new Integer(2);
				else if(licensePolicy.equals(OPRoSStrings.getString("PropertyCopyRightLicenceCombo3")))
					return new Integer(3);
				else if(licensePolicy.equals(OPRoSStrings.getString("PropertyCopyRightLicenceCombo4")))
					return new Integer(4);
				else
					return new Integer(0);
			}
//			Object ret = getComponentInfoPropertyValue(elem.getSpecificationModel(), id);
//			if(ret != null)
//				return ret;
			Object ret = getExeEnvPropertyValue(elem.getExeEnvironmentModel(),id);
			if(ret != null)
				return ret;
			return getExeSemanticsPropertyValue(elem.getExeSemanticsModel(),id);
//		}else if(element instanceof OPRoSComponentSpecificationElementModel){
//			return getComponentInfoPropertyValue(element, id);
		}else if(element instanceof OPRoSPortElementBaseModel){
			OPRoSPortElementBaseModel elem = (OPRoSPortElementBaseModel) element;
			if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME))
				return ""+elem.getName();
			else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_DESCRIPTION))
				return ""+elem.getDescription();
			else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_USAGE))
				return ""+elem.getUsage();
			else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE)){
				ArrayList<String> portTypes = getPortTypes(element);
				String portType = elem.getType();
				if(portTypes!=null){
					for(int i=0;i<portTypes.size();i++){
						if(portType.compareTo(portTypes.get(i))==0){
							return new Integer(i);
						}
					}
					return new Integer(0);
				}else{
					return ""+portType;
				}
			}
			if(element instanceof OPRoSServiceProvidedPortElementModel){
				if(id.equals(OPRoSServiceProvidedPortElementModel.PROPERTY_SERVICE_PROVIDED_PORT_REFERENCE))
					return ""+((OPRoSServiceProvidedPortElementModel)elem).getReference();
			}else if(element instanceof OPRoSServiceRequiredPortElementModel){
				if(id.equals(OPRoSServiceRequiredPortElementModel.PROPERTY_SERVICE_REQUIRED_PORT_REFERENCE))
					return ""+((OPRoSServiceRequiredPortElementModel)elem).getReference();
			}else if(element instanceof OPRoSDataInPortElementModel){
				if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_REFERENCE))
					return ""+((OPRoSDataInPortElementModel)elem).getReference();
				else if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUESIZE))
					return ""+((OPRoSDataInPortElementModel)elem).getQueueSize();
				else if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUEING_POLICY)){
					String policyType = ((OPRoSDataInPortElementModel)elem).getQueueingPolicy();
					if(policyType.compareTo(OPRoSStrings.getString("DataPortPolicy0"))==0)
						return new Integer(0);
					else if(policyType.compareTo(OPRoSStrings.getString("DataPortPolicy1"))==0)
						return new Integer(1);
					else if(policyType.compareTo(OPRoSStrings.getString("DataPortPolicy2"))==0)
						return new Integer(2);
				}
					return ""+((OPRoSDataInPortElementModel)elem).getQueueingPolicy();
			}else if(element instanceof OPRoSDataOutPortElementModel){
				if(id.equals(OPRoSDataOutPortElementModel.PROPERTY_DATAOUT_PORT_REFERENCE))
					return ""+((OPRoSDataOutPortElementModel)elem).getReference();
			}
		}else if(element instanceof OPRoSExeEnvironmentElementModel){
			return getExeEnvPropertyValue(element,id);
		}else if(element instanceof OPRoSExeSemanticsElementModel){
			return getExeSemanticsPropertyValue(element,id);
		}else if(element instanceof OPRoSExeEnvironmentOSElementModel){
			OPRoSExeEnvironmentOSElementModel elem = (OPRoSExeEnvironmentOSElementModel) element;
			if(id.equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME))
				return ""+elem.getOSName();
			else if(id.equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_VERSION))
				return ""+elem.getOSVersion();
		}else if(element instanceof OPRoSExeEnvironmentCPUElementModel){
			OPRoSExeEnvironmentCPUElementModel elem = (OPRoSExeEnvironmentCPUElementModel) element;
			if(id.equals(OPRoSExeEnvironmentCPUElementModel.PROPERTY_CPU_NAME))
				return ""+elem.getCPUName();
		}else if(element instanceof OPRoSPropertyElementModel){
			OPRoSPropertyElementModel elem = (OPRoSPropertyElementModel) element;
			if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME))
				return ""+elem.getName();
			else if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_TYPE))
				return ""+elem.getType();
			else if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_DEFAULT_VALUE))
				return ""+elem.getDefaultValue();
		}else if(element instanceof OPRoSDataTypeElementModel){
			OPRoSDataTypeElementModel elem = (OPRoSDataTypeElementModel) element;
			if(id.equals(OPRoSDataTypeElementModel.PROPERTY_DATA_TYPE_FILENAME))
				return ""+elem.getDataTypeFileName();
		}else if(element instanceof OPRoSServiceTypeElementModel){
			OPRoSServiceTypeElementModel elem = (OPRoSServiceTypeElementModel) element;
			if(id.equals(OPRoSServiceTypeElementModel.PROPERTY_SERVICE_TYPE_FILENAME))
				return ""+elem.getServiceTypeFileName();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if(element instanceof OPRoSComponentElementModel){
			OPRoSComponentElementModel elem = (OPRoSComponentElementModel) element;
//			if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_NAME)){
//				elem.setComponentName((String)value);
			/*}else*/ if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_DESCRIPTION)){
				elem.setComponentDescription((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_VERSION)){
				elem.setVersion((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMP_ID)){
				elem.setComponentID((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_NAME)){
				elem.setCompanyName((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_PHONE)){
				elem.setCompanyPhone((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_ADDRESS)){
				elem.setCompanyAddress((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_COMPANY_HOMEPAGE)){
				elem.setCompanyHomepage((String)value);
			}else if(id.equals(OPRoSComponentElementModel.PROPERTY_LICENSE_POLICY)){
				String licensePolicy="";
				if(((Integer)value)==0)
					licensePolicy=OPRoSStrings.getString("PropertyCopyRightLicenceCombo0");
				else if(((Integer)value)==1)
					licensePolicy=OPRoSStrings.getString("PropertyCopyRightLicenceCombo1");
				else if(((Integer)value)==2)
					licensePolicy=OPRoSStrings.getString("PropertyCopyRightLicenceCombo2");
				else if(((Integer)value)==3)
					licensePolicy=OPRoSStrings.getString("PropertyCopyRightLicenceCombo3");
				else if(((Integer)value)==4)
					licensePolicy=OPRoSStrings.getString("PropertyCopyRightLicenceCombo4");
				elem.setLicensePolicy(licensePolicy);
			}
//			setComponentInfoPropertyValue(elem.getSpecificationModel(),id,value);
			setExeEnvInfoPropertyValue(elem.getExeEnvironmentModel(),id,value);
			setExeSemanticsPropertyValue(elem.getExeSemanticsModel(),id,value);
//		}else if(element instanceof OPRoSComponentSpecificationElementModel){
//			setComponentInfoPropertyValue(element,id,value);
//			
		}else if(element instanceof OPRoSPortElementBaseModel){
			OPRoSPortElementBaseModel elem = (OPRoSPortElementBaseModel) element;
			if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_NAME)){
				elem.setName((String)value);
			}else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_DESCRIPTION)){
				elem.setDescription((String)value);
			}else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_USAGE)){
//				elem.setUsage((String)value);
			}else if(id.equals(OPRoSPortElementBaseModel.PROPERTY_PORT_TYPE)){
				ArrayList<String> portTypes = getPortTypes(element);
				if(portTypes!=null){
					for(int i=0;i<portTypes.size();i++){
						if(((Integer)value)==i)
							elem.setType(portTypes.get(i));
					}
				}else{
					elem.setType((String)value);
				}
			}
			if(element instanceof OPRoSServiceProvidedPortElementModel){
				if(id.equals(OPRoSServiceProvidedPortElementModel.PROPERTY_SERVICE_PROVIDED_PORT_REFERENCE)){
//					((OPRoSServiceInPortElementModel)elem).setReference((String)value);
				}
			}else if(element instanceof OPRoSServiceRequiredPortElementModel){
				if(id.equals(OPRoSServiceRequiredPortElementModel.PROPERTY_SERVICE_REQUIRED_PORT_REFERENCE)){
//					((OPRoSServiceOutPortElementModel)elem).setReference((String)value);
				}
			}else if(element instanceof OPRoSDataInPortElementModel){
				if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_REFERENCE)){
//					((OPRoSDataInPortElementModel)elem).setReference((String)value);
				}else if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUESIZE)){
					((OPRoSDataInPortElementModel)elem).setQueueSize((String)value);
				}else if(id.equals(OPRoSDataInPortElementModel.PROPERTY_DATAIN_PORT_QUEUEING_POLICY)){
					String policyType="";
					if(((Integer)value)==0)
						policyType=OPRoSStrings.getString("DataPortPolicy0");
					else if(((Integer)value)==1)
						policyType=OPRoSStrings.getString("DataPortPolicy1");
					else if(((Integer)value)==2)
						policyType=OPRoSStrings.getString("DataPortPolicy2");
					((OPRoSDataInPortElementModel)elem).setQueueingPolicy(policyType);
				}
			}else if(element instanceof OPRoSDataOutPortElementModel){
				if(id.equals(OPRoSDataOutPortElementModel.PROPERTY_DATAOUT_PORT_REFERENCE)){
//					((OPRoSDataOutPortElementModel)elem).setReference((String)value);
				}
			}
		}else if(element instanceof OPRoSExeEnvironmentElementModel){
			setExeEnvInfoPropertyValue(element,id,value);
		}else if(element instanceof OPRoSExeSemanticsElementModel){
			setExeSemanticsPropertyValue(element,id,value);
		}else if(element instanceof OPRoSExeEnvironmentOSElementModel){
			OPRoSExeEnvironmentOSElementModel elem = (OPRoSExeEnvironmentOSElementModel) element;
			if(id.equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_NAME)){
				elem.setOSName((String)value);
			}else if(id.equals(OPRoSExeEnvironmentOSElementModel.PROPERTY_OS_VERSION)){
				elem.setOSVersion((String)value);
			}
		}else if(element instanceof OPRoSExeEnvironmentCPUElementModel){
			OPRoSExeEnvironmentCPUElementModel elem = (OPRoSExeEnvironmentCPUElementModel) element;
			if(id.equals(OPRoSExeEnvironmentCPUElementModel.PROPERTY_CPU_NAME)){
				elem.setCPUName((String)value);
			}
		}else if(element instanceof OPRoSPropertyElementModel){
			OPRoSPropertyElementModel elem = (OPRoSPropertyElementModel) element;
			if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_NAME)){
				elem.setName((String)value);
			}else if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_TYPE)){
				elem.setType((String)value);
			}else if(id.equals(OPRoSPropertyElementModel.PROPERTY_PROPERTY_DEFAULT_VALUE)){
				elem.setDefaultValue((String)value);
			}
//		}else if(element instanceof OPRoSDataTypeElementModel){
//			OPRoSDataTypeElementModel elem = (OPRoSDataTypeElementModel) element;
//			if(id.equals(OPRoSDataTypeElementModel.PROPERTY_DATA_TYPE_FILENAME)){
//				elem.setDataTypeFileName((String)value);
//			}
		}
	}
	@SuppressWarnings("unchecked")
	private ArrayList<String> getPortTypes(OPRoSElementBaseModel model){
		String fileName;
		ArrayList<String> portTypes = new ArrayList<String>();
		if(model instanceof OPRoSServiceProvidedPortElementModel || model instanceof OPRoSServiceRequiredPortElementModel){
			if(model instanceof OPRoSServiceProvidedPortElementModel)
				fileName=((OPRoSServiceProvidedPortElementModel)model).getReference();
			else
				fileName=((OPRoSServiceRequiredPortElementModel)model).getReference();
			OPRoSComponentElementModel compModel = ((OPRoSBodyElementModel)model.getParent()).compEle;
			Iterator<OPRoSElementBaseModel> it = compModel.getServiceTypesModel().getChildrenList().iterator();
			while(it.hasNext()){
				OPRoSServiceTypeElementModel typeModel = (OPRoSServiceTypeElementModel)it.next();
				if(typeModel.getServiceTypeFileName().compareTo(fileName)==0){
					Document doc = typeModel.getServiceTypeDoc();
					Iterator<Element> eles;
					Element ele;
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("ServiceTypeEle")).iterator();
					while(eles.hasNext()){
						ele = eles.next();
						portTypes.add(ele.getChildTextTrim("type_name"));
					}
				}
			}
		}else if(model instanceof OPRoSDataInPortElementModel || model instanceof OPRoSDataOutPortElementModel){
			//data type을 변경할 수 있도록 하기 위해
			return null;
//			if(model instanceof OPRoSDataInPortElementModel)
//				fileName = ((OPRoSDataInPortElementModel)model).getReference();
//			else
//				fileName = ((OPRoSDataOutPortElementModel)model).getReference();
//			if(fileName==null||fileName.isEmpty()){
//				for(int i=0;i<20;i++){
//					portTypes.add(OPRoSUtil.dataTypes[i]);
//				}
//			}else{
//				OPRoSComponentElementModel compModel = OPRoSComponentElementModel.getInstance();
//				Iterator<OPRoSElementBaseModel> it = compModel.getDataTypesModel().getChildrenList().iterator();
//				while(it.hasNext()){
//					OPRoSDataTypeElementModel typeModel = (OPRoSDataTypeElementModel)it.next();
//					if(typeModel.getDataTypeFileName().compareTo(fileName)==0){
//						Document doc = typeModel.getDataTypeDoc();
//						Iterator<Element> eles;
//						Element ele;
//						eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
//						while(eles.hasNext()){
//							ele = eles.next();
//							portTypes.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
//						}
//					}
//				}
//			}
		}else if(model instanceof OPRoSEventInPortElementModel||model instanceof OPRoSEventOutPortElementModel){
			//data type을 변경할 수 있도록하기 위해...
			return null;
//			for(int i=0;i<20;i++){
//				portTypes.add(OPRoSUtil.dataTypes[i]);
//			}
//			OPRoSComponentElementModel compModel = OPRoSComponentElementModel.getInstance();
//			if(compModel!=null){
//				Iterator<OPRoSElementBaseModel> it = compModel.getDataTypesModel().getChildrenList().iterator();
//				while(it.hasNext()){
//					OPRoSDataTypeElementModel dataModel=(OPRoSDataTypeElementModel)it.next();
//					Iterator<Element> eles;
//					Element ele;
//					Document doc = dataModel.getDataTypeDoc();
//					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
//					while(eles.hasNext()){
//						ele=eles.next();
//						portTypes.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
//					}
//				}
//			}
		}
		return portTypes;
	}
//	private void makeComponentInfoPropertiesView(List<IPropertyDescriptor> properties){
//		PropertyDescriptor libraryNameDescriptor = 
//			new TextPropertyDescriptor(OPRoSComponentSpecificationElementModel.PROPERTY_LIBRARY_NAME, OPRoSComponentSpecificationElementModel.PROPERTY_LIBRARY_NAME);
//		libraryNameDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
//		properties.add(libraryNameDescriptor);
//		PropertyDescriptor libraryTypeDescriptor = 
//			new TextPropertyDescriptor(OPRoSComponentSpecificationElementModel.PROPERTY_LIBRARY_TYPE, OPRoSComponentSpecificationElementModel.PROPERTY_LIBRARY_TYPE);
//		libraryTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
//		properties.add(libraryTypeDescriptor);
//		PropertyDescriptor implLangDescriptor = 
//			new TextPropertyDescriptor(OPRoSComponentSpecificationElementModel.PROPERTY_IMPL_LANGUAGE, OPRoSComponentSpecificationElementModel.PROPERTY_IMPL_LANGUAGE);
//		implLangDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
//		properties.add(implLangDescriptor);
//		String[] compilerDatas = {
//				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo0"),
//				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo1"),
//				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo2")};
//		PropertyDescriptor compilerDescriptor = 
//			new ComboBoxPropertyDescriptor(OPRoSComponentSpecificationElementModel.PROPERTY_COMPILER, OPRoSComponentSpecificationElementModel.PROPERTY_COMPILER,compilerDatas);
//		compilerDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
//		properties.add(compilerDescriptor);
//		
//	}
	private void makeExeEnvPropertiesView(List<IPropertyDescriptor> properties){
		PropertyDescriptor libraryNameDescriptor = 
			new TextPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_NAME, OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_NAME);
		libraryNameDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
		properties.add(libraryNameDescriptor);
		PropertyDescriptor libraryTypeDescriptor = 
			new TextPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_TYPE, OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_TYPE);
		libraryTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
		properties.add(libraryTypeDescriptor);
		PropertyDescriptor implLangDescriptor = 
			new TextPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_IMPL_LANGUAGE, OPRoSExeEnvironmentElementModel.PROPERTY_IMPL_LANGUAGE);
		implLangDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
		properties.add(implLangDescriptor);
		String[] compilerDatas = {
				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo0"),
				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo1"),
				OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo2")};
		PropertyDescriptor compilerDescriptor = 
			new ComboBoxPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_COMPILER, OPRoSExeEnvironmentElementModel.PROPERTY_COMPILER,compilerDatas);
		compilerDescriptor.setCategory(OPRoSStrings.getString("PropertyLibInfoCategory"));
		properties.add(compilerDescriptor);
		PropertyDescriptor domainDescriptor = 
			new TextPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_DOMAIN, OPRoSExeEnvironmentElementModel.PROPERTY_DOMAIN);
		domainDescriptor.setCategory(OPRoSStrings.getString("PropertyExeEnvInfoCategory"));
		properties.add(domainDescriptor);
		PropertyDescriptor robotTypeDescriptor = 
			new TextPropertyDescriptor(OPRoSExeEnvironmentElementModel.PROPERTY_ROBOT_TYPE, OPRoSExeEnvironmentElementModel.PROPERTY_ROBOT_TYPE);
		robotTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyExeEnvInfoCategory"));
		properties.add(robotTypeDescriptor);
	}
	private void makeExeSemanticsPropertiesView(List<IPropertyDescriptor> properties){
		String[] exeTypeDatas = {
				OPRoSStrings.getString("PropertyExeSemanticsTypeCombo0"),
				OPRoSStrings.getString("PropertyExeSemanticsTypeCombo1"),
				OPRoSStrings.getString("PropertyExeSemanticsTypeCombo2")};
		PropertyDescriptor exeTypeDescriptor = 
			new ComboBoxPropertyDescriptor(OPRoSExeSemanticsElementModel.PROPERTY_EXE_TYPE, OPRoSExeSemanticsElementModel.PROPERTY_EXE_TYPE,exeTypeDatas);
		exeTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyExeSemanticsCategory"));
		properties.add(exeTypeDescriptor);
		PropertyDescriptor exePeriodDescriptor = 
			new TextPropertyDescriptor(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PERIOD, OPRoSExeSemanticsElementModel.PROPERTY_EXE_PERIOD);
		exePeriodDescriptor.setCategory(OPRoSStrings.getString("PropertyExeSemanticsCategory"));
		exePeriodDescriptor.setValidator(new ICellEditorValidator(){

			@Override
			public String isValid(Object value) {
				int nValue =-1;
				try{
					nValue = Integer.parseInt((String)value);
				}catch(NumberFormatException e){
					return OPRoSStrings.getString("PropertyVaridateNotNumberErrorMessage");
				}
				return (nValue>=0)? null: OPRoSStrings.getString("PropertyVaridateNumberErrorMessage");
			}
			
		});
		properties.add(exePeriodDescriptor);
		PropertyDescriptor exePriorityDescriptor = 
			new TextPropertyDescriptor(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PRIORITY, OPRoSExeSemanticsElementModel.PROPERTY_EXE_PRIORITY);
		exePriorityDescriptor.setCategory("Execution Semantics");
		exePriorityDescriptor.setValidator(new ICellEditorValidator(){

			@Override
			public String isValid(Object value) {
				int nValue = -1;
				try{
					nValue = Integer.parseInt((String)value);
				}catch(NumberFormatException e){
					return OPRoSStrings.getString("PropertyVaridateNotNumberErrorMessage");
				}
				return (nValue>=0&&nValue<=255)? null: "Value must be 0<= Value <=255 ";
			}
		});
		properties.add(exePriorityDescriptor);
		String[] instanceTypeDatas = {
				OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo0"),
				OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo1")};
		PropertyDescriptor exeInstanceTypeDescriptor = 
			new ComboBoxPropertyDescriptor(OPRoSExeSemanticsElementModel.PROPERTY_EXE_INSTANCE_TYPE, OPRoSExeSemanticsElementModel.PROPERTY_EXE_INSTANCE_TYPE,instanceTypeDatas);
		exeInstanceTypeDescriptor.setCategory(OPRoSStrings.getString("PropertyExeSemanticsCategory"));
		properties.add(exeInstanceTypeDescriptor);
		String[] lifecycleTypeDatas = {
				OPRoSStrings.getString("PropertyExeSematicsLifeCycleCombo0"),
				OPRoSStrings.getString("PropertyExeSematicsLifeCycleCombo1")};
		
	}
	
	private Object getExeEnvPropertyValue(OPRoSElementBaseModel element, Object id){
		OPRoSExeEnvironmentElementModel elem = (OPRoSExeEnvironmentElementModel) element;
		if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_NAME))
			return ""+elem.getLibraryName();
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_TYPE))
			return ""+elem.getLibraryType();
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_IMPL_LANGUAGE))
			return ""+elem.getImplLang();
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_COMPILER)){
			String compiler = elem.getCompiler();
			if(compiler.equals(OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo0")))
				return new Integer(0);
			else if(compiler.equals(OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo1")))
				return new Integer(1);
			else if(compiler.equals(OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo2")))
				return new Integer(2);
			else
				return new Integer(0);
		}
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_DOMAIN))
			return ""+elem.getDomain();
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_ROBOT_TYPE))
			return ""+elem.getRobotType();
		return null;
	}
	private Object getExeSemanticsPropertyValue(OPRoSElementBaseModel element, Object id){
		OPRoSExeSemanticsElementModel elem = (OPRoSExeSemanticsElementModel) element;
		if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_TYPE)){
			String exeType = elem.getExeType();
			if(exeType.equals(OPRoSStrings.getString("PropertyExeSemanticsTypeCombo0")))
				return new Integer(0);
			else if(exeType.equals(OPRoSStrings.getString("PropertyExeSemanticsTypeCombo1")))
				return new Integer(1);
			else if(exeType.equals(OPRoSStrings.getString("PropertyExeSemanticsTypeCombo2")))
				return new Integer(2);
			else
				return new Integer(0);
		}else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PERIOD))
			return ""+elem.getExePeriod();
		else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PRIORITY))
			return ""+elem.getExePriority();
		else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_INSTANCE_TYPE)){
			String exeInstanceType = elem.getExeInstanceType();
			if(exeInstanceType.equals(OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo0")))
				return new Integer(0);
			else if(exeInstanceType.equals(OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo1")))
				return new Integer(1);
			else
				return new Integer(0);
		}
		return null;
	}
	
	private void setExeEnvInfoPropertyValue(OPRoSElementBaseModel element,Object id,Object value){
		OPRoSExeEnvironmentElementModel elem = (OPRoSExeEnvironmentElementModel) element;
		if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_NAME)){
			elem.setLibraryName((String)value);
		}else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_LIBRARY_TYPE)){
			elem.setLibraryType((String)value);
		}else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_IMPL_LANGUAGE)){
			elem.setImplLang((String)value);
		}else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_COMPILER)){
			String compiler="";
			if(((Integer)value)==0)
				compiler=OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo0");
			else if(((Integer)value)==1)
				compiler=OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo1");
			else if(((Integer)value)==2)
				compiler=OPRoSStrings.getString("PropertyExeSemanticsCompilerCombo2");
			elem.setCompiler(compiler);
		}
		else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_DOMAIN)){
			elem.setDomain((String)value);
		}else if(id.equals(OPRoSExeEnvironmentElementModel.PROPERTY_ROBOT_TYPE)){
			elem.setRobotType((String)value);
		}
	}
	private void setExeSemanticsPropertyValue(OPRoSElementBaseModel element,Object id,Object value){
		OPRoSExeSemanticsElementModel elem = (OPRoSExeSemanticsElementModel) element;
		if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_TYPE)){
			String exeType="";
			if(((Integer)value)==0)
				exeType=OPRoSStrings.getString("PropertyExeSemanticsTypeCombo0");
			else if(((Integer)value)==1){
				exeType=OPRoSStrings.getString("PropertyExeSemanticsTypeCombo1");
				elem.setExePeriod("0");
			}
			else if(((Integer)value)==2)
				exeType=OPRoSStrings.getString("PropertyExeSemanticsTypeCombo2");
			elem.setExeType(exeType);
		}else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PERIOD)){
			elem.setExePeriod((String)value);
		}else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_PRIORITY)){
			elem.setExePriority((String)value);
		}else if(id.equals(OPRoSExeSemanticsElementModel.PROPERTY_EXE_INSTANCE_TYPE)){
			String exeInstanceType="";
			if(((Integer)value)==0)
				exeInstanceType=OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo0");
			else if(((Integer)value)==1)
				exeInstanceType=OPRoSStrings.getString("PropertyExeSematicsInstanceTypeCombo1");
			elem.setExeInstanceType(exeInstanceType);
		}
	}
}
