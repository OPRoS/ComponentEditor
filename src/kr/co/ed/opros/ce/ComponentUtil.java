package kr.co.ed.opros.ce;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;

public class ComponentUtil {
	/**
	 * 메니페스트 파일에 쓸 문자열을 생성한다.
	 * @param resList res 디팬던시 리스트
	 * @param libList lib 디팬던시 리스트
	 * @return
	 * @throws Exception
	 */
	public static String  createManifestStr(List<Object> list, String componentName, IFolder componentFolder, String version, String desc, boolean isSourceAttacth) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Manifest-Version:"+version+"\n");
		buffer.append("Archive-type:OPRoS_Component\n");
		buffer.append("Archive-name:"+componentName+".tar\n");
		buffer.append("Archive-description:"+desc+"\n");
		buffer.append("Archive-Element:"+componentName+".xml\n");	
		
		
		IFolder prfileFolder = componentFolder.getFolder(new Path("profile"));
		if(prfileFolder.isAccessible()) {
			IResource[] profileResources = prfileFolder.members();
			for(IResource profile : profileResources) {
				if(profile instanceof IFile && !profile.getName().equals(componentName+".xml")) {
					buffer.append("element:"+((IFile)profile).getName()+"\n");
				}
			}
		}
		
		IFolder releaseFolder = prfileFolder.getFolder(new Path("Release"));
		if(releaseFolder.isAccessible()) {
			IResource[] releaseResources = releaseFolder.members();
			for(IResource release : releaseResources) {
				if(release instanceof IFile && ((IFile)release).getFileExtension().equals("dll")) {
					buffer.append("element:"+((IFile)release).getName()+"\n");
				}
			}
		}
		
		if(isSourceAttacth) {
			buffer.append("element:src/\n");
		}
		
	
		if(list!= null)
			for(Object obj : list) {
				if(obj instanceof IFile) {
					buffer.append("element:"+((IFile)obj).getName()+"\n");
				}
				else if(obj instanceof IFolder) {
					buffer.append("element:"+((IFolder)obj).getName()+"/\n");
				}
			}
		else
			buffer.append("\n");
		
		return buffer.toString();
	}
	
	/**
	 * 디팬던시에 포함되는 파일이름 리스트를 만든다.
	 * @param buffer
	 * @param list
	 */
	public static void createDependenciesFileStr(StringBuffer buffer, List<Object> list, IFile file) throws Exception {
		for(int i=0; i<list.size(); i++) {
			if(list.get(i) instanceof IFile) {
				
				String libPath = ((IFolder) OPRoSUtil2
						.getComponentContainer(file))
						.getFolder("lib").getLocation().toOSString()
						+ "\\";
				String resPath = ((IFolder) OPRoSUtil2
						.getComponentContainer(file))
						.getFolder("res").getLocation().toOSString()
						+ "\\";

				String fileName = ((IFile) list.get(i)).getLocation()
						.toOSString().replace(libPath, "").replace(resPath, "");
				buffer.append(fileName);
				
				if(list.size()-1 != i) 
					buffer.append(", ");
				else
					buffer.append("\n");
			}
		}
	}
	
	public static List<Object> getDependenciesFiles(List<Object> list, String type) {
		List<Object> files = new ArrayList<Object>();		
		for(Object obj : list) {
			String location = ((IResource)obj).getLocation().toOSString();			
			if(obj instanceof IFile) {
				if(location.matches(".*"+type+".*")) {
					files.add(obj);
				}
			}
		}		
		return files;
	}
	
	
}
