/*******************************************************************************
* Copyright (C) 2008 ED Corp., All rights reserved
*  
*  
* This programs is the production of ED Copr. research & development activity;
* you cannot use it and cannot redistribute it and cannot modify it and
* cannot  store it in any media(disk, photo or otherwise) without the prior
* permission of ED.
* 
* You should have received the license for this program to use any purpose.
* If not, contact the ED CORPORATION, 517-15, SangDaeWon-Dong, JungWon-Gu,
* SeongNam-City, GyeongGi-Do, Korea. (Zip : 462-806), http://www.ed.co.kr
* 
* NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
* IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
* PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ED Corp.
*
* @Author: sevensky(Juwon Kim), (jwkim@ed.co.kr, sevensky@mju.ac.kr)
********************************************************************************/

package kr.co.ed.opros.ce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.ui.perspectives.OPRoSPerspectiveFactory;
import kr.co.ed.opros.ce.wizards.OPRoSGUIWizardPage;
import kr.co.ed.opros.ce.wizards.OPRoSNewPrjWizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class OPRoSActivator extends AbstractUIPlugin {
	
	public static final String KEY_PORT_RENAME = "PORT_RENAME";
	public static final String KEY_PORT_DELETE = "PORT_DELETE";
	
	public static final String ELEMENT_PROFILE = "profile";
	public static final String ELEMENT_ID = "id";
	public static final String ELEMENT_DATATYPE = "dataType";
	public static final String ELEMENT_SERVICETYPE = "serviceType";
	public static final String ELEMENT_EXECUTION_ENVIRONMENT = "execution_environment";
	public static final String ELEMENT_LIBRARY_TYPE = "library_type";
	public static final String ELEMENT_LIBRARY_NAME = "library_name";
	public static final String ELEMENT_IMPL_LANGUAGE = "impl_language";
	
	public static final String ATTRIBUTE_NAME = "name";

	// The plug-in ID
	public static final String PLUGIN_ID = "org.opros.componentEditor";

	// The shared instance
	private static OPRoSActivator plugin;
	
	/**
	 * The constructor
	 */
	public OPRoSActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		registerIcons();
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new OPRoSPerspectiveEventHandler());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static OPRoSActivator getDefault() {
		return plugin;
	}
	
	/**
	 * 아이콘 등록
	 */
	private void registerIcons() {
		getImageRegistry().put(IIconConstants.ICON_DEPENDENCIES_EDITOR,
				getDescriptor("icons/dependencies.gif"));
		getImageRegistry().put(IIconConstants.SAMPLE_ICON,
				getDescriptor("icons/sample.gif"));
		getImageRegistry().put(IIconConstants.ICON_CONFIG_TOOL,
				getDescriptor("icons/config-tool.gif"));
		getImageRegistry().put(IIconConstants.ICON_OPROS_COMPONENT,
				getDescriptor("icons/Component.bmp"));
		getImageRegistry().put(IIconConstants.IMG_COMPONENT_BODY,
				getDescriptor("icons/component_body.png"));
		getImageRegistry().put(IIconConstants.IMG_COMPOSITE_TOPLEFT,
				getDescriptor("icons/composite_bg_topleft.png"));
		getImageRegistry().put(IIconConstants.IMG_COMPOSITE_TOPRIGHT,
				getDescriptor("icons/composite_bg_topright.png"));
		getImageRegistry().put(IIconConstants.IMG_COMPOSITE_BOTTOMLEFT,
				getDescriptor("icons/composite_bg_bottomleft.png"));
		getImageRegistry().put(IIconConstants.IMG_COMPOSITE_BOTTOMRIGHT,
				getDescriptor("icons/composite_bg_bottomright.png"));
		
		getImageRegistry().put(IIconConstants.IMG_PROPERTIES_EVENT,
				getDescriptor("icons/Properties_event.png"));
		getImageRegistry().put(IIconConstants.IMG_DATATYPES_EVENT,
				getDescriptor("icons/DataTypes_event.png"));
		getImageRegistry().put(IIconConstants.IMG_SERVICETYPES_EVENT,
				getDescriptor("icons/ServiceTypes_event.png"));
		getImageRegistry().put(IIconConstants.IMG_ENVIRONMENT_EVENT,
				getDescriptor("icons/Environment_event.png"));
		getImageRegistry().put(IIconConstants.IMG_EXPORT_ICON,
				getDescriptor("icons/export.png"));
		
	}
	
	private ImageDescriptor getDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static ImageDescriptor getImageDescriptor(String id) {
		return getDefault().getImageRegistry().getDescriptor(id);
	}

	public static Image getImage(String id) {
		return getDefault().getImageRegistry().get(id);
	}
	
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * 컴포우저에서 호출하는 메소드
	 * @param map
	 * @return
	 */
	public static String[] newProjectForComposer(final HashMap<String, Object> map) {		
		if(map == null)
			return null;
		
		final String uuid = UUID.randomUUID().toString();
		final Object obj = map.get(ELEMENT_PROFILE);
		if(!(obj instanceof Document)) 
			return null;
		
		Document doc = (Document)obj;
		final Element root = doc.getRootElement();
		final String profileName = root.getAttributeValue(ATTRIBUTE_NAME);
		final String componentName = profileName.replace(".xml", "");
		
		//프로젝트가 존재할 경우
		if(OPRoSUtil2.isDuplicateProject(componentName)) {
			IProject project = OPRoSUtil2.getProject(componentName);
			IFile file = project.getFile(componentName + File.separator +"profile" + File.separator + componentName+".xml");
			/*
			if(file.isAccessible()) {
				final IEditorInput ei = new FileEditorInput(file);
				final IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {
							try {
								page.openEditor(ei, "kr.co.ed.opros.ce.editors.OPRoSGUIProfileEditor");
							}
							catch (PartInitException e) {
								e.printStackTrace();
							}							
						}
					});				
			}
			*/
			MessageDialog.openError(null, "Error", "A project("+componentName+") with that name already exists in the workspace");
			return new String[] {XmlUtil.getUUID(file), file.getParent().getParent().getLocation().toOSString()};
		}
		
		//컴포넌트를 새로 생성할 경우
		else {
			Display.getDefault().asyncExec(new Runnable() {					
				@Override
				public void run() {				
					
					Document profileDoc = (Document)map.get(profileName);	
					Element profileRoot = profileDoc.getRootElement();
					
					Element executionEnvironment = profileRoot.getChild(ELEMENT_EXECUTION_ENVIRONMENT);
					
					Element libraryType = executionEnvironment.getChild(ELEMENT_LIBRARY_TYPE);
					libraryType.setText("dll");
					
					Element libraryName = executionEnvironment.getChild(ELEMENT_LIBRARY_NAME);
					libraryName.setText(componentName+".dll");
					
					Element id = profileRoot.getChild(ELEMENT_ID);
					if(id == null){
						id = new Element(ELEMENT_ID);
						profileRoot.addContent(id);
					}
					id.setText(uuid);
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
					IProgressMonitor monitor = dialog.getProgressMonitor();
					dialog.open();	
					
					
					OPRoSProjectInfo info = getProjectInfo(componentName, executionEnvironment.getChildText(ELEMENT_IMPL_LANGUAGE));	
					
					OPRoSBodyElementModel model = new OPRoSBodyElementModel(null);			
					OPRoSGUIWizardPage page = new OPRoSGUIWizardPage(componentName, model.getComponentModel(), info);	
					
					IProject project = createOPRoSProject(componentName, info, monitor);
					IFolder componentFolder = page.modifyProject(project, componentName);		
					
					IFile profile =  createIFile(componentFolder.getFile("profile/"+componentName+".xml"), profileDoc, monitor);
					
					model.doLoad(profile.getLocation().toOSString().replace("\\", "/"), root);		
					model.getComponentModel().setComponentName(componentName);				
					page.performFinishProgress(monitor, project, componentFolder, profile);
					
					
					IFolder profileFolder = (IFolder)profile.getParent();
					String[] types = {ELEMENT_DATATYPE, ELEMENT_SERVICETYPE};
					for(String eleName : types) {
						createIFile(root.getChild(eleName), profileFolder, map, monitor);
					}
					
					
					monitor.done();
					dialog.close();
					openOPRoSPerspective();
				}
			});
			String componentLocation = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation().toOSString().replace("\\", "/")
					+ "/" + componentName + "/" + componentName;
			return new String[] {uuid, componentLocation};
		}
		
	}
	
	public static void createIFile(Element parent, IFolder parentFolder, HashMap<String, Object> map, IProgressMonitor monitor) {
		if(parent != null) {
			List<Element> dataTypes = parent.getChildren();
			for(Element element : dataTypes) {
				String name = element.getAttributeValue(ATTRIBUTE_NAME);
				createIFile(parentFolder.getFile(name), (Document)map.get(name), monitor);
			}
		}
	}
	
	public static IFile createIFile(IFile file, Document doc, IProgressMonitor monitor) {
		XMLOutputter xo = new XMLOutputter(Format.getPrettyFormat().setEncoding("euc-kr"));		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(xo.outputString(doc).getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			file.create(bais, true, monitor);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static OPRoSProjectInfo getProjectInfo(String compName, String complierType) {
		OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
		prjInfo.setPrjName(compName);
		prjInfo.addComponent(compName);
		prjInfo.setImplLanguage(complierType);
		prjInfo.setLocation(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
		return prjInfo;		
	}
	
	public static IProject createOPRoSProject(String name, OPRoSProjectInfo info, IProgressMonitor monitor) {
		IProject project = new OPRoSNewPrjWizard(name).createNewProject(info);
		try {
			OPRoSNature.addNature(project, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return project; 
	}
	
	/**
	 * 오프로스 컴포넌트 퍼스팩티브 오픈
	 */
	public static void openOPRoSPerspective() {
		// 퍼스팩티브 오픈
		IPerspectiveRegistry reg = (IPerspectiveRegistry) UIPlugin.getDefault()
				.getWorkbench().getPerspectiveRegistry(); 
		IPerspectiveDescriptor desc = (IPerspectiveDescriptor) reg
				.findPerspectiveWithId(OPRoSPerspectiveFactory.ID);
		PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage()
				.setPerspective(desc);
	}
	
	/**
	 * 프리퍼런스 스토어에 값을 입력한다.
	 * @param key
	 * @param value
	 */
	public static void setPreferenceValue(String key, String value) {
		getDefault().getPreferenceStore().setValue(key, value);
	}
	
	/**
	 * 프리퍼런스 스토어에서 값을 가져온다.
	 * @param key
	 * @return
	 */
	public static String getPreferenceValue(String key) {
		return getDefault().getPreferenceStore().getString(key);
	}

}
