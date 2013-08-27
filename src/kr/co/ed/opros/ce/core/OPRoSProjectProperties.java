package kr.co.ed.opros.ce.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;

/**
 * CPS 프로젝트관련 설정 파일 저장
 * @author lee6607
 *
 */
public class OPRoSProjectProperties {
	
	public static String QUAILIFIER = "kr.co.ed.opros";
	
	public static String SRC_FOLDER = "SRC_FOLDER";
	public static String BIN_FOLDER = "BIN_FOLDER";
	public static String IMPORT_LANGUAGE = "IMPORT_LANGUAGE";
	public static String LOCATION = "LOCATION";
	public static String COMPONENT_LIST = "COMPONENT_LIST";	
	
	protected IProject project;
	protected String projectName;
	protected IResource projectResource;
	
	protected String srcFolder;
	protected String binFolder;
	protected String implLanguage="MinGW C++";
	protected String location;
	protected List<String> componentList;
	
	public OPRoSProjectProperties(IProject project) {
		this.setProject(project);
	}
	
	public OPRoSProjectProperties(String projectName) {
		this.setProjectName(projectName); 
	}
	
	public void setProject(IProject project) {
		this.project = project;
		this.projectName = project.getName();
		this.projectResource = ResourcesPlugin.getWorkspace().getRoot().findMember(this.projectName);
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
		IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		this.setProject(prj);
	}
	
	
	
	/**
	 * save properties
	 * @throws CoreException
	 */
	public void store() throws CoreException {
		this.store(this);
	}
	/**
	 * save propertes with param prop
	 * @param prop
	 */
	public void store(OPRoSProjectProperties prop) throws CoreException {
		//this.storeToSettings(prop);
		this.projectResource.setPersistentProperty(new QualifiedName(QUAILIFIER, SRC_FOLDER), prop.getSrcFolder());
		this.projectResource.setPersistentProperty(new QualifiedName(QUAILIFIER, BIN_FOLDER), prop.getBinFolder());		
		this.projectResource.setPersistentProperty(new QualifiedName(QUAILIFIER, IMPORT_LANGUAGE), prop.getImplLanguage());		
		this.projectResource.setPersistentProperty(new QualifiedName(QUAILIFIER, LOCATION), prop.getLocation());
		this.projectResource.setPersistentProperty(new QualifiedName(QUAILIFIER, COMPONENT_LIST), getComponentListStr(prop.getComponentList()));	
	}
	
	/*
	private void storeToSettings(OPRoSProjectProperties prop) {
		Preferences node1 = Platform.getPreferencesService().getRootNode().node(ProjectScope.SCOPE);
		node1 = node1.node(this.projectName);
		try {
			node1.put(QUAILIFIER+"."+L_MAP_NAME, prop.getMapName());
			node1.put(QUAILIFIER+"."+L_CPS_FILE_NAME, prop.getCpsFileName());
			
			node1.flush();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	*/
	
	public OPRoSProjectProperties load() throws CoreException {
		this.srcFolder = projectResource.getPersistentProperty(new QualifiedName(QUAILIFIER, SRC_FOLDER));
		this.binFolder = projectResource.getPersistentProperty(new QualifiedName(QUAILIFIER, BIN_FOLDER));
		this.implLanguage = projectResource.getPersistentProperty(new QualifiedName(QUAILIFIER, IMPORT_LANGUAGE));
		this.location = projectResource.getPersistentProperty(new QualifiedName(QUAILIFIER, LOCATION));;
		this.componentList = getComponentList(projectResource.getPersistentProperty(new QualifiedName(QUAILIFIER, COMPONENT_LIST)));		
		return this;
	}
	
	public String getSrcFolder() {
		return srcFolder;
	}

	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}

	public String getBinFolder() {
		return binFolder;
	}

	public void setBinFolder(String binFolder) {
		this.binFolder = binFolder;
	}

	public String getImplLanguage() {
		return implLanguage;
	}

	public void setImplLanguage(String implLanguage) {
		this.implLanguage = implLanguage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getComponentList() {
		return componentList;
	}

	public void setComponentList(ArrayList<String> componentList) {
		this.componentList = componentList;
	}
	
	public void addComponent(String name) {
		if(!componentList.contains(name)) {
			componentList.add(name);
		}
	}
	
	public String getComponentListStr(List<String> list) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<list.size(); i++ ) {
			buffer.append(list.get(i));
			
			if(i!=list.size() -1) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}
	
	public List<String> getComponentList(String str) {
		List<String> list = new ArrayList<String>();
		String[] args = str.split(",");
		if(args != null && args.length != 0) {
			for(String name : args) {
				if(name != null && !name.equals("")) {
					list.add(name);
				}
			}
		}
		return list; 
	}
	
}
