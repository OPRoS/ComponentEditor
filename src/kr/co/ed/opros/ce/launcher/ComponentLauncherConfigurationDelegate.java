package kr.co.ed.opros.ce.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.FileUtils;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.core.EngineProcess;
import kr.co.ed.opros.ce.core.EngineProcessManager;
import kr.co.ed.opros.ce.preferences.PreferenceConstants;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class ComponentLauncherConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String projectName = configuration.getAttribute(ComponentLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
		String componentName = configuration.getAttribute(ComponentLaunchConfigurationConstants.ATTR_COMPONENT_NAME, "");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		if(project != null && project.isAccessible()) {
			String engineFilePath = OPRoSActivator.getDefault().getPreferenceValue(PreferenceConstants.OPROS_ENGINE_FILE);
			String engineRepositoryPath = OPRoSActivator.getDefault().getPreferenceValue(PreferenceConstants.OPROS_REPOSITORY_PATH);
			File engineFile = new File(engineFilePath);
			File repositoryFolder = new File(engineRepositoryPath);
			
			if((engineFile != null && engineFile.isFile())
					&&(repositoryFolder != null && repositoryFolder.isDirectory())) {
				List<IFile> list = sourceFiles(project, componentName, mode);
				if(list != null && list.size() != 0) {
					//파일 복사전 모든 프로세스를 정지시킨다. 엔진이 파일을 잡고있으면 복사안되는 문제때문(임시)
					EngineProcessManager.getInstence().allTerminate();
					
					
					//파일 복사					
					boolean isSuccess = copyFile(list, repositoryFolder);					
					if(!isSuccess) {
						showMessage(getErrorMessageRunnable("Failed to copy file"));
						return;
					}
					
					//엔진실행
					EngineProcess eProcess 
							= EngineProcessManager.getInstence().getProcess(launch);
					if(eProcess == null) { 
						showMessage(getErrorMessageRunnable("Run the engine failed."));
						return;						
					}
					
					//엔진에 컴포넌트 실행 명령 전달
					eProcess.excuteCommand("app set " +componentName+ "\r\n");
					eProcess.excuteCommand("app run\r\n");
					
					//디버그 모드일 경우 GDB 서버에 접속한다.					
					if(mode.equals(ILaunchManager.DEBUG_MODE)) {				
						launchCAttach(project, eProcess.getPid(), monitor);
					}
				}
			} else {	
				showMessage(getErrorMessageRunnable(
						"Please, OPRoS Compiler Setting in the menu " +
						"of Window -> Preference -> OPRoS -> OPRoS_CE_Compiler.\n"));
			}
		}		
	}
	
	/**
	 * 로컬 컴포넌트 디버깅
	 * @param project
	 * @param monitor
	 */
	private void launchCAttach(IProject project, int pid, IProgressMonitor monitor) {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ICDTLaunchConfigurationConstants.ID_LAUNCH_C_ATTACH);
		try
		{
			ILaunchConfigurationWorkingCopy configuration = type.newInstance(null, project.getName()+" (debug)");
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME, "Debug/"+project.getName()+"."+OPRoSUtil.getLibType());
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE, ICDTLaunchConfigurationConstants.DEBUGGER_MODE_ATTACH);
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID, "org.eclipse.cdt.debug.mi.core.MinGW");
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_ATTACH_PROCESS_ID, pid);
			
			configuration.launch(ILaunchManager.DEBUG_MODE, monitor);
		} catch(CoreException e) {
			e.printStackTrace();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일 복사
	 * @param list
	 * @param target
	 * @return
	 */
	private boolean copyFile(List<IFile> list, File target) {
		for(IFile source : list) {
			if(!FileUtils.copy(source, 
					target.getPath() + File.separator + source.getName())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 엔진 레파지토리로 복사될 파일 리스트를 리턴한다.
	 * @param project
	 * @param mode
	 * @return
	 */
	private List<IFile> sourceFiles(IProject project, String componentName, String mode) {
		//복사할 파일 구하기
		IFile profile = project.getFile(componentName + "/profile/"+componentName+".xml");
		IFile binary = project.getFile(componentName + "/"
				+convertModeStr(mode) 
				+ File.separator 
				+ componentName
				+"."
				+ OPRoSUtil.getLibType());
		
		if(profile == null || !profile.isAccessible()) { 
			showMessage(getErrorMessageRunnable("Launch failed. Component profile not found."));
			return null;
		} else if(binary == null || !binary.isAccessible()) {
			showMessage(getErrorMessageRunnable("Launch failed. Component binary not found."));
			return null;
		}	
		List<IFile> list = new ArrayList<IFile>();
		list.add(profile);
		list.add(binary);
		
		return list;
	}
	
	public String convertModeStr(String mode) {
		String newMode = "";
		if(mode.equals("run")) 
			newMode = "Release";
		else if(mode.equals("debug")) 
			newMode = "Debug";
		return newMode;
	}
	
	private void showMessage(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}
	
	private Runnable getErrorMessageRunnable(final String message) {
		return new Runnable() {			
			@Override
			public void run() {
				MessageDialog.openError(null, "Component launcher", message);
			}
		};
	}

}
