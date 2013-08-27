package kr.co.ed.opros.ce.ui.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil2;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.internal.wizards.datatransfer.TarFileExporter;
import org.opros.mainpreference.Activator;
import org.opros.mainpreference.preferences.PreferenceConstants;

public class PackagingComponent implements IWorkbenchWindowActionDelegate {
	
	public IFile inputFile;
	public TarFileExporter exporter;
	public String componentName;
	
	@Override
	public void run(IAction action) {
		//프로젝트 빌드		
		OPRoSUtil2.refreshProject(inputFile.getProject());
		
		IFolder componentFolder = (IFolder)inputFile.getParent().getParent();
		IFile file = componentFolder.getFile(new Path("OPRoS.mf"));
		if(file.isAccessible()) {
			try {
				List<IResource> list = OPRoSUtil2.getSavedResource(file, componentName);
				
				//릴리즈, 디버그 파일을 구분하여 추가
				IResource[] resource = componentFolder
				.getFolder(new Path("Release")).members();
		
				if(resource != null && resource.length != 0){
					for(int i=0; i<resource.length; i++) {
						if(resource[i].getFileExtension().equals("dll")) {
							list.add(resource[i]);
						}
					}
				}
				
				//메니페스트 파일 추가
				list.add(file);
				
				//컴포넌트 프로파일 추가
				list.add(inputFile);						
				
				String[] destinations = {Activator.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.LOCAL_REPOSITORY_PATH)
						+"\\"+ componentName + ".tar", 
						componentFolder.getProject().getLocation().toOSString() + File.separator +componentName + ".tar"};
				
				for(String d : destinations) {
					if(new File(d).getParentFile().isDirectory()) {
						exporter = new TarFileExporter(d, false);
						for(IResource res : list) {
							writeResource(res, "/");
						}
						exporter.finished();
						OPRoSUtil2.refreshProject(inputFile.getProject());
					}
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {		
		arg0.setEnabled(false);
    	inputFile=null;
		
		if (selection == null || selection.isEmpty()) 
			return;
		
		IResource resource = OPRoSUtil2.getOPRoSComponent2(selection);
		if(resource != null) {
			arg0.setEnabled(true);
			componentName =  resource.getName();
			String proFile=resource.getName(); 
			proFile+="/profile/";
			proFile+= resource.getName();
			proFile+=".xml";
			if(resource.getProject().getFile(proFile).isAccessible()) {
				inputFile = resource.getProject().getFile(proFile);
			}			
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}
	
	/**
	 * 타르 익스포터에 리소스 추가
	 * @param resource
	 * @param parentPath
	 */
	public void writeResource(IResource resource, String parentPath) {
		if(resource instanceof IFile) {
			IFile res = (IFile)resource;	
			try {
				exporter.write(res, parentPath + File.separator + res.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		else if(resource instanceof IFolder) {
			IFolder folder = (IFolder) resource;
			try {
				IResource[] res = folder.members();
				for(IResource re : res) {
					writeResource(re, parentPath + File.separator + folder.getName());
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

}
