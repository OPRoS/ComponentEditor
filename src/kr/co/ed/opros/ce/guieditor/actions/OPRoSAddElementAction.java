package kr.co.ed.opros.ce.guieditor.actions;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.editors.OPRoSGUIProfileEditor;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.command.MonitoringVariableCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSDataTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentCPUElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentOSElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPortCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPropertyElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSServiceTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
//import org.opros.test.core.common.OPRoSTestCommonException;
//import org.opros.test.core.porttest.ServicePortTestData;
//import org.opros.test.core.porttest.ServicePortTestProjectManager;
//import org.opros.test.core.util.PathUtil;

public class OPRoSAddElementAction extends SelectionAction implements
		IWorkbenchWindowActionDelegate {
	public static final String ADD_SERVICE_PROVIDED_PORT ="Add Service Provided Port";
	public static final String ADD_SERVICE_REQUIRED_PORT ="Add Service Required Port";
	public static final String ADD_DATA_IN_PORT ="Add Data Input Port";
	public static final String ADD_DATA_OUT_PORT ="Add Data Output Port";
	public static final String ADD_EVENT_IN_PORT ="Add Event Input Port";
	public static final String ADD_EVENT_OUT_PORT ="Add Event Output Port";
	public static final String ADD_OS ="Add OS ExeEnv";
	public static final String ADD_CPU ="Add CPU ExeEnv";
	public static final String ADD_PROPERTY ="Add Property";
	public static final String ADD_DATA_TYPE ="Add Data Type";
	public static final String ADD_SERVICE_TYPE ="Add Service Type";
	public static final String NEW_SERVICE_PORT_TEST ="New ServicePort Test";
	public static final String ADD_MONITORING_VARIABLE = "Add Monitoring Variable";
	
	Request request;
	private OPRoSGUIProfileEditor editor;
	
	public OPRoSAddElementAction(IEditorPart part, String type) {
		super(part);
		editor=(OPRoSGUIProfileEditor)part;
		if(type.equals(ADD_SERVICE_PROVIDED_PORT)){
			request = new Request(ADD_SERVICE_PROVIDED_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceProvidedIcon")));
		}else if(type.equals(ADD_SERVICE_REQUIRED_PORT)){
			request = new Request(ADD_SERVICE_REQUIRED_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceRequiredIcon")));
		}else if(type.equals(ADD_DATA_IN_PORT)){
			request = new Request(ADD_DATA_IN_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataInIcon")));
		}else if(type.equals(ADD_DATA_OUT_PORT)){
			request = new Request(ADD_DATA_OUT_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataOutIcon")));
		}else if(type.equals(ADD_EVENT_IN_PORT)){
			request = new Request(ADD_EVENT_IN_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventInIcon")));
		}else if(type.equals(ADD_EVENT_OUT_PORT)){
			request = new Request(ADD_EVENT_OUT_PORT);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventOutIcon")));
		}else if(type.equals(ADD_OS)){
			request = new Request(ADD_OS);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("OSIcon")));
		}else if(type.equals(ADD_CPU)){
			request = new Request(ADD_CPU);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("CPUIcon")));
		}else if(type.equals(ADD_PROPERTY)){
			request = new Request(ADD_PROPERTY);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("PropertyIcon")));
		}else if(type.equals(ADD_DATA_TYPE)){
			request = new Request(ADD_DATA_TYPE);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataTypeIcon")));
		}else if(type.equals(ADD_SERVICE_TYPE)){
			request = new Request(ADD_SERVICE_TYPE);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceTypeIcon")));
		}else if(type.equals(NEW_SERVICE_PORT_TEST)){
			request = new Request(NEW_SERVICE_PORT_TEST);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceTypeIcon")));
		}else if(type.equals(ADD_MONITORING_VARIABLE)){
			request = new Request(ADD_MONITORING_VARIABLE);
			this.setImageDescriptor(ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("MonitoringVariableIcon")));
		}
		setId(type);
		setText(type);
	}

	@Override
	protected boolean calculateEnabled() {
		if(request.getType().equals(ADD_SERVICE_PROVIDED_PORT)){
			return true;
		}else if(request.getType().equals(ADD_SERVICE_REQUIRED_PORT)){
			return true;
		}else if(request.getType().equals(ADD_DATA_IN_PORT)){
			return true;
		}else if(request.getType().equals(ADD_DATA_OUT_PORT)){
			return true;
		}else if(request.getType().equals(ADD_EVENT_IN_PORT)){
			return true;
		}else if(request.getType().equals(ADD_EVENT_OUT_PORT)){
			return true;
		}else if(request.getType().equals(ADD_OS)){
			return true;
		}else if(request.getType().equals(ADD_CPU)){
			return true;
		}else if(request.getType().equals(ADD_PROPERTY)){
			return true;
		}else if(request.getType().equals(ADD_DATA_TYPE)){
			return true;
		}else if(request.getType().equals(ADD_SERVICE_TYPE)){
			return true;
		}else if(request.getType().equals(NEW_SERVICE_PORT_TEST)){
			return true;
		}else if(request.getType().equals(ADD_MONITORING_VARIABLE)){
			return true;
		}		
		return false;
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}
	
	private Command getCommand(){
		OPRoSElementBaseModel parent = editor.getModel();
		OPRoSElementBaseModel element=null;
		Command cmd=null;
		if(getId().equals(ADD_SERVICE_PROVIDED_PORT)){
			element = new OPRoSServiceProvidedPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_SERVICE_REQUIRED_PORT)){
			element = new OPRoSServiceRequiredPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_DATA_IN_PORT)){
			element = new OPRoSDataInPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_DATA_OUT_PORT)){
			element = new OPRoSDataOutPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_EVENT_IN_PORT)){
			element = new OPRoSEventInPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_EVENT_OUT_PORT)){
			element = new OPRoSEventOutPortElementModel();
			cmd = new OPRoSPortCreateCommand();
			((OPRoSPortCreateCommand)cmd).setParent(parent);
			((OPRoSPortCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_OS)){
			element = new OPRoSExeEnvironmentOSElementModel();
			cmd = new OPRoSExeEnvironmentOSElementCreateCommand();
			((OPRoSExeEnvironmentOSElementCreateCommand)cmd).setParent(parent.getChild(0));
			((OPRoSExeEnvironmentOSElementCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_CPU)){
			element = new OPRoSExeEnvironmentCPUElementModel();
			cmd = new OPRoSExeEnvironmentCPUElementCreateCommand();
			((OPRoSExeEnvironmentCPUElementCreateCommand)cmd).setParent(parent.getChild(0));
			((OPRoSExeEnvironmentCPUElementCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_PROPERTY)){
			element = new OPRoSPropertyElementModel();
			cmd = new OPRoSPropertyElementCreateCommand();
			((OPRoSPropertyElementCreateCommand)cmd).setParent(parent.getChild(0));
			((OPRoSPropertyElementCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_DATA_TYPE)){
			element = new OPRoSDataTypeElementModel();
			cmd = new OPRoSDataTypeElementCreateCommand();
			((OPRoSDataTypeElementCreateCommand)cmd).setParent(parent.getChild(0));
			((OPRoSDataTypeElementCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_SERVICE_TYPE)){
			element = new OPRoSServiceTypeElementModel();
			cmd = new OPRoSServiceTypeElementCreateCommand();
			((OPRoSServiceTypeElementCreateCommand)cmd).setParent(parent.getChild(0));
			((OPRoSServiceTypeElementCreateCommand)cmd).setElement(element);
		}else if(getId().equals(ADD_MONITORING_VARIABLE)){
			element = new MonitoringVariableModel();
			cmd = new MonitoringVariableCreateCommand();
			((MonitoringVariableCreateCommand)cmd).setParent(parent.getChild(0));
			((MonitoringVariableCreateCommand)cmd).setElement(element);
		}		
		else if(getId().equals(NEW_SERVICE_PORT_TEST)){
			String profilePathName=editor.getModel().getFilename();
			String projectPath = profilePathName.substring(0,profilePathName.lastIndexOf("/"));
			projectPath = projectPath.substring(0,projectPath.lastIndexOf("/"));
//			String libraryPath = projectPath + "/Release/";
//			String libraryName=libraryPath+((OPRoSExeEnvironmentElementModel)editor.getModel().compEle.getExeEnvironmentModel()).getLibraryName();
//			String servicePortName=editor.getSelectionServicePortName();
//			ServicePortTestData testData = new ServicePortTestData();
//			testData.setProjectName("SPT_01");
//			testData.set_tCompDllName(new PathUtil(libraryName));
//			testData.set_tCompProfileName(new PathUtil(profilePathName));
//			testData.set_tServicePortName(servicePortName);
//			
//			try {
//				new ServicePortTestProjectManager(projectPath, testData);
//			} catch (OPRoSTestCommonException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				editor.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cmd;
	}
	
	public void run(){
		Command command = getCommand();
		execute(command);
		editor.updateOutline();
	}

}
