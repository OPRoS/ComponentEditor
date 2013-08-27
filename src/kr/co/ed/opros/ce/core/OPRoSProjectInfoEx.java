package kr.co.ed.opros.ce.core;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.jdom.Document;

import kr.co.ed.opros.ce.OPRoSNature;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;

public class OPRoSProjectInfoEx extends OPRoSProjectInfo {
	
	public boolean loadProfile(IProject project) {
		if(project != null && project.isAccessible() &&
				OPRoSUtil2.isOPRoSProject(project, OPRoSNature.NATURE_ID)) {
			IFile iFile = project.getFile(project.getName()+"Prj.xml");
			if(iFile != null && iFile.isAccessible()) {
				Document document = XmlUtil.getIFileDocument(iFile);
				if(document != null) {
					loadProfile(document.getRootElement());
					return true;
				}
			}			
		}
		return false;
	}
	
	public List<String> getComponentList() {
		return componentList;
	}
}
