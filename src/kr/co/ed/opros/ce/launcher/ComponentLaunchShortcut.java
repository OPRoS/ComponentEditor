package kr.co.ed.opros.ce.launcher;

import java.util.List;

import kr.co.ed.opros.ce.core.OPRoSProjectInfoEx;
import kr.co.ed.opros.ce.guieditor.dialog.ComponentSelectionDialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class ComponentLaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if(selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if(obj != null) {
				IProject project = null;
				if(obj instanceof IResource) {
					project = ((IResource)obj).getProject();								
				} 
				
				if(project != null && project.isAccessible()) {
					launch(project, mode);
				}
			}			
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		if(editor.getEditorInput() instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)editor.getEditorInput()).getFile();
			if(file != null && file.isAccessible()) {
				launch(file.getProject(), editor.getTitle(), mode);
			}
		}
	}
	
	public void launch(IProject project, String mode) {
		OPRoSProjectInfoEx info = new OPRoSProjectInfoEx();
		info.loadProfile(project);
		
		List<String> componentList = info.getComponentList();
		if(componentList == null)
			return;
		
		String compiler = info.getImplLanguage();
		String selectedComponentName = componentList.get(0);
		if(componentList.size() != 1) {
			ComponentSelectionDialog dialog = new ComponentSelectionDialog(null, componentList, "Select a component");
			dialog.open();
			
			if(dialog.getReturnCode() == Dialog.OK) {
				selectedComponentName = (String)dialog.getSelectedItem();
			} else {
				return;
			}			
			
		}
		launch(project, selectedComponentName, mode);
	}
	
	public void launch(IProject project, String componentName, String mode) {
		try {
			//런처 컨피그레이션 생성
			ILaunchConfiguration configuration = createLaunchConfiguration(project, componentName);
			configuration.launch(mode, null, false);
		} catch (CoreException e) {
			e.printStackTrace();				
		}
	}
	
	private ILaunchConfiguration createLaunchConfiguration(IProject project, String componentName) throws CoreException {		
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations();
		
		for (int i = 0; i < configs.length; i++) {
			String value = configs[i].getAttribute(ComponentLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
			if (value.equals(project.getName())) {
				configs[i].delete();
			}
		}

		ILaunchConfigurationType type = 
			manager.getLaunchConfigurationType(ComponentLaunchConfigurationConstants.ID_OPROS_COMPONENT_LAUNCHER);
		ILaunchConfigurationWorkingCopy wc = type.newInstance(null, componentName);

		wc.setAttribute(ComponentLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
		wc.setAttribute(ComponentLaunchConfigurationConstants.ATTR_COMPONENT_NAME, componentName);
		return wc.doSave();
	}

}
